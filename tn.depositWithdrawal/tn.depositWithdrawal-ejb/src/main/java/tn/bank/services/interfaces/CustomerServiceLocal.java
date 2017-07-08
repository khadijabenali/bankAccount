package tn.bank.services.interfaces;

import javax.ejb.Local;

import tn.bank.persistence.Customer;

@Local
public interface CustomerServiceLocal {
	Customer login(String login, String password);

	void addAccount(Customer customer);

	Customer findCustomerById(Integer id);

	Float addAmount(Float amount);

	Integer findIdByAccountNumber(Integer nbr);

}
