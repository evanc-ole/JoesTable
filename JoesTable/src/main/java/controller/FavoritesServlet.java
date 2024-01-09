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

import model.Restaurant;
import DAO.FavoritesDAO;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


/**
 * Servlet implementation class Servlet
 */
@WebServlet(name = "FavoritesServlet", urlPatterns = {"/favoritesServlet"})
public class FavoritesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FavoritesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
	
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		Gson gson = new Gson();
    	response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");   
        try {
            HttpSession session = request.getSession(false);
            int uID = (int) session.getAttribute("uID");
            FavoritesDAO fdao = new FavoritesDAO();
            List<Restaurant> favorites = fdao.getFavorites(uID);
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
