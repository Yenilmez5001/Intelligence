/**
 * Osman Selim Yuksel
 * Program that reads a file and creates an AVL tree with the given inputs
 * While reading, it also does the operations that are asked in the file
 */
import java.io.*;
import java.util.Scanner;
public class Main {
    static String fileName;
    static String testFileLocation;

    public static void main(String[] args) throws IOException {
        String outputName = "output2.txt";
        // Create an output file
        fileName = args[0];
        outputName = args[1];

        try{
            File outputFile = new File(outputName);
            outputFile.createNewFile();

        }
        catch (Exception e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        readFile(new FileWriter(outputName));
    }

    public static void readFile(FileWriter w) {
        AVLTree tree = new AVLTree();
        try{
            FileInputStream fis = new FileInputStream(fileName);
            Scanner sc = new Scanner(fis);
            String firstLine = sc.nextLine();
            String firstName = firstLine.split(" ")[0];
            double firstGMS = Double.parseDouble(firstLine.split(" ")[1]);
            tree.root = tree.insert(tree.root, firstGMS, firstName);

            while(sc.hasNextLine()){
                String line = sc.nextLine();
                if (line.startsWith("MEMBER_IN")){
                    double gms = Double.parseDouble(line.split(" ")[2]);
                    String name = line.split(" ")[1];
                    printRoute(tree.root, gms, name, w);
                    tree.root = tree.insert(tree.root, gms, name);

                }
                else if (line.startsWith("MEMBER_OUT")){
                    double gms = Double.parseDouble(line.split(" ")[2]);
                    String name = line.split(" ")[1];
                    tree.root = tree.delete(tree.root, gms, name, 1, w);

                }


                // TARGET IN ALGORITMASINI SAATLERCE BOS BOS BAKTIKTAN SONRA HALA KUSURSUZ GORMEYE DEVAM ETSEM DE SABAH ALLAH'IN IZNIYLE HATAYI BULACAGIMA INANIYORUM
                // BISMILLAHIRRAHMANIRRAHIM
                // && operatorunun soldaki ifade yanlissa sagdakini kontrol etmemesini gormek icin saatlerimi harcadim, ve gormemi CEK sagladi

                else if (line.startsWith("INTEL_TARGET")){
                    double gms1 = Double.parseDouble(line.split(" ")[2]);
                    double gms2 = Double.parseDouble(line.split(" ")[4]);

                    AVLTree.Node ancestor = tree.commonAncestor(tree.root, gms1, gms2);

                    String a = String.format("%.3f", ancestor.GMS).split(",")[0];
                    String b = String.format("%.3f", ancestor.GMS).split(",")[1];
                    while(b.length() < 3){
                        b += "0";
                    }

                    w.write("Target Analysis Result: " + ancestor.name + " " + a + "." + b + "\n");
                    w.flush();

                }

                else if (line.startsWith("INTEL_RANK")){
                    double gms = Double.parseDouble(line.split(" ")[2]);
                    int rank = tree.getRank(tree.root, gms);
                    w.write("Rank Analysis Result: ");
                    w.flush();
                    tree.monitoreRank(tree.root, rank, 0, w);
                    w.write("\n");
                    w.flush();
                }


                else if (line.startsWith("INTEL_DIVIDE")){
                    int[] counter = {0};
                    tree.maxIndependent(tree.root, counter);

                    w.write("Division Analysis Result: " + counter[0] + "\n");
                    w.flush();

                }
            }

        }
        catch (Exception e) {
            System.out.println("Input file " + fileName + " not found");
            System.exit(1);
        }
    }



    // A utility function to print the route from root to the given node, which means printing the path from root to the node
    public static void printRoute(AVLTree.Node root, double gms, String name, FileWriter output) throws IOException {
        try {
            if (root == null) {
                return;
            }
            if (root.GMS == gms) {
                return;
            }
            if (root.GMS > gms) {
                output.write(root.name + " welcomed " + name + "\n");
                output.flush();
                printRoute(root.left, gms, name, output);
            }
            if (root.GMS < gms) {
                output.write(root.name + " welcomed " + name + "\n");
                output.flush();
                printRoute(root.right, gms, name, output);
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    // Created for testing only
    public static void test(String outputFileLocation, String testFileLocation) throws FileNotFoundException{
        boolean areTheySame = true;
        File file1 = new File(outputFileLocation);
        File file2 = new File(testFileLocation);
        Scanner scanner1 = new Scanner(file1);
        Scanner scanner2 = new Scanner(file2);
        String line1;
        String line2;
        while(scanner1.hasNextLine() && scanner2.hasNextLine()){
            line1 = scanner1.nextLine();
            line2 = scanner2.nextLine();
            System.out.print(line1);
            if(line1.equals(line2) || line1.equals(line2 + " ")){
            }else{
                areTheySame = false;
                System.out.print("--------" + line2);
            }
            System.out.print("\n");
        }
        if(areTheySame){
            System.out.println("Test case works");
        }else{
            System.out.println("Test case doesn't work");
        }
    }
}

