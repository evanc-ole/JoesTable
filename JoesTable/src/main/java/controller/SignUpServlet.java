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

import model.User;
import DAO.UserDAO;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


/**
 * Servlet implementation class Servlet
 */
@WebServlet(name = "SignUpServlet", urlPatterns = {"/signupServlet"})
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
    	response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String jsonData = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println("Received JSON data: " + jsonData);
        
        User user = null;
        try {
            user = new Gson().fromJson(jsonData, User.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if(user == null) {
        	System.out.println("User still null");
        	user = new User();
        }
        String username = user.getUname();
        String password = user.getPword();
        String email = user.getEmail();
        
        Gson gson = new Gson();
        
        if (username == null || username.isBlank() || password == null || password.isBlank()
        		|| email == null || email.isBlank()) {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	String error = "User info is missing";
        	pw.write(gson.toJson(error));
        	pw.flush();
        }
        UserDAO udao = new UserDAO();
        int ID = 0;
        try{
        	ID = udao.insertNewUser(username, password, email);
        } catch(ClassNotFoundException e) {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String error = "Server error: Class not found"; 
            pw.write(gson.toJson(error));
            pw.flush();
        }
        if(ID == -1) {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	String error = "Username is already in use";
        	pw.write(gson.toJson(error));
        	pw.flush();
        } else if(ID == -2) {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	String error = "Email is already in use";
        	pw.write(gson.toJson(error));
        	pw.flush();
        }else if(ID != 0){
        	HttpSession session = request.getSession();
        	session.setAttribute("uID", udao.getUserID(username));
        	response.setStatus(HttpServletResponse.SC_OK);
        	pw.write(gson.toJson(ID));
        	pw.flush();
        } else {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	String error = "ID equals to 0";
        	pw.write(gson.toJson(error));
        	pw.flush();
        }
        
	}

}
