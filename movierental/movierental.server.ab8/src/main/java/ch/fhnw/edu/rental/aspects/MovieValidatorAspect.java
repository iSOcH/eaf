package ch.fhnw.edu.rental.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.fhnw.edu.rental.model.Movie;

@Aspect
@Component
public class MovieValidatorAspect {
	private static final Logger LOG = LoggerFactory
			.getLogger(MovieValidatorAspect.class);
	@Autowired
	private MovieValidator movieValidator;

	@Around("execution(* *..*.MovieService.saveOrUpdateMovie(..)) && args(movie)")
	public void checkMovieEntity(ProceedingJoinPoint pjp, Movie movie) throws Throwable {
		if (movieValidator.isValid(movie)) {
			LOG.debug("Proceeding for movie '{}'", movie.getTitle());
			pjp.proceed();
		} else {
			LOG.debug("Movie '{}' is not valid", movie.getTitle());
			throw new RuntimeException("Movie Bean not valid");
		}
	}
}
