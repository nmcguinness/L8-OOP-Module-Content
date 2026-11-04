package t18_io.exercises.ex06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Exercise {

    public static void run() throws IOException {
        Path file = Files.createTempFile("game", ".properties");
        Files.writeString(file, "# Game settings\nmaxPlayers = 8\nmapName=forest\nvolume=loud\n");

        ConfigLoader config = new ConfigLoader(file);
        System.out.println(config.asMap());
        System.out.println("maxPlayers: " + config.getInt("maxPlayers", 4));
        System.out.println("volume (not a number): " + config.getInt("volume", 50));
        System.out.println("difficulty (missing): " + config.getInt("difficulty", 1));

        Files.deleteIfExists(file);
    }
}
