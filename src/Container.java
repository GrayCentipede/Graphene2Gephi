import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class Container {
     HashMap<String, Table> tables;

     public Container() {
          tables = new HashMap<>();
     }

     public void load(ArrayList<Parser.Edge> edges) {
          for (Parser.Edge e : edges) {
               String relation = e.getRelation();
               String target = e.getTarget().getLabel();
               String source = e.getSource().getLabel();
               Table t = tables.get(source);
               Table n = null;
               if (t == null) {
                    n = new Table(source);
                    n.addColumn(relation, target);
                    tables.put(source, n);
               }
               else
                    t.addColumn(relation, target);

               t = tables.get(target);
               if (t == null) {
                    n = new Table(target);
                    n.addColumn(relation, source);
                    tables.put(target, n);
               }
               else
                    t.addColumn(relation, source);
          }
     }

     public void sort() {
          for (Table t : tables.values())
               Collections.sort(t.columns, (a,b) -> {
                    String x = a.relation;
                    String y = b.relation;
                    return x.compareTo(y);
               });
     }

     @Override public String toString() {
          String html = "<html>\n";
          html += "    <head>\n";
          html += "        <title> ... </title>\n";
          html += "        <style>\n";
          html += "            table, th, td {\n";
          html += "                border: 1px solid black;\n";
          html += "                border-collapse: collapse;\n";
          html += "            }\n";
          html += "        </style>\n";
          html += "    </head>\n";
          html += "    <body>\n";

          for (Table t: tables.values()){
               html += "<br>";
               html += t.toString();
          }

          html += "    </body>\n";
          html += "</html>\n";
          return html;
     }
}
