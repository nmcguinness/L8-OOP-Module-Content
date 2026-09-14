package t11_generics_1.challenges.ce03;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileHelper
{
    /// <summary>
    /// Reads a list of strings from a CSV file (first column only).
    /// </summary>
    public static List<String> readStringsFromCsv(Path csvPath, boolean hasHeader) throws IOException
    {
        if (csvPath == null)
            throw new IllegalArgumentException("csvPath is null.");

        if (!Files.exists(csvPath))
            throw new IOException("CSV file not found: " + csvPath);

        List<String> results = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(csvPath))
        {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null)
            {
                if (firstLine && hasHeader)
                {
                    firstLine = false;
                    continue;
                }
                firstLine = false;

                line = line.trim();
                if (line.isEmpty())
                    continue;

                // Your dataset is "token,token,token" (often all on one line),
                // so we must collect *every* token, not just parts[0].
                String[] tokens = line.split(",");

                for (String token : tokens)
                {
                    String value = token.trim();

                    if (!value.isEmpty())
                        results.add(value);
                }
            }
        }

        return results;
    }

    /// <summary>
    /// Reads Weapon objects from an XML file.
    /// Expected structure:
    /// &lt;weapons&gt;
    ///   &lt;weapon&gt;&lt;name&gt;...&lt;/name&gt;&lt;strength&gt;...&lt;/strength&gt;&lt;/weapon&gt;
    /// &lt;/weapons&gt;
    /// </summary>
    public static List<Weapon> readWeaponsFromXml(Path xmlPath) throws Exception
    {
        if (xmlPath == null)
            throw new IllegalArgumentException("xmlPath is null.");

        if (!Files.exists(xmlPath))
            throw new IOException("XML file not found: " + xmlPath);

        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(xmlPath.toFile());

        doc.getDocumentElement().normalize();

        NodeList weaponNodes = doc.getElementsByTagName("weapon");
        List<Weapon> weapons = new ArrayList<>();

        for (int i = 0; i < weaponNodes.getLength(); i++)
        {
            Element weaponEl = (Element) weaponNodes.item(i);

            String name = getChildText(weaponEl, "name").trim();
            String strengthText = getChildText(weaponEl, "strength").trim();

            if (name.isEmpty())
                throw new IllegalStateException("Weapon name is missing/blank at index " + i);

            int strength;
            try
            {
                strength = Integer.parseInt(strengthText);
            }
            catch (NumberFormatException ex)
            {
                throw new IllegalStateException("Weapon strength is not a valid integer at index " + i + ": " + strengthText);
            }

            weapons.add(new Weapon(name, strength));
        }

        return weapons;
    }

    private static String getChildText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0)
            return "";

        return nodes.item(0).getTextContent();
    }
}
