package chao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLManager {
	
	
	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
			        "jdbc:mysql://localhost/"+Constants.DB_NAME, "root", "");
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static void updateDB(Connection conn, String sqlQuery) {
		Statement stmt = null;
		if(conn!=null){
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlQuery);
			} catch (SQLException e) {
			    System.out.println("SQLException: " + e.getMessage());
			}
		}
	}
	
	public static ResultSet queryFromDB(Connection conn, String sqlQuery) {
		Statement stmt = null;
		if(conn!=null){
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlQuery);
				return rs;
			} catch (SQLException e) {
			    System.out.println("SQLException: " + e.getMessage());
			}
		}
		return null;
	}
	
}
