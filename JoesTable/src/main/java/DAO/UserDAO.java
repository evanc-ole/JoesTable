package DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class UserDAO {
	public UserDAO(){}
	
	public int insertNewUser(String uname, String pword, String email) throws ClassNotFoundException{
		try(Connection conn = DatabaseUtil.getConnection()){
			PreparedStatement st = conn.prepareStatement("INSERT INTO Users (username, password, email) VALUES (?, ?, ?)");
			st.setString(1, uname);
			st.setString(2, pword);
			st.setString(3, email);
			st.executeUpdate();
			conn.close();
			return getUserID(uname);
		} catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                if (e.getMessage().contains("username")) {
                    return -1;
                } else if (e.getMessage().contains("email")) {
                    return -2;
                }
            } else {
            	System.out.println(e.getMessage());
            }
        }
		return 0;
	}
	
	public boolean loginUser(String uname, String pword) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT password FROM Users WHERE username = ?")) {

            st.setString(1, uname);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    if (pword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
	}
	
	public int getUserID(String uname) {
		try (Connection conn = DatabaseUtil.getConnection();
	            PreparedStatement st = conn.prepareStatement("SELECT user_id FROM Users WHERE username = ?")) {
	            st.setString(1, uname);
	            try(ResultSet rs = st.executeQuery()){
	            	if(rs.next()) {
	            		int userID = rs.getInt("user_id");
	            		return userID;
	            	}
	            } catch(SQLException sqle) {
	            	System.out.println(sqle.getMessage());
	            }
	        } catch (SQLException sqle) {
	        	System.out.println(sqle.getMessage());
	        }
		return 0;

	}
}
