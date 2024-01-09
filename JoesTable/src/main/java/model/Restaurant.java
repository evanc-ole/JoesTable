package model;

public class Restaurant {
	private String name;
	private String address;
	private String phone;
	private String category;
	private String price;
	private String yelpUrl;
	private String imageUrl;
	
	public Restaurant(String n, String a, String p, String c, String pr, String y, String i) {
		name = n;
		address = a;
		phone = p;
		category = c;
		price = pr;
		yelpUrl = y;
		imageUrl = i;
	}
	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	public String getPhone() {
		return phone;
	}
	public String getCategory() {
		return category;
	}
	public String getPrice() {
		return price;
	}
	public String getYelpUrl() {
		return yelpUrl;
	}
	public String getImage() {
		return imageUrl;
	}
}
