package controller;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import model.Restaurant;
import model.Reservation;
import DAO.RestaurantDAO;
import DAO.FavoritesDAO;
import DAO.ReservationsDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;


/**
 * Servlet implementation class Servlet
 */
@WebServlet(name = "ReservationServlet", urlPatterns = {"/reservationServlet"})
public class ReservationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReservationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		Gson gson = new Gson();
    	response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String jsonData = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println(jsonData);
        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
	
	     // Extract the data into separate variables
	     String date = jsonObject.get("date").getAsString();
	     String time = jsonObject.get("time").getAsString();
	     String notes = jsonObject.has("notes") ? jsonObject.get("notes").getAsString() : "";
	     
	     
        Restaurant r = null;
        try {
            r = new Gson().fromJson(jsonData, Restaurant.class);
            HttpSession session = request.getSession(false);
            int uID = (int) session.getAttribute("uID");
            RestaurantDAO rdao = new RestaurantDAO();
	            try {
	            	if(!rdao.restaurantExists(r.getName())) {
	            		rdao.insertNewRestaurant(r.getName(), r.getAddress(), r.getPhone(), r.getCategory(), r.getPrice(), r.getYelpUrl(), r.getImage());
	            	}
	            } catch(ClassNotFoundException e) {
	            	System.out.println(e.getMessage());
	            }
	            ReservationsDAO fdao = new ReservationsDAO();
	            fdao.addReservation(uID, rdao.getRestaurantID(r.getName()), date, time, notes);
	            response.setStatus(HttpServletResponse.SC_OK);
	            pw.write(gson.toJson(1));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            pw.write(gson.toJson(0));
        }
        
        
	}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	PrintWriter pw = response.getWriter();
		Gson gson = new Gson();
    	response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");   
        try {
            HttpSession session = request.getSession(false);
            int uID = (int) session.getAttribute("uID");
            ReservationsDAO fdao = new ReservationsDAO();
            List<Reservation> favorites = fdao.getReservations(uID);
            System.out.println(favorites);
            response.setStatus(HttpServletResponse.SC_OK);
            pw.write(gson.toJson(favorites));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            pw.write(gson.toJson(0));
        }
	}

}
