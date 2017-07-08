package tn.bank.presentation.mbeans;

import java.util.Date;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import tn.bank.persistence.Customer;
import tn.bank.services.interfaces.CustomerServiceLocal;

@ManagedBean
@SessionScoped
public class AccountBean {

	// Generate a unique number to "NumberAccount" with current date
	Date day = new Date();
	Integer cle = day.getDate() * 7 + day.getMonth() * 30 + day.getYear() * 360 + day.getHours() * 3600
			+ day.getMinutes() * 60 + day.getSeconds();

	// Models
	private Customer user = new Customer();
	// injection of dependency
	@EJB
	private CustomerServiceLocal customerServiceLocal;

	// @ManagedProperty(value = "#{loginBean}")
	// private LoginBean loginBean;

	public String doSaveOrUpdateAccount() {
		// Integer idUser = loginBean.getUser().getId();
		user.setAccountNumber(cle);
		user.setCreationDate(new Date());
		customerServiceLocal.addAccount(user);
		return "";
	}

	public Customer getUser() {
		return user;
	}

	public void setUser(Customer user) {
		this.user = user;
	}

}
