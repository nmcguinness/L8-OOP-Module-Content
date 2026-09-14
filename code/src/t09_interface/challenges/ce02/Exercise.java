package t09_interface.challenges.ce02;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Steps:
 *  - load contacts from 3 CSV files
 *  - de-duplicate using HashSet (equals/hashCode on normalised email)
 *  - build simple statistics
 *  - run contacts through a formatter pipeline
 *  - write a single HTML file with:
 *      * table 1: statistics
 *      * table 2: formatted contacts
 */
public class Exercise {

    public static void run() {

        // 1. Load contacts from CSV
        System.out.println("Put your data file in: " + System.getProperty("user.dir"));
        String filePath = "data/ce02/";

        List<Contact> allContacts = new ArrayList<Contact>();
        allContacts.addAll(ContactUtility.readContactsCsv(filePath + "contacts_hr.csv"));
        allContacts.addAll(ContactUtility.readContactsCsv(filePath + "contacts_it.csv"));
        allContacts.addAll(ContactUtility.readContactsCsv(filePath + "contacts_sales.csv"));

        int rawCount = allContacts.size();
        System.out.println("Raw contacts loaded: " + rawCount);

        // 2. De-duplicate using a HashSet
        Set<Contact> uniqueSet = new HashSet<Contact>(allContacts);
        List<Contact> uniqueContacts = new ArrayList<Contact>(uniqueSet);

        int uniqueCount = uniqueContacts.size();
        int duplicateCount = rawCount - uniqueCount;

        System.out.println("Unique contacts: " + uniqueCount);
        System.out.println("Duplicates removed: " + duplicateCount);

        // 3. Simple stats
        int followUpCount = 0;
        int totalTurnoverK = 0;

        for (Contact c : uniqueContacts) {
            if (c.isFollowUp()) {
                followUpCount++;
            }

            totalTurnoverK += c.getTurnoverThousands();
        }

        // 4. Build formatter pipeline
        List<IFormatter> formatters = new ArrayList<IFormatter>();
        formatters.add(new TrimAndNormaliseFormatter());
        formatters.add(new InitialsAvatarFormatter());
        formatters.add(new StrongSurnameFormatter());
        formatters.add(new EmailHtmlFormatter());

        ContactFormatterPipeline pipeline = new ContactFormatterPipeline(formatters);

        // 5. Apply your chosen formatters
        List<Contact> formattedContacts = new ArrayList<Contact>(uniqueContacts.size());
        for (Contact c : uniqueContacts) {
            formattedContacts.add(pipeline.apply(c));
        }

        // 6. Sort contacts for nicer output (by department, then name)
        formattedContacts.sort(
            Comparator.comparing(Contact::getDepartment, String.CASE_INSENSITIVE_ORDER)
                      .thenComparing(Contact::getName, String.CASE_INSENSITIVE_ORDER)
        );

        // 7. Build simple HTML
        String html = buildHtml(
            formattedContacts,
            rawCount,
            uniqueCount,
            duplicateCount,
            followUpCount,
            totalTurnoverK
        );

        // 8. Write HTML to file
        String strPath = filePath + "contacts_directory.html";
        ContactUtility.writeHtml(strPath, html);
        System.out.println("Wrote HTML to: " + strPath);
    }

    // -------------------------------------------------------------------------
    // HTML building helpers (simple: headings + two tables, CSS)
    // -------------------------------------------------------------------------

    private static String buildHtml(
        List<Contact> contacts,
        int rawCount,
        int uniqueCount,
        int duplicateCount,
        int followUpCount,
        int totalTurnoverK) {

        StringBuilder sb = new StringBuilder(16384);

        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("<meta charset=\"UTF-8\" />\n");
        sb.append("<title>Unified Contact Directory</title>\n");

        // Basic CSS with table styling and alternate row colour
        sb.append("<style>\n");
        sb.append("body { font-family: Arial, sans-serif; }\n");
        sb.append("table { border-collapse: collapse; width: 100%; margin-bottom: 1.5em; }\n");
        sb.append("th, td { border: 1px solid #ccc; padding: 4px 8px; text-align: left; }\n");
        sb.append("th { background-color: #f2f2f2; }\n");
        sb.append("tr:nth-child(even) { background-color: #f9f9f9; }\n");
        sb.append(".avatar { display:inline-block; width:2em; height:2em; border-radius:50%; ");
        sb.append("text-align:center; font-weight:bold; border:1px solid #ccc; ");
        sb.append("margin-right:0.5em; font-size:0.8em; line-height:2em; }\n");
        sb.append("</style>\n");

        sb.append("</head>\n");
        sb.append("<body>\n");

        sb.append("<h1>Unified Contact Directory</h1>\n");

        // Table 1: statistics
        sb.append("<h2>Statistics</h2>\n");
        sb.append("<table border=\"1\">\n");
        sb.append("<tr><th>Statistic</th><th>Value</th></tr>\n");

        sb.append("<tr><td>Raw contacts loaded</td><td>")
          .append(rawCount).append("</td></tr>\n");

        sb.append("<tr><td>Unique contacts</td><td>")
          .append(uniqueCount).append("</td></tr>\n");

        sb.append("<tr><td>Duplicates removed</td><td>")
          .append(duplicateCount).append("</td></tr>\n");

        sb.append("<tr><td>Contacts interested in follow-up</td><td>")
          .append(followUpCount).append("</td></tr>\n");

        sb.append("<tr><td>Total turnover (sum, thousands of EUR)</td><td>")
          .append(totalTurnoverK).append("</td></tr>\n");

        sb.append("</table>\n");

        // Table 2: contacts
        sb.append("<h2>Contacts</h2>\n");
        sb.append("<table border=\"1\">\n");
        sb.append("<tr>")
          .append("<th>ID</th>")
          .append("<th>Name</th>")
          .append("<th>Email</th>")
          .append("<th>Phone</th>")
          .append("<th>Department</th>")
          .append("<th>Country</th>")
          .append("<th>Company</th>")
          .append("<th>Follow-up</th>")
          .append("<th>Turnover (k EUR)</th>")
          .append("</tr>\n");

        for (Contact c : contacts) {
            sb.append("<tr>");
            sb.append("<td>").append(escape(c.getId())).append("</td>");
            // Name and email are already formatted as HTML by the pipeline
            sb.append("<td>").append(c.getName()).append("</td>");
            sb.append("<td>").append(c.getEmail()).append("</td>");
            sb.append("<td>").append(renderPhone(c.getPhone())).append("</td>");
            sb.append("<td>").append(escape(c.getDepartment())).append("</td>");
            sb.append("<td>").append(escape(c.getCountry())).append("</td>");
            sb.append("<td>").append(escape(c.getCompany())).append("</td>");
            sb.append("<td>").append(c.isFollowUp() ? "Yes" : "No").append("</td>");
            sb.append("<td>").append(Integer.toString(c.getTurnoverThousands())).append("</td>");
            sb.append("</tr>\n");
        }

        sb.append("</table>\n");

        sb.append("</body>\n</html>\n");
        return sb.toString();
    }

    private static String renderPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return "";
        }

        String trimmed = phone.trim();
        String digits = trimmed.replace(" ", "").replace("-", "");

        String telHref = "tel:" + digits;
        String waHref = "https://wa.me/" + digits.replace("+", "");

        // no CSS classes, just plain links
        return "<a href=\"" + telHref + "\">" + escape(trimmed) + "</a>" +
               " (" +
               "<a href=\"" + waHref + "\">WhatsApp</a>" +
               ")";
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;");
    }
}
