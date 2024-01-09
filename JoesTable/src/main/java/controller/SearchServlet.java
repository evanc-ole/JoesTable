package controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;





@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private static final String YELP_API_KEY = "6u6ER3ZEUMjfq6HWkDSeeRVkvDTdfJHLaDYZpqDZXkjsHPYYObRB6zHwwP0MRwPccZTYtHUog2hct5qfbg_g4RXSrevkpeWBVzkK4oJReGAfULjBlYZH46FOBaY9ZXYx"; // Replace with your Yelp API key

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Read the request body to get search parameters
        String jsonData = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(jsonData);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the error properly
        }
        System.out.println(jsonObject);
        String term = (String) jsonObject.get("term");
        String latitude = (String) jsonObject.get("latitude");
        String longitude = (String) jsonObject.get("longitude");
        String sort = (String) jsonObject.get("sort");
        // Build the Yelp search URL
        String yelpSearchUrl = "https://api.yelp.com/v3/businesses/search?term=" + URLEncoder.encode(term, "UTF-8") +
                                "&latitude=" + latitude +
                                "&longitude=" + longitude;

        // Set up the HTTP connection to the Yelp API
        URL url = new URL(yelpSearchUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + YELP_API_KEY);

        // Get the response from Yelp
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder responseContent = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            responseContent.append(line);
        }
        reader.close();

        // Convert the response into a JSON object
        JSONObject jsonResponse = null;
        try {
            jsonResponse = (JSONObject) parser.parse(responseContent.toString());
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the error properly
        }
        JSONArray businesses = (JSONArray) jsonResponse.get("businesses");

		 // Convert JSONArray to a List of JSONObject for sorting
		 List<JSONObject> businessList = new ArrayList<>();
		 for (Object obj : businesses) {
		     businessList.add((JSONObject) obj);
		 }
		
		 // Sort the list based on the sort criteria
		 switch (sort) {
		     case "review-count":
		         businessList.sort(Comparator.comparingInt(b -> ((Number) ((JSONObject) b).get("review_count")).intValue()).reversed());
		         break;
		     case "rating":
		         businessList.sort(Comparator.comparingDouble(b -> ((Number) ((JSONObject) b).get("rating")).doubleValue()).reversed());
		         break;
		     case "distance":
		         businessList.sort(Comparator.comparingDouble(b -> ((Number) ((JSONObject) b).get("distance")).doubleValue()));
		         break;
		     // Assuming 'best-match' doesn't need sorting as it's the default order
		 }
		
		 // Limit to top 10 results and map to required fields
		 JSONArray topBusinesses = new JSONArray();
		 businessList.stream().limit(10).forEach(b -> {
		     JSONObject business = new JSONObject();
		     business.put("image_url", b.get("image_url"));
		     business.put("name", b.get("name"));
		     business.put("display_address", String.join(", ", (List<String>) ((JSONObject) b.get("location")).get("display_address")));
		     business.put("display_phone", b.get("display_phone"));
		     business.put("url", b.get("url"));
		     business.put("rating", b.get("rating"));
		     business.put("price", b.get("price"));
		     
		     // Extract category title
		     JSONArray categories = (JSONArray) b.get("categories");
		     if (categories != null && !categories.isEmpty()) {
		         JSONObject category = (JSONObject) categories.get(0);
		         business.put("category_title", category.get("title"));
		     }
		     topBusinesses.add(business);
		 });
		
		 // Send the processed data back to the client
		 response.setContentType("application/json");
		 response.setCharacterEncoding("UTF-8");
		 response.getWriter().write(topBusinesses.toJSONString());
		}
}
