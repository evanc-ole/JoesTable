package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Reservation;

public class ReservationsDAO {
	public ReservationsDAO() {}
	
	public void addReservation(int u_id, int r_id, String date, String time, String notes) {
		System.out.println("trying to add res");
		try (Connection conn = DatabaseUtil.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Reservations (user_id, restaurant_id, reservation_date, reservation_time, reservation_notes) VALUES (?, ?, ?, ?, ?)")){
	            pstmt.setInt(1, u_id);
	            pstmt.setInt(2, r_id);
	            pstmt.setString(3, date);
	            pstmt.setString(4, time);
	            pstmt.setString(5, notes);
	            pstmt.executeUpdate();
	            conn.close();
	        } catch (SQLException sqle) {
	            System.out.println(sqle.getMessage());
	        }
	}
	
	public void removeReservation(int u_id, int r_id, String date, String time) {
		try (Connection conn = DatabaseUtil.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Favorites WHERE user_id = ? AND restaurant_id = ? AND date = ? AND time = ?")){
	            pstmt.setInt(1, u_id);
	            pstmt.setInt(2, r_id);
	            pstmt.setString(3, date);
	            pstmt.setString(4, time);
	            
	        } catch (SQLException sqle) {
	            System.out.println(sqle.getMessage());
	        }
	}
	
	public List<Reservation> getReservations(int uID){
		List<Reservation> favorites = new ArrayList<>();
		String query = "SELECT r.reservation_date, r.reservation_time, r.reservation_notes, rest.name, rest.address, rest.phone, rest.price, rest.yelpurl, rest.image, rest.category " +
	               "FROM Reservations r " +
	               "JOIN Restaurants rest ON r.restaurant_id = rest.restaurant_id " +
	               "WHERE r.user_id = ?";
		try (Connection conn = DatabaseUtil.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(query)){
				pstmt.setInt(1, uID);
	
	            try (ResultSet resultSet = pstmt.executeQuery()) {
	                while (resultSet.next()) {
	                    // Assuming Restaurant is a class that represents a restaurant entity
	                    Reservation restaurant = new Reservation(
	                            resultSet.getString("name"),
	                            resultSet.getString("address"),
	                            resultSet.getString("phone"),
	                            resultSet.getString("price"),
	                            resultSet.getString("category"),
	                            resultSet.getString("yelpurl"),
	                            resultSet.getString("image"),
	                            resultSet.getString("reservation_date"),
	                            resultSet.getString("reservation_time"),
	                            resultSet.getString("reservation_notes")
	                            
	                    );
	                    favorites.add(restaurant);
	                }
	            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
		return favorites;
	}
}
