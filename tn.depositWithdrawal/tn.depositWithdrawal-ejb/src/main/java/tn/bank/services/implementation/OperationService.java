package tn.bank.services.implementation;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.bank.persistence.Operation;
import tn.bank.services.interfaces.OperationServiceLocal;
import tn.bank.services.interfaces.OperationServiceRemote;

/**
 * Session Bean implementation class OperationService
 */
@Stateless
@LocalBean
public class OperationService implements OperationServiceRemote, OperationServiceLocal {
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Default constructor.
	 */
	public OperationService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addOrUpdateBalanceAccount(Operation operation) {
		entityManager.merge(operation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Operation> findAllOperationsById(Integer id) {
		String jpql = "SELECT o FROM Operation o where o.customer.id=:id";
		Query query = entityManager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList();
	}

}
