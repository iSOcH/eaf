package ch.fhnw.edu.rental.model;

import java.sql.Timestamp;
import java.util.Date;

public class Movie {
	private Long id;
	
	private final String title;
	private final Date releaseDate;
	private boolean rented;
	private PriceCategory priceCategory;

	public Movie(String title, Date releaseDate, PriceCategory priceCategory) throws NullPointerException {
		if ((title == null) || (releaseDate == null) || (priceCategory == null)) {
			throw new NullPointerException("not all input parameters are set!");
		}
		this.title = title;
		this.releaseDate = releaseDate;
		this.priceCategory = priceCategory;
		this.rented = false;
	}

    public Movie(Long movie_id, String movie_title, Timestamp movie_releasedate, Boolean movie_rented, PriceCategory pc) {
        id = movie_id;
        title = movie_title;
        releaseDate = movie_releasedate;
        rented = movie_rented;
        priceCategory = pc;
    }

    public PriceCategory getPriceCategory() {
		return priceCategory;
	}

	public void setPriceCategory(PriceCategory priceCategory) {
		this.priceCategory = priceCategory;
	}

	public String getTitle() {
		return title;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public boolean isRented() {
		return rented;
	}

	public void setRented(boolean rented) {
		this.rented = rented;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
