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

     private boolean includePredicates;
     private boolean strictSearch;

     public Parser() {
          Nodes = new ArrayList<>();
          Edges = new ArrayList<>();
          Objectives = new ArrayList<>();
     }

     public void setOption(String option) {
          if (option.equals("include-predicates"))
              includePredicates = true;

          else if (option.equals("strict-search"))
              strictSearch = true;
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

     private ArrayList<Node> depurateNodes() {
         ArrayList<Node> filteredNodes = new ArrayList<>();

         boolean comparison = false;

         for (Node node: Nodes) {
             for (String objective : Objectives) {

                 if (strictSearch)
                     comparison = node.label.equals(objective);
                 else
                     comparison = (node.label.indexOf(objective) > -1 ||
                                   objective.indexOf(node.label) > -1);

                 if (comparison) {
                     filteredNodes.add(node);
                     break;
                 }
             }
         }

         return filteredNodes;
     }

     private boolean isNodeContained(Node node, ArrayList<Node> list) {
         for (Node n : list)
             if (node.ID == n.ID)
                 return true;

         return false;
     }

     private ArrayList<Edge> depurateEdges(ArrayList<Node> filteredNodes) {
         ArrayList<Edge> filteredEdges = new ArrayList<>();

         /* List of nodes that do not match the objetives but are connected by
            an edge that matches with an objective or is adjecent to a filtered
            node. */
         ArrayList<Node> adjecentNodes = new ArrayList<>();

         Node source = null;
         Node target = null;

         boolean comparison = false;

         boolean includeEdge = false;

         for (Edge edge : Edges) {

             includeEdge = false;

             source = edge.getSource();
             target = edge.getTarget();

             if (includePredicates) {
                 for (String objective : Objectives) {
                     if (strictSearch)
                         comparison = edge.label.equals(objective);
                     else
                         comparison = (edge.label.indexOf(objective) > -1 ||
                                       objective.indexOf(edge.label) > -1);

                     if (comparison) {
                         includeEdge = true;
                         break;
                     }
                 }
             }

             else {
                 if (isNodeContained(source, filteredNodes) ||
                     isNodeContained(target, filteredNodes)){
                     includeEdge = true;
                 }
             }

             if (includeEdge) {
                 filteredEdges.add(edge);

                 if (!(isNodeContained(source, filteredNodes)) &&
                     !(isNodeContained(source, adjecentNodes)))
                     adjecentNodes.add(source);

                 if (!(isNodeContained(target, filteredNodes)) &&
                     !(isNodeContained(target, adjecentNodes)))
                     adjecentNodes.add(target);
             }
         }

         filteredNodes.addAll(adjecentNodes);

         return filteredEdges;
     }

     public void depurate() {
         ArrayList<Node> filteredNodes = new ArrayList<>();
         ArrayList<Edge> filteredEdges = new ArrayList<>();

         filteredNodes = depurateNodes();

         filteredEdges = depurateEdges(filteredNodes);

         Nodes = filteredNodes;
         Edges = filteredEdges;
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
