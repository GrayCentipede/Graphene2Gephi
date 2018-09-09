import java.util.ArrayList;

public class Table {

     public class Column {
          String relation;
          String target;

          public Column(String relation, String target) {
               this.relation = relation;
               this.target = target;
          }
     }

     public String source;
     public ArrayList<Column> columns;

     public Table(String source) {
          if (source.isEmpty())
               this.source = "' '";
          else
               this.source = source;
          this.columns = new ArrayList<>();
     }

     public void addColumn(String relation, String target) {
          Column c = new Column(relation, target);
          columns.add(c);
     }

     @Override public String toString() {
          String tableHTML = "";
          tableHTML += "    <table align='center' >\n";
          tableHTML += "        <tr>\n";
          tableHTML += "            <th colspan='2' align='center'> " + this.source + "</th>\n";
          tableHTML += "        </tr>\n";
          tableHTML += "        <tr>\n";
          tableHTML += "            <td align='center'> Relation </td>\n";
          tableHTML += "            <td align='center'> Target </td>\n";
          tableHTML += "        </tr>\n";
          for (Column c: columns) {
               tableHTML += "        <tr>\n";
               tableHTML += "            <td align='center'> "+ c.relation +" </td>\n";
               tableHTML += "            <td align='center'> "+ c.target +" </td>\n";
               tableHTML += "        </tr>\n";
          }
          tableHTML += "    </table>\n";
          return tableHTML;
     }
}
