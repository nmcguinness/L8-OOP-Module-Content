package t09_interface.exercises.ex09;

import t09_interface.exercises.ex07.XmlSerializable;

public class PlayerProfile implements XmlSerializable {

    private final String name;
    private final int score;

    public PlayerProfile(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toXml() {
        return "<player name=\"" + name + "\" score=\"" + score + "\" />";
    }

    public static PlayerProfile fromXml(String xml) {
        String name = extractAttribute(xml, "name");
        String scoreText = extractAttribute(xml, "score");
        int score = Integer.parseInt(scoreText);
        return new PlayerProfile(name, score);
    }

    private static String extractAttribute(String xml, String attributeName) {
        String pattern = attributeName + "=\"";
        int start = xml.indexOf(pattern);
        if (start < 0) {
            throw new IllegalArgumentException("Attribute '" + attributeName + "' not found in: " + xml);
        }
        start += pattern.length();
        int end = xml.indexOf("\"", start);
        if (end < 0) {
            throw new IllegalArgumentException("Malformed attribute '" + attributeName + "' in: " + xml);
        }
        return xml.substring(start, end);
    }
}
