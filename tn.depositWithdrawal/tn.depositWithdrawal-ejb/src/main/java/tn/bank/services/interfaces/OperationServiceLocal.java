package tn.bank.services.interfaces;

import java.util.List;

import javax.ejb.Local;

import tn.bank.persistence.Operation;

@Local
public interface OperationServiceLocal {
	public void addOrUpdateBalanceAccount(Operation operation);

	List<Operation> findAllOperationsById(Integer id);
}
