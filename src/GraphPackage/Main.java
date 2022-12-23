package GraphPackage;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        Test newTest = new Test();
        String path = "C:\\Users\\vedat\\OneDrive\\Masaüstü\\MazeEscapeUsingGraphs\\maze1.txt";
        newTest.solve(path);

    }

}
