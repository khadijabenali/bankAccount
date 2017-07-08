package tn.bank.services.implementation;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.bank.persistence.Customer;
import tn.bank.services.interfaces.CustomerServiceLocal;
import tn.bank.services.interfaces.CustomerServiceRemote;

/**
 * Session Bean implementation class CustomerService
 */
@Stateless
@LocalBean
public class CustomerService implements CustomerServiceRemote, CustomerServiceLocal {
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Default constructor.
	 */
	public CustomerService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Customer login(String login, String password) {
		Customer user = null;
		String jpql = "SELECT u FROM Customer u WHERE u.login = :param1 AND u.password = :param2";
		Query query = entityManager.createQuery(jpql);
		query.setParameter("param1", login);
		query.setParameter("param2", password);

		user = (Customer) query.getSingleResult();

		return user;
	}

	@Override
	public void addAccount(Customer customer) {
		entityManager.merge(customer);

	}

	@Override
	public Customer findCustomerById(Integer id) {
		// TODO Auto-generated method stub
		return entityManager.find(Customer.class, id);
	}

	@Override
	public Float addAmount(Float amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer findIdByAccountNumber(Integer nbr) {
		String jpql = "SELECT c.id FROM Customer c WHERE c.accountNumber=:nbr";
		Query query = entityManager.createQuery(jpql);
		query.setParameter("nbr", nbr);
		return (Integer) query.getSingleResult();
	}

}
