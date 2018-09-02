import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.ArrayIndexOutOfBoundsException;

public class Graphene2Gephi {

     static private boolean flag;
     static private Parser p;
     static private Container c;

     private static void read(String grapheneFile) throws IOException {
          try {
               FileInputStream fileIn = new FileInputStream(grapheneFile);
               InputStreamReader isIn = new InputStreamReader(fileIn);
               BufferedReader in = new BufferedReader(isIn);

               p = new Parser();
               if (flag)
                    c = new Container();
               p.load(in);
               in.close();
          } catch (IOException ioe) {
               throw new IOException("An error occurred while reading the file.");
          }
     }

     private static void readObjectives(String objFile) throws IOException {
          try {
               FileInputStream fileIn = new FileInputStream(objFile);
               InputStreamReader isIn = new InputStreamReader(fileIn);
               BufferedReader in = new BufferedReader(isIn);

               p.loadObjectives(in);
               p.depurate();
               in.close();
          } catch (IOException ioe) {
               throw new IOException("An error occurred while trying to read the file: " + ioe);
          }
     }

     private static void save(String gephiFile) throws IOException {
          try {
               FileOutputStream fileOut = new FileOutputStream(gephiFile);
               OutputStreamWriter osOut = new OutputStreamWriter(fileOut);
               BufferedWriter out = new BufferedWriter(osOut);
               if (flag) {
                    c.load(p.getEdges());
                    c.sort();
                    out.write(c.toString());
               }
               else
                    out.write(p.toString());
               out.close();
          } catch (IOException ioe) {
               throw new IOException("An error occurred while writing the file: " + ioe);
          }
     }

     private static void usage() {
          String s = "Usage: \n " +
                         "java -jar graphene2gephi.jar inputFile.nt outputFile.gexf|outputFile.html [-d|-D objectivesFile]";
          System.out.println(s);
     }

     public static void main(String[] args) {
          try {
               if (args[1].indexOf(".html") > 0)
                    flag = true;

               else if (args[1].indexOf(".gexf") > 0)
                    flag = false;

               else {
                    System.err.println("Invalid output.");
                    usage();
                    System.exit(1);
               }

               read(args[0]);

               if (args.length > 2) {
                    if (args[2].equals("-d"))
                         p.setMode(1);
                    else if (args[2].equals("-D"))
                         p.setMode(2);
                    else{
                         System.err.println("Invalid flag.");
                         System.exit(1);
                    }
                    readObjectives(args[3]);
               }
               save(args[1]);
          } catch (IOException ioe) {
               System.out.println(ioe);
          } catch (ArrayIndexOutOfBoundsException aio) {
               usage();
          }
     }
}
