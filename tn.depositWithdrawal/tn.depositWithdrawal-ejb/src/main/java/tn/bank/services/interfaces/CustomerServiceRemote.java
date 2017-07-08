package tn.bank.services.interfaces;

import javax.ejb.Remote;

import tn.bank.persistence.Customer;

@Remote
public interface CustomerServiceRemote {
	Customer login(String login, String password);

	void addAccount(Customer customer);

	Customer findCustomerById(Integer id);

}
