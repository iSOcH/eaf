package ch.fhnw.edu.rental.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;

import ch.fhnw.edu.rental.model.Movie;
import ch.fhnw.edu.rental.model.PriceCategory;
import ch.fhnw.edu.rental.model.Rental;
import ch.fhnw.edu.rental.model.User;

public class BusinessLogicEjb implements BusinessLogic {
	
	private MovieService movieService;
	private UserService userService;
	private RentalService rentalService;

	public BusinessLogicEjb() throws Exception {
		InitialContext ctx = new InitialContext();
		movieService = (MovieService) ctx.lookup("movies/MovieServiceImpl!ch.fhnw.edu.rental.services.MovieService");
		userService = (UserService) ctx.lookup("movies/UserServiceImpl!ch.fhnw.edu.rental.services.UserService");
		rentalService = (RentalService) ctx.lookup("movies/RentalServiceImpl!ch.fhnw.edu.rental.services.RentalService");
	}
	
	public String getUserLastName(Long id){
		return getUser(id).getLastName();
	}
	public String getUserFirstName(Long id){
		return getUser(id).getFirstName();
	}
	public int getUserRentalsSize(Long id){
		return getUser(id).getRentals().size();
	}
	
	public String getMovieTitle(Long id){
		return getMovie(id).getTitle();
	}
	public String getMoviePriceCategory(Long id){
		return getMovie(id).getPriceCategory().toString();
	}
	public Date getMovieReleaseDate(Long id){
		return getMovie(id).getReleaseDate();
	}
	public boolean getMovieIsRented(Long id){
		return getMovie(id).isRented();
	}
	
	
	private User user;
	private User getUser(Long id){
		if(user == null || !user.getId().equals(id)){
			user = userService.getUserById(id);
		}
		return user;
	}
	
	private Movie movie;
	private Movie getMovie(Long id){
		if(movie == null || !movie.getId().equals(id)){
			movie = movieService.getMovieById(id);
		}
		return movie;
	}
	
	private Rental rental;
	private Rental getRental(Long id){
		if(rental == null || !rental.getId().equals(id)){
			rental = rentalService.getRentalById(id);
		}
		return rental;
	}
	
	void invalidateCache(){
		movie = null;
		user = null;
	}
	
	public void removeRental(Long rentalId){
		rentalService.deleteRental(getRental(rentalId));
		invalidateCache();
	}
	
	public void deleteUser(Long userId) {
		userService.deleteUser(getUser(userId));
		invalidateCache();
	}

	public void updateUser(Long userId, String lastName, String firstName) {
		User currUser = getUser(userId);
		currUser.setLastName(lastName);
		currUser.setFirstName(firstName);
		userService.saveOrUpdateUser(currUser);
		invalidateCache();
	}

	public Long createUser(String lastName, String firstName) {
		User user = new User(lastName, firstName);
		userService.saveOrUpdateUser(user);
		invalidateCache();
		return user.getId();
	}
	
	public void deleteMovie(Long movieId) {
		movieService.deleteMovie(getMovie(movieId));
		invalidateCache();
	}

	public Long createMovie(String movieTitle, Date date, String category) {
		Movie m = null;
		List<PriceCategory> categories = movieService.getAllPriceCategories();
		for(PriceCategory c : categories){
			if(c.toString().equals(category)){
				m = new Movie(movieTitle, date, c);
			}
		}
		movieService.saveOrUpdateMovie(m);
		invalidateCache();
		return m.getId();
	}

	public void updateMovie(Long movieId, String movieTitle, Date date,	String category) {
		Movie m = null;
		List<PriceCategory> categories = movieService.getAllPriceCategories();
		for(PriceCategory c : categories){
			if(c.toString().equals(category)){
				m = new Movie(movieTitle, date, c);
				// TODO what happens with the rented flag
				m.setId(movieId);
			}
		}
		movieService.saveOrUpdateMovie(movie);
		invalidateCache();
	}

	public void createRental(Long movieId, Long userId, Integer rentalDays) {
		User user = userService.getUserById(userId);
		Movie movie = movieService.getMovieById(movieId);
		userService.rentMovie(user, movie, rentalDays);
		invalidateCache();
	}

	public void visitUsers(UserVisitor visitor) {
		for(User u : userService.getAllUsers()){
			visitor.visit(u.getId(), u.getLastName(), u.getFirstName());
		}
	}

	public void visitMovies(MovieVisitor visitor) {
		for(Movie m : movieService.getAllMovies()){
			visitor.visit(m.getId(), m.getTitle(), m.getReleaseDate(), m.isRented(), m.getPriceCategory().toString());
		}
	}

	public void visitRentals(RentalVisitor visitor) {
		Date now = Calendar.getInstance().getTime();
		for(Rental r : rentalService.getAllRentals()){
			User user = r.getUser();
			Movie movie = r.getMovie();
			visitor.visit(r.getId(), r.getRentalDays(), r.getRentalDate(), user.getLastName(), user.getFirstName(), movie.getTitle(), r.calcRemainingDaysOfRental(now), r.getRentalFee());
		}
	}

	public void visitRentalsOfUser(Long userId, RentalVisitor visitor) {
		User user = userService.getUserById(userId);
		Date now = Calendar.getInstance().getTime();
		for(Rental r : user.getRentals()){
			Movie movie = r.getMovie();
			visitor.visit(r.getId(), r.getRentalDays(), r.getRentalDate(), user.getLastName(), user.getFirstName(), movie.getTitle(), r.calcRemainingDaysOfRental(now), r.getRentalFee());
		}
	}

	
}
