import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.ArrayIndexOutOfBoundsException;

public class Graphene2Gephi {

     static private Parser p;

     private static void read(String grapheneFile) throws IOException {
          try {
               FileInputStream fileIn = new FileInputStream(grapheneFile);
               InputStreamReader isIn = new InputStreamReader(fileIn);
               BufferedReader in = new BufferedReader(isIn);

               p = new Parser();
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
               throw new IOException("An error occurred while trying to read the file.");
          }
     }

     private static void save(String gephiFile) throws IOException {
          try {
               FileOutputStream fileOut = new FileOutputStream(gephiFile);
               OutputStreamWriter osOut = new OutputStreamWriter(fileOut);
               BufferedWriter out = new BufferedWriter(osOut);
               out.write(p.toString());
               out.close();
          } catch (IOException ioe) {
               throw new IOException("An error occurred while reading the file.");
          }

     }

     public static void main(String[] args) {
          try{
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
               System.out.println("Usage: \n java -jar graphene2gephi.jar inputFile.nt outputFile.gexf [-d|-D objectiveFile]");
          }
     }
}
