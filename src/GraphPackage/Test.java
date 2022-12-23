package GraphPackage;

import ADTPackage.LinkedQueue;
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
        String s, label;
        int line = 0, size= 0;

        while(scan.hasNextLine()) {
            line++;
            s = scan.next();
            size = s.length();
            for(int i = 0; i < s.length()-1; i++){
                if(s.charAt(i) != '#') {
                    label = line + "-" + (i+1);
                    maze.addVertex(label);
                }
            }
            System.out.println();
        }








//        QueueInterface<String> print = new LinkedQueue<>();
//        print = maze.getBreadthFirstTraversal("1","121");
//
//        while(!print.isEmpty())
//            System.out.println(print.dequeue());




    }

}
