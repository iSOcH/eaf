package ch.fhnw.edu.rental.aspects;


import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.fhnw.edu.rental.model.Movie;

@Component
@Aspect
public class MovieProgressAspect {
	private static final Logger LOG = LoggerFactory.getLogger(MovieProgressAspect.class);
	
	//@Autowired
	private MovieProgress movieProgress = new MovieProgress();
	
	@AfterReturning(pointcut="execution(List<Movie> ch.fhnw.edu.rental.services.MovieService.getAllMovies())", returning="movieList")
	public void checkMovieList(Object movieList) {
		List<Movie> list = (List<Movie>) movieList;
		if (movieProgress.checkForUpdateProgress(list)) {
			LOG.debug(movieProgress.toString());
		}
	}
	
}
