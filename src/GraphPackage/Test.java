package GraphPackage;

import ADTPackage.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        try {
            String path = "C:\\Users\\vedat\\OneDrive\\Masaüstü\\MazeEscapeUsingGraphs\\maze1.txt";
            solve(path);
        } catch (Exception e) {
            System.out.println("File could not found");
        }
    }

    public static void solve(String path) throws FileNotFoundException {

        UndirectedGraph<String> maze = new UndirectedGraph<>();

        File file = new File(path);
        Scanner scan = new Scanner(file).useDelimiter("\n");
        String s, label = "", startVertex = "0-1", endVertex = null;
        int line = 0, width= 0;

        while(scan.hasNextLine()) {
            s = scan.next();
            width = s.length();
            for(int i = 0; i < s.length()-1; i++){
                if(s.charAt(i) != '#') {
                    label = line + "-" + i;
                    maze.addVertex(label);
                }
            }
            endVertex = label;
            line++;
        }
        scan.close();

        int random;
        for(int i = line-1; i >= 0; i--){
            for(int j = width - 1; j >= 1; j--){
                label = i + "-" + j;
                if(maze.findVertex(label)){
                    if(maze.findVertex(i + "-" + (j-1))){
                        random = (int) (1 + 4 * Math.random());
                        maze.addEdge(label, i + "-" + (j-1), random);
                    }
                    if(maze.findVertex(i + "-" + (j+1))){
                        random = (int) (1 + 4 * Math.random());
                        maze.addEdge(label, i + "-" + (j+1), random);
                    }
                    if(maze.findVertex((i+1) + "-" + j)) {
                        random = (int) (1 + 4 * Math.random());
                        maze.addEdge(label, (i + 1) + "-" + j, random);
                    }
                    if(maze.findVertex((i-1) + "-" + j)) {
                        random = (int) (1 + 4 * Math.random());
                        maze.addEdge(label, (i - 1) + "-" + j, random);
                    }

                }
            }
        }
        int visNum;

        System.out.println("\n---------------------------EDGES--------------------------");
        maze.displayEdges();
        maze.adjacenyList();
        maze.adjacencyMatrix();
        System.out.println("\nNumber of edges found: " + maze.getNumberOfEdges());

        System.out.println("\n----------------------------CP----------------------------");
        StackInterface<String> stack1 = new LinkedStack<>();
        double cost = maze.getCheapestPath(startVertex, endVertex, stack1);
        visNum = printStack(stack1, endVertex, line, width, file);
        System.out.println("Weight: " + (int)cost );
        System.out.println("Number of vertices visited: " + visNum );


        System.out.println("\n---------------------------BFS----------------------------");
        QueueInterface<String> que;
        que = maze.getBreadthFirstTraversal(startVertex, endVertex);
        visNum = printQueue(que,endVertex, line, width,file);
        System.out.println("Number of vertices visited: " + visNum );

        System.out.println("\n---------------------------DFS----------------------------");
        QueueInterface<String> que1;
        que1 = maze.getDepthFirstTraversal(startVertex, endVertex);
        visNum = printQueue(que1,endVertex, line, width,file);
        System.out.println("Number of vertices visited: " + visNum );

        System.out.println("\n---------------------------SP----------------------------");
        StackInterface<String> stack = new LinkedStack<>();
        maze.getShortestPath(startVertex, endVertex, stack);
        visNum = printStack(stack, endVertex,line, width, file);
        System.out.println("Number of vertices visited: " + visNum );


    }

    public static int printQueue(QueueInterface<String> queue, String end, int height, int width, File file) throws FileNotFoundException {

        Character[][] output = new Character[height][width];
        Scanner scan = new Scanner(file).useDelimiter("\n");
        String s, label;
        String[] splittedLabel;
        int line = 0, row, column, visNum = 0;

        while(scan.hasNextLine()){
            s = scan.next();
            for(int i = 0; i < s.length()-1; i++)
                output[line][i] = s.charAt(i);
            line++;
        }
        output[line-1][width-1] = '#';
        scan.close();


        System.out.println("\n---------------------------PATH---------------------------");
        while (!queue.isEmpty()) {
            if (queue.getFront().equals(end)) {
                label = queue.dequeue();
                splittedLabel = label.split("-");
                row = Integer.parseInt(splittedLabel[0]);
                column = Integer.parseInt(splittedLabel[1]);
                output[row][column] = '.';
                System.out.print(label);
                visNum++;
                break;
            } else{
                label = queue.dequeue();
                splittedLabel = label.split("-");
                row = Integer.parseInt(splittedLabel[0]);
                column = Integer.parseInt(splittedLabel[1]);
                output[row][column] = '.';
                System.out.print(label + "-->");
                visNum++;
            }
        }

        System.out.println("\n--------------------------OUTPUT--------------------------");
        for(int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                System.out.print(output[i][j]);
            }
            System.out.println();
        }
        return visNum;
    }

    public static int printStack(StackInterface<String> stack, String end, int height, int width, File file) throws FileNotFoundException {
        Character[][] output = new Character[height][width];
        Scanner scan = new Scanner(file).useDelimiter("\n");
        String s, label;
        String[] splittedLabel;
        int line = 0, row, column, visNum = 0;

        while(scan.hasNextLine()){
            s = scan.next();
            for(int i = 0; i < s.length()-1; i++)
                output[line][i] = s.charAt(i);
            line++;
        }
        output[line-1][width-1] = '#';
        scan.close();

        System.out.println("\n---------------------------PATH---------------------------");
        while(!stack.isEmpty()){
            if (stack.peek().equals(end)) {
                label = stack.pop();
                splittedLabel = label.split("-");
                row = Integer.parseInt(splittedLabel[0]);
                column = Integer.parseInt(splittedLabel[1]);
                output[row][column] = '.';
                System.out.print(label);
                visNum++;
                break;
            } else{
                label = stack.pop();
                splittedLabel = label.split("-");
                row = Integer.parseInt(splittedLabel[0]);
                column = Integer.parseInt(splittedLabel[1]);
                output[row][column] = '.';
                System.out.print(label + "-->");
                visNum++;
            }
        }

        System.out.println("\n--------------------------OUTPUT--------------------------");
        for(int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                System.out.print(output[i][j]);
            }
            System.out.println();
        }

        return visNum;
    }


}
