import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.ArrayIndexOutOfBoundsException;

public class Graphene2Gephi {

     static private boolean   htmlOutput;
     static private boolean   hasObjectives;
     static private Parser    parser;
     static private Container container;

     private static void read(String grapheneFile) throws IOException {
          try {
               FileInputStream fileIn = new FileInputStream(grapheneFile);
               InputStreamReader isIn = new InputStreamReader(fileIn);
               BufferedReader in = new BufferedReader(isIn);

               parser = new Parser();
               parser.load(in);
               in.close();
          } catch (IOException ioe) {
               throw new IOException("An error occurred while reading the file: " + ioe);
          }
     }

     private static void readObjectives(String objFile) throws IOException {
          try {
               FileInputStream fileIn = new FileInputStream(objFile);
               InputStreamReader isIn = new InputStreamReader(fileIn);
               BufferedReader in = new BufferedReader(isIn);

               parser.loadObjectives(in);
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
               if (htmlOutput) {
                    container = new Container();
                    container.load(parser.getEdges());
                    container.sort();
                    out.write(container.toString());
               }
               else
                    out.write(parser.toString());
               out.close();
          } catch (IOException ioe) {
               throw new IOException("An error occurred while writing the file: " + ioe);
          }
     }

     private static void applyOptions(String[] options) throws IOException {
         String option = "";

         for (int i = 2; i < options.length; i++) {
             option = options[i];

             if (option.equals("--search-entities")) {
                 String entitiesFile = options[++i];
                 hasObjectives = true;

                 try {
                     readObjectives(entitiesFile);
                 } catch (IOException ioe) {
                     throw new IOException(ioe);
                 }
             }

             else if (option.equals("--include-predicates"))
                 parser.setOption("include-predicates");

             else if (option.equals("--strict-search")){
                 parser.setOption("strict-search");
             }

             else if (option.equals("--html-output"))
                 htmlOutput = true;

             else {
                 System.out.println("Unknown option: " + option);
                 System.exit(1);
             }
         }

     }

     private static void usage() {
          String s = "Usage: \n "                                                               +
                     " java -jar graphene2gephi.jar <input-file> <output-file> [options...] \n" +
                     "\n"                                                                       +
                     "Parse graphene files into gephi files. \n"                                +
                     "\n"                                                                       +
                     "Options: \n"                                                              +
                     " --search-entities <file> Filter the results from the input so the "      +
                                               "output only has subjects and objects that "     +
                                               "contain the searched entities. \n"              +
                     " --include-predicates     Include predicates in the search. \n"           +
                     " --strict-search          Nodes will have to match exactly the "          +
                                               "entities to appear in the output. \n"           +
                     " --html-output            Output has an html table format. \n"            +
                     " --help                   Display this help.";
          System.out.println(s);
     }

     public static void main(String[] args) {

          if (args.length < 2 || args[0].equals("--help")){
              usage();
              return;
          }

          String inputFile  = args[0];
          String outputFile = args[1];

          try {

               read(inputFile);

               applyOptions(args);

               if (hasObjectives)
                   parser.depurate();

               save(outputFile);

          } catch (IOException ioe) {
               System.out.println(ioe);
          } catch (ArrayIndexOutOfBoundsException aio) {
               usage();
          }
     }
}
