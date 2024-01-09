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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import DAO.UserDAO;
import com.google.gson.Gson;



/**
 * Servlet implementation class Servlet
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/loginServlet"})
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
        Gson gson = new Gson();
        try {
            // Parse JSON data
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);

            // Extract username and password from JSON object
            String username = (String) jsonObject.get("username");
            String password = (String) jsonObject.get("password");
            UserDAO dao = new UserDAO();
            if(dao.loginUser(username, password)) {
            	HttpSession session = request.getSession();
            	session.setAttribute("uID", dao.getUserID(username));
            	response.setStatus(HttpServletResponse.SC_OK);
            	pw.write(gson.toJson(1));
            	pw.flush();
            } else {
            	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            	String error = "Incorrect Username or Password";
            	pw.write(gson.toJson(error));
            	pw.flush();
            }
           
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parse exception
        }
        
	}

}
