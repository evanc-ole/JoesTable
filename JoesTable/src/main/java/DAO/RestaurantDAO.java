package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RestaurantDAO {
	public RestaurantDAO() {}
	
	public void insertNewRestaurant(String rname, String address, String phone, String category, String price, String yelpurl, String image) throws ClassNotFoundException{
		try(Connection conn = DatabaseUtil.getConnection()){
			PreparedStatement st = conn.prepareStatement("INSERT INTO Restaurants (name, address, phone, price, category, yelpurl, image) VALUES(?, ?, ?, ?, ?, ?, ?)");
			st.setString(1, rname);
			st.setString(2, address);
			st.setString(3, phone);
			st.setString(4, price);
			st.setString(5, category);
			st.setString(6, yelpurl);
			st.setString(7, image);
			st.executeUpdate();
			conn.close();
		} catch(SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
	}
	
	public int getRestaurantID(String rname) {
		try (Connection conn = DatabaseUtil.getConnection();
	            PreparedStatement st = conn.prepareStatement("SELECT restaurant_id FROM Restaurants WHERE name = ?")) {
	            st.setString(1, rname);
	            try(ResultSet rs = st.executeQuery()){
	            	if(rs.next()) {
	            		int restaurantID = rs.getInt("restaurant_id");
	            		return restaurantID;
	            	}
	            } catch(SQLException sqle) {
	            	System.out.println(sqle.getMessage());
	            }
	        } catch (SQLException sqle) {
	        	System.out.println(sqle.getMessage());
	        }
		return 0;
	}
	
	public boolean restaurantExists(String rname) {
		try (Connection conn = DatabaseUtil.getConnection();
	            PreparedStatement st = conn.prepareStatement("SELECT COUNT(*) FROM Restaurants WHERE name = ?")) {
	            st.setString(1, rname);
	            try(ResultSet rs = st.executeQuery()){
	            	if(rs.next()) {
	            		int count = rs.getInt(1);
	            		return count > 0;
	            	}
	            } catch(SQLException sqle) {
	            	System.out.println(sqle.getMessage());
	            }
	        } catch (SQLException sqle) {
	        	System.out.println(sqle.getMessage());
	        }
		return false;
	}
}
