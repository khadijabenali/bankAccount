package tn.bank.presentation.mbeans;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import tn.bank.services.interfaces.CustomerServiceLocal;

@ManagedBean
@SessionScoped
public class OperationsBean {

	// Models
	// private Customer user = new Customer();
	// injection of dependency

	@EJB
	private CustomerServiceLocal customerServiceLocal;

}
