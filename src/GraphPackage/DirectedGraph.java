package GraphPackage;

import java.util.Iterator;

import ADTPackage.*; // Classes that implement various ADTs


public class DirectedGraph<T> implements GraphInterface<T> {
    private DictionaryInterface<T, VertexInterface<T>> vertices;
    private int edgeCount;

    public DirectedGraph() {
        vertices = new UnsortedLinkedDictionary<>();
        edgeCount = 0;
    } // end default constructor

    public boolean findVertex(T vertexlabel) {

        if (vertices.contains(vertexlabel))
            return true;
        return false;
    }

    public boolean addVertex(T vertexLabel) {
        VertexInterface<T> addOutcome = vertices.add(vertexLabel, new Vertex<>(vertexLabel));
        return addOutcome == null; // Was addition to dictionary successful?
    } // end addVertex

    public boolean addEdge(T begin, T end, double edgeWeight) {
        boolean result = false;
        VertexInterface<T> beginVertex = vertices.getValue(begin);
        VertexInterface<T> endVertex = vertices.getValue(end);
        if ((beginVertex != null) && (endVertex != null))
            result = beginVertex.connect(endVertex, edgeWeight);
        if (result)
            edgeCount++;
        return result;
    } // end addEdge

    public boolean addEdge(T begin, T end) {
        return addEdge(begin, end, 0);
    } // end addEdge

    public boolean hasEdge(T begin, T end) {
        boolean found = false;
        VertexInterface<T> beginVertex = vertices.getValue(begin);
        VertexInterface<T> endVertex = vertices.getValue(end);
        if ((beginVertex != null) && (endVertex != null)) {
            Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();
            while (!found && neighbors.hasNext()) {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (endVertex.equals(nextNeighbor))
                    found = true;
            } // end while
        } // end if

        return found;
    } // end hasEdge

    public boolean isEmpty() {
        return vertices.isEmpty();
    } // end isEmpty

    public void clear() {
        vertices.clear();
        edgeCount = 0;
    } // end clear

    public int getNumberOfVertices() {
        return vertices.getSize();
    } // end getNumberOfVertices

    public int getNumberOfEdges() {
        return edgeCount;
    } // end getNumberOfEdges

    protected void resetVertices() {
        Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
        while (vertexIterator.hasNext()) {
            VertexInterface<T> nextVertex = vertexIterator.next();
            nextVertex.unvisit();
            nextVertex.setCost(0);
            nextVertex.setPredecessor(null);
        } // end while
    } // end resetVertices

    public StackInterface<T> getTopologicalOrder() {
        resetVertices();

        StackInterface<T> vertexStack = new LinkedStack<>();
        int numberOfVertices = getNumberOfVertices();
        for (int counter = 1; counter <= numberOfVertices; counter++) {
            VertexInterface<T> nextVertex = findTerminal();
            nextVertex.visit();
            vertexStack.push(nextVertex.getLabel());
        } // end for

        return vertexStack;
    } // end getTopologicalOrder


    public QueueInterface<T> getBreadthFirstTraversal(T origin, T end) {
        resetVertices();
        QueueInterface<T> traversalOrder = new LinkedQueue<>();
        QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<>();
        VertexInterface<T> originVertex = vertices.getValue(origin);
        originVertex.visit();
        traversalOrder.enqueue(origin); // Enqueue vertex label
        vertexQueue.enqueue(originVertex); // Enqueue vertex
        while (!vertexQueue.isEmpty()) {
            VertexInterface<T> frontVertex = vertexQueue.dequeue();
            Iterator<VertexInterface<T>> neighbors =
                    frontVertex.getNeighborIterator();
            while (neighbors.hasNext()) {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited()) {
                    nextNeighbor.visit();
                    traversalOrder.enqueue(nextNeighbor.getLabel());
                    vertexQueue.enqueue(nextNeighbor);
                } // end if
            } // end while
        } // end while
        return traversalOrder;
    }

    public QueueInterface<T> getDepthFirstTraversal(T origin, T end) {
        resetVertices();
        QueueInterface<T> traversalOrder = new LinkedQueue<>();
        StackInterface<VertexInterface<T>> vertexStack = new LinkedStack<>();
        VertexInterface<T> originVertex = vertices.getValue(origin);
        originVertex.visit();
        traversalOrder.enqueue(origin);
        vertexStack.push(originVertex);

        while (!vertexStack.isEmpty()) {
            VertexInterface<T> topVertex = vertexStack.peek();

                if (topVertex.getUnvisitedNeighbor() != null) {
                    VertexInterface<T> nextNeighbor = topVertex.getUnvisitedNeighbor();
                    nextNeighbor.visit();
                    traversalOrder.enqueue(nextNeighbor.getLabel());
                    vertexStack.push(nextNeighbor);
                } else
                    vertexStack.pop();

        }

        return traversalOrder; //depth first search traversal order between origin vertex and end vertex
    }


    public int getShortestPath(T begin, T end, StackInterface<T> path) {
        resetVertices();
        boolean done = false;
        QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<>();
        VertexInterface<T> originVertex = vertices.getValue(begin);
        VertexInterface<T> endVertex = vertices.getValue(end);
        originVertex.visit();

        vertexQueue.enqueue(originVertex);
        while (!done && !vertexQueue.isEmpty()) {
            VertexInterface<T> frontVertex = vertexQueue.dequeue();
            Iterator<VertexInterface<T>> neighbors =
                    frontVertex.getNeighborIterator();
            while (!done && neighbors.hasNext()) {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited()) {
                    nextNeighbor.visit();
                    nextNeighbor.setCost(1 + frontVertex.getCost());
                    nextNeighbor.setPredecessor(frontVertex);
                    vertexQueue.enqueue(nextNeighbor);
                }
                if (nextNeighbor.equals(endVertex))
                    done = true;
            }
        }

        int pathLength = (int) endVertex.getCost();
        path.push(endVertex.getLabel());
        VertexInterface<T> vertex = endVertex;
        while (vertex.hasPredecessor()) {
            vertex = vertex.getPredecessor();
            path.push(vertex.getLabel());
        }
        return pathLength;
    }

    /**
     * Precondition: path is an empty stack (NOT null)
     */
    /* Use EntryPQ instead of Vertex in Priority Queue because multiple entries contain
     * 	the same vertex but different costs - cost of path to vertex is EntryPQ's priority value
     */
    public double getCheapestPath(T begin, T end, StackInterface<T> path) {
        boolean done = false;
        PriorityQueueInterface<EntryPQ> vertexQueue = new HeapPriorityQueue<>();
        VertexInterface<T> originVertex = vertices.getValue(begin);
        VertexInterface<T> endVertex = vertices.getValue(end);
        vertexQueue.add(new EntryPQ(originVertex, 0, null));

        while (!done && !vertexQueue.isEmpty()) {
            EntryPQ frontEntry = vertexQueue.remove();
            VertexInterface<T> frontVertex = frontEntry.vertex;

            if (!frontVertex.isVisited()) {
                frontVertex.visit();
                frontVertex.setCost(frontEntry.cost);
                frontVertex.setPredecessor(frontEntry.getPredecessor());

                if (frontVertex.equals(endVertex))
                    done = true;
                else {
                    Iterator<VertexInterface<T>> neighbors =
                            frontVertex.getNeighborIterator();
                    while (neighbors.hasNext()) {
                        VertexInterface<T> nextNeighbor = neighbors.next();
                        Iterator<Double> weights = nextNeighbor.getWeightIterator();
                        double weightOfEdgeToNeighbor = weights.next();

                        if (!nextNeighbor.isVisited()) {
                            double nextCost = weightOfEdgeToNeighbor + frontVertex.getCost();
                            vertexQueue.add(new EntryPQ(nextNeighbor, nextCost, frontVertex));
                        }
                    }
                }
            }
        }
        int pathCost = (int) endVertex.getCost();
        path.push(endVertex.getLabel());
        VertexInterface<T> vertex = endVertex;

        while (vertex.hasPredecessor()) {
            vertex = vertex.getPredecessor();
            path.push(vertex.getLabel());
        }
        return pathCost;
    }


    //###########################################################################
    protected VertexInterface<T> findTerminal() {
        boolean found = false;
        VertexInterface<T> result = null;

        Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();

        while (!found && vertexIterator.hasNext()) {
            VertexInterface<T> nextVertex = vertexIterator.next();

            // If nextVertex is unvisited AND has only visited neighbors)
            if (!nextVertex.isVisited()) {
                if (nextVertex.getUnvisitedNeighbor() == null) {
                    found = true;
                    result = nextVertex;
                } // end if
            } // end if
        } // end while

        return result;
    } // end findTerminal

    // Used for testing
    public void displayEdges() {
        System.out.println("\nEdges exist from the first vertex in each line to the other vertices in the line.");
        System.out.println("(Edge weights are given; weights are zero for unweighted graphs):\n");
        Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
        while (vertexIterator.hasNext()) {
            ((Vertex<T>) (vertexIterator.next())).display();
        } // end while
    } // end displayEdges

    private class EntryPQ implements Comparable<EntryPQ> {
        private VertexInterface<T> vertex;
        private VertexInterface<T> previousVertex;
        private double cost; // cost to nextVertex

        private EntryPQ(VertexInterface<T> vertex, double cost, VertexInterface<T> previousVertex) {
            this.vertex = vertex;
            this.previousVertex = previousVertex;
            this.cost = cost;
        } // end constructor

        public VertexInterface<T> getVertex() {
            return vertex;
        } // end getVertex

        public VertexInterface<T> getPredecessor() {
            return previousVertex;
        } // end getPredecessor

        public double getCost() {
            return cost;
        } // end getCost

        public int compareTo(EntryPQ otherEntry) {
            // Using opposite of reality since our priority queue uses a maxHeap;
            // could revise using a minheap
            return (int) Math.signum(otherEntry.cost - cost);
        } // end compareTo

        public String toString() {
            return vertex.toString() + " " + cost;
        } // end toString
    } // end EntryPQ

    public void adjacenyMatrix() {
        System.out.println("\n----------------------ADJACENY LİST-----------------------");
        Iterator valueIterator = vertices.getValueIterator();
        Iterator neighborIterator;
        Vertex<String> vertex, neighbor;
        String label = "";
        String[][] matrix = new String[getNumberOfVertices()][getNumberOfVertices()];
        String[] splittedLabel;
        int row = getNumberOfVertices()-1, column = getNumberOfVertices()-1;

        while (valueIterator.hasNext()) {

            vertex = (Vertex<String>) valueIterator.next();
            label = vertex.getLabel();
//            System.out.println("[" + label + "]");
//            splittedLabel = label.split("-");
            neighborIterator = vertex.getNeighborIterator();
            while (neighborIterator.hasNext()) {
                matrix[row][column] = "0";
                if(column != 0)
                    matrix[row][column -1] = "1";
                neighbor = (Vertex<String>) neighborIterator.next();
                label = neighbor.getLabel();
                System.out.println(label);
                column--;
            }
            row--;
        }

        for(int i = 0; i < getNumberOfVertices(); i++){
            for(int j = 0; j < getNumberOfVertices(); j++){
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }

    }
} // end DirectedGraph
