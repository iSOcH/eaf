package ch.fhnw.edu.rental.daos;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DAO<T> {
	T getById(Long id);
	List<T> getAll();
	void saveOrUpdate(T t);
	void delete(T t);
}
