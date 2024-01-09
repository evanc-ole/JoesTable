package controller;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.util.stream.Collectors;

import model.Restaurant;
import DAO.RestaurantDAO;
import DAO.FavoritesDAO;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


/**
 * Servlet implementation class Servlet
 */
@WebServlet(name = "RemoveFavoriteServlet", urlPatterns = {"/removefavoriteServlet"})
public class RemoveFavoriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveFavoriteServlet() {
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
        
        Restaurant r = null;
        try {
            r = new Gson().fromJson(jsonData, Restaurant.class);
            HttpSession session = request.getSession(false);
            int uID = (int) session.getAttribute("uID");
            RestaurantDAO rdao = new RestaurantDAO();
            FavoritesDAO fdao = new FavoritesDAO();
            fdao.removeFavorite(uID, rdao.getRestaurantID(r.getName()));
            response.setStatus(HttpServletResponse.SC_OK);
            pw.write(gson.toJson(1));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            pw.write(gson.toJson(0));
        }
        
        
	}

}
