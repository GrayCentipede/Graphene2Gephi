import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;

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
               save(args[1]);
          } catch (IOException ioe) {
               System.out.println(ioe);
          }
     }
}
