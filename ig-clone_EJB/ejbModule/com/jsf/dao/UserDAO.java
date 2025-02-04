package com.jsf.dao;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.jsf.entities.User;

@Stateless
public class UserDAO {
	private final static String UNIT_NAME = "ig-clone";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	public List<User> getSuggestions(User suggestionsFor){
		String queryString = "SELECT DISTINCT u FROM User u WHERE u.id NOT IN (SELECT f.user2.id FROM Follow f WHERE f.user1.id = :following_user_id) AND u.id != :following_user_id";
		
		Query query = em.createQuery(queryString);
		
		query.setParameter("following_user_id", suggestionsFor.getId());
		
		return (List<User>) query.setMaxResults(5).getResultList();
	}
	
	public User login(String nickname, String password) {
		String queryString = "select u from User u where nickname = :nickname and password = :password";
		Query query = em.createQuery(queryString);
		
		query.setParameter("nickname", nickname);
		query.setParameter("password", password);
		
		User user = null;
		
		try {
			user = (User) query.getSingleResult();
			return user;
		}catch(NoResultException e) {
			return null;
		}
	}
	
	public boolean checkUniqueNickname(String nickname) {
		String queryString = "select u from User u where nickname = :nickname";
		Query query = em.createQuery(queryString);
		query.setParameter("nickname", nickname);
		return query.getResultList().size() == 0;
	}
	
	public boolean checkUniqueEmail(String email) {
		String queryString = "select u from User u where email = :email";
		Query query = em.createQuery(queryString);
		query.setParameter("email", email);
		return query.getResultList().size() == 0;
	}

	public void create(User user) {
		em.persist(user);
	}

	public User merge(User user) {
		return em.merge(user);
	}

	public void remove(User user) {
		em.remove(em.merge(user));
	}

	public User find(Object id) {
		return em.find(User.class, id);
	}

	public List<User> getFullList() {
		List<User> list = null;

		Query query = em.createQuery("select u from User u");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<User> getList(Map<String, Object> searchParams) {
		List<User> list = null;

		// 1. Build query string with parameters
		String select = "select u ";
		String from = "from User u ";
		String where = "";
		String orderby = "order by u.nickname asc, u.firstName";

		// search for surname
		String nickname = (String) searchParams.get("nickname");
		if (nickname != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "or ";
			}
			where += "u.nickname like :nickname ";
		}
		
		String firstName = (String) searchParams.get("firstName");
		if (firstName != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "or ";
			}
			where += "u.firstName like :firstName ";
		}
		
		String lastName = (String) searchParams.get("lastName");
		if (lastName != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "or ";
			}
			where += "u.lastName like :lastName ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (nickname != null) {
			query.setParameter("nickname", nickname+"%");
		}
		if (firstName != null) {
			query.setParameter("firstName", firstName+"%");
		}
		if (lastName != null) {
			query.setParameter("lastName", lastName+"%");
		}

		// ... other parameters ... 

		// 4. Execute query and retrieve list of Person objects
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
