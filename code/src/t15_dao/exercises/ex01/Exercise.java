package t15_dao.exercises.ex01;

import t15_dao.exercises.DbConfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Exercise {

    public static void run() {

        DbConfig config;
        try {
            config = DbConfig.load(DbConfig.DEFAULT_FILE);
        } catch (IOException e) {
            System.out.println("Could not read " + DbConfig.DEFAULT_FILE
                    + ". Copy db.properties.example to it and fill in your settings.");
            return;
        }

        try (Connection c = DriverManager.getConnection(
                config.url(), config.user(), config.password())) {

            try (PreparedStatement ps = c.prepareStatement("SELECT 1");
                 ResultSet rs = ps.executeQuery()) {

                rs.next();
                int v = rs.getInt(1);
                System.out.println("DB connection OK, SELECT 1 -> " + v);
            }

            try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM cars");
                 ResultSet rs = ps.executeQuery()) {

                rs.next();
                int count = rs.getInt(1);
                System.out.println("Cars in fleet -> " + count);
            }
        }
        catch (SQLException e) {
            // Report the real reason, then the most likely fix
            System.out.println("Database error: " + e.getMessage());
            System.out.println("Check that MySQL is running (XAMPP) and that the car_rental "
                    + "database and cars table exist.");
        }
    }
}
