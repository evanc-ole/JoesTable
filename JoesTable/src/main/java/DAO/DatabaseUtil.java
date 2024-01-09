package DAO;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;

public class DatabaseUtil {
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/JoesTableDB?user=root&password=Rocket88B!");
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch(ClassNotFoundException ex) {
			System.out.println("MySQL Driver not found.");
		}
		return conn;
	}
}
