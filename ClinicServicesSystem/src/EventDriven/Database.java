package EventDriven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public static Connection sqlConnect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ClinicDB", "root", "Gelo123");

            try {
                String checkAdmin = "SELECT COUNT(*) FROM AdminAccount";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(checkAdmin);

                if (rs.next() && rs.getInt(1) == 0) {
                    String defaultPass = "admin123";
                    String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(defaultPass, org.mindrot.jbcrypt.BCrypt.gensalt());

                    String sql = "INSERT INTO AdminAccount (AdminID, Username, Password) VALUES (?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, "A-001");
                    ps.setString(2, "admin");
                    ps.setString(3, hashed);
                    ps.executeUpdate();
                    System.out.println(">>> Default Admin 'admin' created with password 'admin123'");
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (SQLException ex) {
            System.out.println("Missing driver/Connection error: " + ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("Not connected: " + ex);
        }
        return conn;
    }
}
