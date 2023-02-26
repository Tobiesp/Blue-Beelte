package com.tspdevelopment.kidsscore.pdf;

import java.util.List;
import com.tspdevelopment.kidsscore.data.model.Student;
import com.tspdevelopment.kidsscore.data.model.PointTable;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateHTML {

    final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static GenerateHTML getInstance() {
        return GeneratePDFHolder.INSTANCE;
    }

    public String generateTestHTML() {
        StringBuffer sb = new StringBuffer();
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
        StringBuffer sb = new StringBuffer();
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

        sb.append("<div class=\"header\"><h2>" + group + "</h2></div>");
        sb.append("<div class=\"headerDate\"><h2>" + getNextWendesday().format(CUSTOM_FORMATTER) + "</h2></div>");

        sb.append("<table>");
        sb.append("  <tr>");
        sb.append("    <th>Student</th>");
        sb.append("    <th>Point Spent</th>");
        sb.append("  </tr>");
        for (Student s : students) {
            sb.append("  <tr>");
            sb.append("    <td>" + s.getName() + "</td>");
            sb.append("    <td></td>");
            sb.append("  </tr>");
        }
        sb.append("</table>");

        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }

    public String generateCheckinHTML(List<Student> students, List<PointTable> pointTypes, String group) {
        StringBuffer sb = new StringBuffer();
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

        sb.append("<div class=\"header\"><h2>" + group + "</h2></div>");
        sb.append("<div class=\"headerDate\"><h2>" + getNextWendesday().format(CUSTOM_FORMATTER) + "</h2></div>");

        sb.append("<table>");
        sb.append("  <tr>");
        sb.append("    <th>Student</th>");
        for (PointTable pc : pointTypes) {
            sb.append("    <th>" + pc.getPointCategory().getCategory() + "</th>");
        }
        sb.append("  </tr>");
        for (Student s : students) {
            sb.append("  <tr>");
            sb.append("    <td>" + s.getName() + "</td>");

            for (PointTable pc : pointTypes) {
                sb.append("    <td></td>");
            }

            sb.append("  </tr>");
        }
        sb.append("</table>");

        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }

    private LocalDateTime getNextWendesday() {
        LocalDateTime date = LocalDateTime.now();

        if((date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) && (date.getHour() < 17)) {
            return date;
        }
        do {
            date.plusDays(1);
        } while(!date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY));
        return date;
    }

    private static class GeneratePDFHolder {
        private static final GenerateHTML INSTANCE = new GenerateHTML();
    }
}
