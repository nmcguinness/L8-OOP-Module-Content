package t10_exception_handling.exercises.ex03;

import java.util.Map;

public class Exercise {

    public static void run() {
        PlayerRepository repo = new PlayerRepository(Map.of("ana", 90, "ben", 75));

        // Search box: the user may type anything, so absence is routine
        System.out.println("Search 'zoe': " + repo.findScore("zoe").map(String::valueOf)
                .orElse("no such player"));

        // Logged-in player: the name came from our own records, so it must exist
        try {
            System.out.println("Ana's score: " + repo.requireScore("ana"));
            System.out.println("Zoe's score: " + repo.requireScore("zoe"));
        } catch (PlayerNotFoundException e) {
            System.out.println("Data problem: " + e.getMessage());
        }
    }
}
