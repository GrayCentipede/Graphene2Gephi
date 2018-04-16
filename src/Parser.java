import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;

public class Parser {
     private ArrayList<String[]> Nodes;
     private ArrayList<Object[]> Edges;

     private ArrayList<String> Objectives;

     private String file;

     private int mode;

     public Parser() {
          mode = 0;
          Nodes = new ArrayList<>();
          Edges = new ArrayList<>();
          Objectives = new ArrayList<>();
     }

     public void setMode(int m) {
          mode = m;
     }

     private String filter(String xml) {
          int x = xml.indexOf("text#");
          int y = xml.indexOf("> .");
          String label = xml.substring(x+5,y);
          label = label.replaceAll("\\+"," ");

          return label;
     }

     public void loadObjectives(BufferedReader in) throws IOException {
          String line = "";
          try {
               do {
                    line = in.readLine();

                    if (line != null)
                         Objectives.add(line);

               } while (line != null);
          } catch (IOException ioe) {
               throw new IOException("An error ocurred while loading the file");
          }
     }

     public void load(BufferedReader in) throws IOException {
          String line = "";
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
                         String copy = label.toLowerCase();

                         int z = -1;
                         for (String[] s : Nodes) {
                              if(s[1].toLowerCase().equals(copy)){
                                   z = Integer.parseInt(s[0]);
                                   break;
                              }
                         }


                         if (z < 0) {
                              if (line.indexOf("extraction#subject") > -1)
                                   subjectID = Nodes.size();
                              else
                                   objectID = Nodes.size();
                              String id = Integer.toString(Nodes.size());
                              String[] node = new String[]{id, label};
                              Nodes.add(node);
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

     public void depurate() {
          ArrayList<Integer> n = new ArrayList<>();
          ArrayList<String> keep = new ArrayList<>();
          Iterator<String[]> j = Nodes.iterator();
          Iterator<Object[]> i = Edges.iterator();

          for (String[] s : Nodes) {
               for (String o : Objectives) {
                    String k = s[1];
                    if (k.indexOf(o) > -1) {
                         n.add(Integer.parseInt(s[0]));
                         break;
                    }
               }
          }

          while (i.hasNext()) {
               Object[] e = i.next();
               Boolean found = false;
               if (!(n.contains(e[1]) || n.contains(e[2]))) {
                    if (mode == 2) {
                         for (String o : Objectives) {
                              found = false;
                              String aux = (String) e[0];
                              if (aux.indexOf(o) > -1)
                                   found = true;
                         }
                         if (!found) {
                              i.remove();
                              continue;
                         }
                    }
                    else {
                         i.remove();
                         continue;
                    }
               }
               String[] t = Nodes.get((int) e[1]);
               keep.add(t[0]);
               t = Nodes.get((int) e[2]);
               keep.add(t[0]);
          }

          while (j.hasNext()) {
               String[] s = j.next();
               if (!(keep.contains(s[0])))
                    j.remove();
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
          for (String[] node : Nodes) {
               file += "               <node id=\""+ node[0] +"\" label=\""+ node[1] +"\"/>\n";
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
