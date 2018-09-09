import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;

public class Parser {

     public class Node {
          private int ID;
          private String label;

          private Node(int ID, String label) {
               this.ID = ID;
               this.label = label;
          }

          public String getLabel() {
               return this.label;
          }
     }

     public class Edge {
          private String label;
          private Node source;
          private Node target;

          private Edge(String label, Node source, Node target) {
               this.label = label;
               this.source = source;
               this.target = target;
          }

          public Node getSource() {
               return this.source;
          }

          public String getRelation() {
               return this.label;
          }

          public Node getTarget() {
               return this.target;
          }
     }

     private ArrayList<Node> Nodes;
     private ArrayList<Edge> Edges;

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
          Node subject = null;
          Node object = null;
          String verb = "";

          try {
               do {
                    Node temp = null;
                    line = in.readLine();

                    if (line == null)
                         break;

                    if (line.indexOf("extraction#subject") > -1 || line.indexOf("extraction#object") > -1) {

                         String label = filter(line);
                         String copy = label.toLowerCase();

                         for (Node node : Nodes) {
                              boolean nodeContains = (node.label.toLowerCase().indexOf(copy) > -1);
                              boolean nodeIsContained = (copy.indexOf(node.label.toLowerCase()) > -1);
                              if (nodeContains || nodeIsContained) {
                                   temp = node;
                                   break;
                              }
                         }


                         if (temp == null) {
                              int id = Nodes.size();

                              if (line.indexOf("extraction#subject") > -1) {
                                   subject = new Node(id, label);
                                   Nodes.add(subject);
                              }

                              else {
                                   object = new Node(id, label);
                                   Nodes.add(object);
                              }

                         }

                         else {

                              if (line.indexOf("extraction#subject") > -1)
                                   subject = temp;
                              else
                                   object = temp;
                         }

                         if (line.indexOf("extraction#object") > -1) {
                              Edge edge;
                              if (verb.isEmpty())
                                   edge = new Edge("", subject, object);
                              else
                                   edge = new Edge(verb, subject, object);

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
          ArrayList<Integer> keep = new ArrayList<>();
          Iterator<Node> j = Nodes.iterator();
          Iterator<Edge> i = Edges.iterator();

          for (Node node : Nodes) {
               for (String o : Objectives) {
                    if (node.label.indexOf(o) > -1) {
                         n.add(node.ID);
                         break;
                    }
               }
          }

          while (i.hasNext()) {
               Edge e = i.next();
               Boolean found = false;
               if (!(n.contains(e.source.ID) || n.contains(e.target.ID))) {
                    if (mode == 2) {
                         for (String o : Objectives) {
                              found = false;
                              String aux = (String) e.label;
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
               Node t = e.source;
               keep.add(t.ID);
               t = e.target;
               keep.add(t.ID);
          }

          while (j.hasNext()) {
               Node s = j.next();
               if (!(keep.contains(s.ID)))
                    j.remove();
          }
     }

     public ArrayList<Edge> getEdges() {
          return this.Edges;
     }

     @Override public String toString() {
          file = "<gexf version=\"1.3\">\n";
          file += "     <meta lastmodifieddate=\""+ new SimpleDateFormat("yyyy-MM-dd+HH:mm").format(Calendar.getInstance().getTime()) +"\">\n";
          file += "          <creator>Carrasco-Ruiz M.</creator>\n";
          file += "     </meta>\n";
          file += "     <graph defaultedgetype=\"directed\" type=\"static\">\n";
          file += "          <nodes count=\""+ Nodes.size() +"\">\n";

          int x = 0;
          for (Node node : Nodes)
               file += "               <node id=\""+ node.ID +"\" label=\""+ node.label +"\"/>\n";

          file += "          </nodes>\n";
          file += "          <edges count=\""+ Edges.size() +"\">\n";

          x = 0;
          for (Edge edge : Edges)
               file += "               <edge id=\""+ (x++) +"\" label=\""+ edge.label +"\" source=\""+ edge.source.ID +"\" target=\""+ edge.target.ID +"\" />\n";

          file += "          </edges>\n";
          file += "     </graph>\n";
          file += "</gexf>";

          return file;
     }
}
