package controller;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;


/**
 * Servlet implementation class Servlet
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/homeServlet"})
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
    	response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        HttpSession session = request.getSession(false); // Don't create if it doesn't exist
    	int uID = 0;
    	if (session != null) {
    	    uID = (int) session.getAttribute("uID");
    	}
    	Map<String, Integer> responseMap = new HashMap<>();
    	responseMap.put("userID", uID);
    	pw.write(gson.toJson(responseMap));
	}

}
