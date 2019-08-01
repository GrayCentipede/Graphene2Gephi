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
               String source   = e.getSource().getLabel();
               String relation = e.getRelation();
               String target   = e.getTarget().getLabel();

               Table table = tables.get(source);

               if (table == null) {
                    table = new Table(source);
                    table.addColumn(source, relation, target);

                    tables.put(source, table);
               }
               else
                    table.addColumn(source, relation, target);

               table = tables.get(target);

               if (table == null) {
                    table = new Table(target);
                    table.addColumn(source, relation, target);

                    tables.put(target, table);
               }
               else
                    table.addColumn(source ,relation, target);
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
          String html = "";
          html += "<html>\n";
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
