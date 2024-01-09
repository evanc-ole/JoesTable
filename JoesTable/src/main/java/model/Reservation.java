package model;

public class Reservation {
	private String name;
	private String address;
	private String phone;
	private String category;
	private String price;
	private String yelpUrl;
	private String imageUrl;
	private String date;
	private String time;
	private String notes;
	
	public Reservation(String n, String a, String p, String c, String pr, String y, String i, String d, String t, String no) {
		name = n;
		address = a;
		phone = p;
		category = c;
		price = pr;
		yelpUrl = y;
		imageUrl = i;
		date = d;
		time = t;
		notes = no;
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
	public String getDate() {
		return date;
	}
	public String getTime() {
		return time;
	}
	public String getNotes() {
		return notes;
	}
}
