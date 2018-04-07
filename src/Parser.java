import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.io.IOException;

public class Parser {
     private ArrayList<String> Nodes;
     private ArrayList<Object[]> Edges;

     private String file;

     public Parser() {
          Nodes = new ArrayList<>();
          Edges = new ArrayList<>();
     }

     private String filter(String xml) {
          int x = xml.indexOf("text#");
          int y = xml.indexOf("> .");
          String label = xml.substring(x+5,y);
          label = label.replaceAll("\\+"," ");

          return label;
     }

     public void load(BufferedReader in) throws IOException {
          String line = "";
          Boolean verbExists = false;
          int subjectID = 0;
          int objectID = 0;
          String verb = "";

          try {
               do {
                    line = in.readLine();

                    if (line == null)
                         break;

                    if (line.indexOf("extraction#subject") > -1 || line.indexOf("extraction#object") > -1) {

                         String label = filter(line);

                         int z = Nodes.indexOf(label);


                         if (z < 0) {
                              if (line.indexOf("extraction#subject") > -1)
                                   subjectID = Nodes.size();
                              else
                                   objectID = Nodes.size();

                              Nodes.add(label);
                         }
                         else {
                              if (line.indexOf("extraction#subject") > -1)
                                   subjectID = z;
                              else
                                   objectID = z;
                         }

                         if (line.indexOf("extraction#object") > -1) {
                              Object[] edge;
                              if (verb.isEmpty())
                                   edge = new Object[]{"", subjectID, objectID} ;
                              else
                                   edge = new Object[]{verb, subjectID, objectID};

                              Edges.add(edge);
                              verb = "";
                         }
                    }

                    else if (line.indexOf("extraction#predicate") > -1)
                         verb = filter(line);

               } while (line != null);
          } catch (IOException ioe) {
               throw new IOException("An error ocurred while loading the file");
          }
     }

     @Override public String toString() {
          file = "<gexf version=\"1.3\">\n";
          file += "     <meta lastmodifieddate=\""+ new SimpleDateFormat("yyyy-MM-dd+HH:mm").format(Calendar.getInstance().getTime()) +"\">\n";
          file += "          <creator>Carrasco-Ruiz M.</creator>\n";
          file += "     </meta>\n";
          file += "     <graph defaultedgetype=\"directed\" type=\"static\">\n";
          file += "          <nodes count=\""+ Nodes.size() +"\">\n";

          int x = 0;
          for (String label : Nodes) {
               file += "               <node id=\""+ (x++) +"\" label=\""+ label +"\"/>\n";
          }


          file += "          </nodes>\n";
          file += "          <edges count=\""+ Edges.size() +"\">\n";

          x = 0;
          for (Object[] edge : Edges) {
               file += "               <edge id=\""+ (x++) +"\" label=\""+ edge[0] +"\" source=\""+ edge[1] +"\" target=\""+ edge[2] +"\" />\n";
          }

          file += "          </edges>\n";
          file += "     </graph>\n";
          file += "</gexf>";

          return file;
     }
}
