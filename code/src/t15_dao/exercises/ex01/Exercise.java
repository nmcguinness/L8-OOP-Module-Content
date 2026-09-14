package t15_dao.exercises.ex01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Exercise {

    public static void run() throws Exception {

        String url = "jdbc:mysql://localhost:3306/car_rental";
        String user = "root";
        String pass = "";

        try (Connection c = DriverManager.getConnection(url, user, pass)) {

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
        catch(Exception e)
        {
            System.out.println("Remember to make the car_rental DB in PHPMyAdmin and ensure XAMPP is running properly!");
        }
    }
}
