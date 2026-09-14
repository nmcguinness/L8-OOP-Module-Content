package t09_interface.exercises.ex07;

public class Exercise {

    public static void run() {
        PlayerProfile p1 = new PlayerProfile("Alex", 10);
        PlayerProfile p2 = new PlayerProfile("Morgan", 25);

        String xml1 = p1.toXml();
        String xml2 = p2.toXml();

        System.out.println("XML 1: " + xml1);
        System.out.println("XML 2: " + xml2);

        PlayerProfile loaded1 = PlayerProfile.fromXml(xml1);
        PlayerProfile loaded2 = PlayerProfile.fromXml(xml2);

        System.out.println("Loaded 1 -> name=" + loaded1.getName() + ", score=" + loaded1.getScore());
        System.out.println("Loaded 2 -> name=" + loaded2.getName() + ", score=" + loaded2.getScore());
    }
}
