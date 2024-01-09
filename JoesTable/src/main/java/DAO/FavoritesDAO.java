package DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import model.Restaurant;

public class FavoritesDAO {
	public FavoritesDAO() {}
	
	public void addFavorite(int u_id, int r_id) {
		try (Connection conn = DatabaseUtil.getConnection();
	            PreparedStatement st = conn.prepareStatement("INSERT INTO Favorites (user_id, restaurant_id) VALUES (?, ?)")){
	            st.setInt(1, u_id);
	            st.setInt(2, r_id);
	            st.executeUpdate();
	            conn.close();
	        } catch (SQLException sqle) {
	            System.out.println(sqle.getMessage());
	        }
	}

	public void removeFavorite(int u_id, int r_id) {
		try (Connection conn = DatabaseUtil.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Favorites WHERE user_id = ? AND restaurant_id = ?")){
	            pstmt.setInt(1, u_id);
	            pstmt.setInt(2, r_id);
	            pstmt.executeUpdate();
	        } catch (SQLException sqle) {
	            System.out.println(sqle.getMessage());
	        }
	}
	
	public List<Restaurant> getFavorites(int uID){
		List<Restaurant> favorites = new ArrayList<>();
		String query = "SELECT r.* FROM Restaurants r " +
                "JOIN Favorites f ON r.restaurant_id = f.restaurant_id " +
                "WHERE f.user_id = ?";
		try (Connection conn = DatabaseUtil.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(query)){
				pstmt.setInt(1, uID);
	
	            try (ResultSet resultSet = pstmt.executeQuery()) {
	                while (resultSet.next()) {
	                    // Assuming Restaurant is a class that represents a restaurant entity
	                    Restaurant restaurant = new Restaurant(
	                            resultSet.getString("name"),
	                            resultSet.getString("address"),
	                            resultSet.getString("phone"),
	                            resultSet.getString("price"),
	                            resultSet.getString("category"),
	                            resultSet.getString("yelpurl"),
	                            resultSet.getString("image")
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
