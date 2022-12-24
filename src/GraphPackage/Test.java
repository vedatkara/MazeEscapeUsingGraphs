package GraphPackage;

import ADTPackage.LinkedQueue;
import ADTPackage.LinkedStack;
import ADTPackage.QueueInterface;
import ADTPackage.StackInterface;

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

        int random;
        for(int i = line-1; i >= 0; i--){
            for(int j = width - 1; j >= 1; j--){
                label = i + "-" + j;
                random = (int) (1 + 4 * Math.random());
                if(maze.findVertex(label)){
                    if(maze.findVertex(i + "-" + (j-1)))
                        maze.addEdge(label, i + "-" + (j-1), random);
                    if(maze.findVertex(i + "-" + (j+1)))
                        maze.addEdge(label, i + "-" + (j+1), random);
                    if(maze.findVertex((i+1) + "-" + j))
                        maze.addEdge(label, (i+1) + "-" + j, random);
                    if(maze.findVertex((i-1) + "-" + j))
                        maze.addEdge(label, (i-1) + "-" + j, random);

                }
            }
        }

//        StackInterface<String> stack = new LinkedStack<>();
//        System.out.println(maze.getShortestPath(startVertex, endVertex,stack));
//        while(!stack.isEmpty()){
//            if(stack.peek().equals(endVertex)) {
//                System.out.print(stack.pop());
//                break;
//            }
//            else
//                System.out.print(stack.pop() + "-->");
//        }

        System.out.println("\n---------------------------BFS----------------------------");
        QueueInterface<String> que;
        que = maze.getBreadthFirstTraversal(startVertex, endVertex);
        int visNum = printQueue(que,endVertex, line, width,file);
        System.out.println("Number of vertices visited: " + visNum );



//        QueueInterface<String> print = new LinkedQueue<>();
//        print = maze.getBreadthFirstTraversal("1","121");
//
//        while(!print.isEmpty())
//            System.out.println(print.dequeue());




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

}
