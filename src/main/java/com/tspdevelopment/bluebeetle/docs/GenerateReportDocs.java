package com.tspdevelopment.bluebeetle.docs;

import java.util.List;
import com.tspdevelopment.bluebeetle.data.model.Student;
import com.tspdevelopment.bluebeetle.data.model.PointType;
import com.tspdevelopment.bluebeetle.helpers.TimeHelper;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateReportDocs {

    final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static GenerateReportDocs getInstance() {
        return GeneratePDFHolder.INSTANCE;
    }

    public String generateTestHTML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<style>");
        sb.append("table {");
        sb.append("  font-family: arial, sans-serif;");
        sb.append("  border-collapse: collapse;");
        sb.append("  width: 100%;");
        sb.append("}");

        sb.append("td, th {");
        sb.append("  border: 1px solid #dddddd;");
        sb.append("  text-align: left;");
        sb.append("  padding: 8px;");
        sb.append("}");

        sb.append("tr:nth-child(even) {");
        sb.append("  background-color: #dddddd;");
        sb.append("}");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");

        sb.append("<h2>Test Page</h2>");

        sb.append("Hello World!");

        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }

    public String generateCheckoutHTML(List<Student> students, String group) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<style>");
        sb.append("table {");
        sb.append("  font-family: arial, sans-serif;");
        sb.append("  border-collapse: collapse;");
        sb.append("  width: 100%;");
        sb.append("}");

        sb.append("td, th {");
        sb.append("  border: 1px solid #dddddd;");
        sb.append("  text-align: left;");
        sb.append("  padding: 8px;");
        sb.append("}");

        sb.append("tr:nth-child(even) {");
        sb.append("  background-color: #dddddd;");
        sb.append("}");

        sb.append("div.header {");
        sb.append("    position: absolute;");
        sb.append("    left: auto;");
        sb.append("    width: 100px;");
        sb.append("    height: 120px;");
        sb.append("  } ");

        sb.append("  div.headerDate {");
        sb.append("    position: absolute;");
        sb.append("    right: 0px;");
        sb.append("    width: 200px;");
        sb.append("    height: 120px;");
        sb.append("  } ");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");

        sb.append("<div class=\"header\"><h2>").append(group).append("</h2></div>");
        sb.append("<div class=\"headerDate\"><h2>").append(TimeHelper.getInstance().getNextEventDate().format(CUSTOM_FORMATTER)).append("</h2></div>");

        sb.append("<table>");
        sb.append("  <tr>");
        sb.append("    <th>Student</th>");
        sb.append("    <th>Point Spent</th>");
        sb.append("  </tr>");
        for (Student s : students) {
            sb.append("  <tr>");
            sb.append("    <td>").append(s.getName()).append("</td>");
            sb.append("    <td></td>");
            sb.append("  </tr>");
        }
        sb.append("</table>");

        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }
    
    public String generateCheckoutJSON(List<Student> students, String group) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"report\": [");
        boolean first = true;
        for (Student s : students) {
            if(!first) {
                sb.append(",{");
                sb.append("\"student\": \"").append(s.getName()).append("\"");
                sb.append("}");
            } else {
                sb.append("{");
                sb.append("\"student\": \"").append(s.getName()).append("\"");
                sb.append("}");
                first = false;
            }
        }
        sb.append("],");
        sb.append("\"reportDate\": ").append("\"").append(TimeHelper.getInstance().getNextEventDate().format(CUSTOM_FORMATTER)).append("\",");
        sb.append("\"group\": ").append("\"").append(group).append("\"");
        sb.append("}");

        return sb.toString();
    }

    public String generateCheckinHTML(List<Student> students, List<PointType> pointTypes, String group) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<style>");
        sb.append("table {");
        sb.append("  font-family: arial, sans-serif;");
        sb.append("  border-collapse: collapse;");
        sb.append("  width: 100%;");
        sb.append("}");

        sb.append("td, th {");
        sb.append("  border: 1px solid #dddddd;");
        sb.append("  text-align: left;");
        sb.append("  padding: 8px;");
        sb.append("}");

        sb.append("tr:nth-child(even) {");
        sb.append("  background-color: #dddddd;");
        sb.append("}");

        sb.append("div.header {");
        sb.append("    position: absolute;");
        sb.append("    left: auto;");
        sb.append("    width: 100px;");
        sb.append("    height: 120px;");
        sb.append("  } ");

        sb.append("  div.headerDate {");
        sb.append("    position: absolute;");
        sb.append("    right: 0px;");
        sb.append("    width: 200px;");
        sb.append("    height: 120px;");
        sb.append("  } ");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");

        sb.append("<div class=\"header\"><h2>").append(group).append("</h2></div>");
        sb.append("<div class=\"headerDate\"><h2>").append(TimeHelper.getInstance().getNextEventDate().format(CUSTOM_FORMATTER)).append("</h2></div>");

        sb.append("<table>");
        sb.append("  <tr>");
        sb.append("    <th>Student</th>");
        for (PointType pc : pointTypes) {
            if(pc.isEnabled()) {
                sb.append("    <th>").append(pc.getPointCategory().getCategory()).append("</th>");
            }
        }
        sb.append("  </tr>");
        for (Student s : students) {
            sb.append("  <tr>");
            sb.append("    <td>").append(s.getName()).append("</td>");

            for (PointType pc : pointTypes) {
                if(pc.isEnabled()) {
                    sb.append("    <td></td>");
                }
            }

            sb.append("  </tr>");
        }
        sb.append("</table>");

        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }
    
    public String generateCheckinJSON(List<Student> students, List<PointType> pointTypes, String group) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"report\": [");
        boolean first = true;
        for (Student s : students) {
            if(!first) {
                sb.append(",{");
                sb.append("\"student\": \"").append(s.getName()).append("\"");
                boolean sf = true;
                for (PointType pc : pointTypes) {
                    if(pc.isEnabled()) {
                        if(sf){
                            sb.append("\"").append(pc.getPointCategory().getCategory()).append("\": \"\"");
                            sf = false;
                        } else {
                            sb.append(",\"").append(pc.getPointCategory().getCategory()).append("\": \"\"");
                        }
                    }
                }
                sb.append("}");
            } else {
                sb.append("{");
                sb.append("\"student\": \"").append(s.getName()).append("\"");
                boolean sf = true;
                for (PointType pc : pointTypes) {
                    if(pc.isEnabled()) {
                        if(sf){
                            sb.append("\"").append(pc.getPointCategory().getCategory()).append("\": \"\"");
                            sf = false;
                        } else {
                            sb.append(",\"").append(pc.getPointCategory().getCategory()).append("\": \"\"");
                        }
                    }
                }
                sb.append("}");
                first = false;
            }
        }
        sb.append("],");
        sb.append("\"reportDate\": ").append("\"").append(TimeHelper.getInstance().getNextEventDate().format(CUSTOM_FORMATTER)).append("\",");
        sb.append("\"group\": ").append("\"").append(group).append("\"");
        sb.append("}");

        return sb.toString();
    }

    private static class GeneratePDFHolder {
        private static final GenerateReportDocs INSTANCE = new GenerateReportDocs();
    }
}
