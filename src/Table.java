import java.util.ArrayList;

public class Table {

     public class Column {
          String source;
          String relation;
          String target;

          public Column(String source, String relation, String target) {
               this.source   = source;
               this.relation = relation;
               this.target   = target;
          }
     }

     public String entity;
     public ArrayList<Column> columns;

     public Table(String entity) {
          if (entity.isEmpty())
               this.entity = "' '";
          else
               this.entity = entity;
          this.columns = new ArrayList<>();
     }

     public void addColumn(String source, String relation, String target) {
          Column c = new Column(source, relation, target);
          columns.add(c);
     }

     @Override public String toString() {
          String tableHTML = "";
          tableHTML += "    <table align='center' >\n";
          tableHTML += "        <tr>\n";
          tableHTML += "            <th colspan='3' align='center'> " + this.entity + "</th>\n";
          tableHTML += "        </tr>\n";
          tableHTML += "        <tr>\n";
          tableHTML += "            <td align='center'> Source   </td>\n";
          tableHTML += "            <td align='center'> Relation </td>\n";
          tableHTML += "            <td align='center'> Target   </td>\n";
          tableHTML += "        </tr>\n";
          for (Column c: columns) {
               tableHTML += "        <tr>\n";
               tableHTML += "            <td align='center'> " + c.source   + " </td>\n";
               tableHTML += "            <td align='center'> " + c.relation + " </td>\n";
               tableHTML += "            <td align='center'> " + c.target   + " </td>\n";
               tableHTML += "        </tr>\n";
          }
          tableHTML += "    </table>\n";
          return tableHTML;
     }
}
