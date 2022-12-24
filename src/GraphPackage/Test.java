package GraphPackage;

import ADTPackage.LinkedQueue;
import ADTPackage.LinkedStack;
import ADTPackage.QueueInterface;
import GraphPackage.UndirectedGraph;
import GraphPackage.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {

    public void solve(String path) throws FileNotFoundException {

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

        for(int i = line-1; i >= 0; i--){
            for(int j = width - 1; j >= 0; j--){
                label = i + "-" + j;

                if(maze.findVertex(label)){
                    if(maze.findVertex(i + "-" + (j+1)))
                        maze.addEdge(label, i + "-" + (j+1), 0);
                    if(maze.findVertex(i + "-" + (j-1)))
                        maze.addEdge(label, i + "-" + (j-1),0);
                    if(maze.findVertex((i+1) + "-" + j))
                        maze.addEdge(label, (i+1) + "-" + j,0);
                    if(maze.findVertex((i-1) + "-" + j))
                        maze.addEdge(label, (i-1) + "-" + j,0);
                }
            }
        }

        maze.displayEdges();

        QueueInterface<String> que;
        que = maze.getBreadthFirstTraversal(startVertex, endVertex);
        while(!que.isEmpty())
            System.out.println(que.dequeue());

//        Scanner scan1 = new Scanner(file).useDelimiter("\n");
//        line = 0;
//
//        while(scan1.hasNextLine()){
//            s = scan1.next();
//
//            for(int i = 0; i < s.length()-1; i++){
//                if(s.charAt(i) != '#') {
//                    label = line + "-" + i;
//                    if(que.getFront().equals(label)) {
//                        System.out.print(".");
//                        que.dequeue();
//                    }
//                }
//                else
//                    System.out.print("#");
//            }
//            System.out.println();
//            line++;
//        }


//        QueueInterface<String> print = new LinkedQueue<>();
//        print = maze.getBreadthFirstTraversal("1","121");
//
//        while(!print.isEmpty())
//            System.out.println(print.dequeue());




    }

}
