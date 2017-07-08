package tn.bank.presentation.mbeans;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import tn.bank.persistence.Customer;
import tn.bank.persistence.Operation;
import tn.bank.services.interfaces.CustomerServiceLocal;
import tn.bank.services.interfaces.OperationServiceLocal;

@ManagedBean
@SessionScoped
public class LoginBean {
	// Models
	private Customer user = new Customer();
	private Customer user2 = new Customer();
	private Operation operation = new Operation();
	private List<Operation> operations = new ArrayList<Operation>();

	// injection of dependency
	@EJB
	private CustomerServiceLocal customerServiceLocal;

	@EJB
	private OperationServiceLocal operationServiceLocal;

	// Recall sevices do...(){}

	public String doLogin() {
		Customer userLoggedIn = null;
		String navigateTo = "";
		userLoggedIn = customerServiceLocal.login(user.getLogin(), user.getPassword());
		if (userLoggedIn != null) {
			user = userLoggedIn;

			navigateTo = "/pages/customerHome/CustomerHome?faces-redirect=true";

		} else {
			navigateTo = "/pages/customerHome/ErreurLogin?faces-redirect=true";

		}
		return navigateTo;
	}

	public String makeDeposit() {
		return "/pages/customerHome/MakeDeposit?faces-redirect=true";
	}

	public String operationList() {
		return "/pages/customerHome/DisplayOperations?faces-redirect=true";

	}

	public String doSaveOrUpdateBalanceAccount() {
		Customer c1 = customerServiceLocal.findCustomerById(user.getId());

		operation.setOperationDate(new Date());
		operation.setCustomer(user);
		operation.setAmount(c1.getBalance());
		operation.setState("Deposit");
		operationServiceLocal.addOrUpdateBalanceAccount(operation);
		customerServiceLocal.findCustomerById(user.getId());

		System.out.println("balance:" + user.getBalance());
		if (user.getBalance() == null) {

			customerServiceLocal.addAccount(user);
		} else {
			Customer c = customerServiceLocal.findCustomerById(user.getId());
			Float nbr = c.getBalance();
			nbr += user.getBalance();
			user.setBalance(nbr);
			customerServiceLocal.addAccount(user);

		}
		return "";
	}

	public String bankTranfer() {
		return "/pages/customerHome/BankTranfer?faces-redirect=true";
	}

	public String doRetrieveSavingsBankTranfer() {
		Customer c1 = customerServiceLocal.findCustomerById(user.getId());
		if ((c1.getBalance() > user.getBalance()) && (user.getBalance() != 0)) {
			operation.setOperationDate(new Date());
			operation.setCustomer(user);
			operation.setAmount(c1.getBalance());
			operation.setState("Retrieve");
			operationServiceLocal.addOrUpdateBalanceAccount(operation);
			customerServiceLocal.findCustomerById(user.getId());
			Float nbr = c1.getBalance();
			nbr -= user.getBalance();
			user.setBalance(nbr);
			customerServiceLocal.addAccount(user);
			// doSendPDFFileViaEmail();
			System.out.println("user2" + user2.getId());
		} else {
			return "/pages/customerHome/ErreurLogin?faces-redirect=true";

		}

		return "/pages/customerHome/BankTranferValidation?faces-redirect=true";
	}

	public String doSaveOrUpdateBalanceAccountBankTranfer() {

		Integer idUser2 = customerServiceLocal.findIdByAccountNumber(user2.getAccountNumber());
		Customer c1 = customerServiceLocal.findCustomerById(idUser2);
		System.out.println("user2" + user2.getId());
		operation.setOperationDate(new Date());
		operation.setCustomer(user2);
		operation.setAmount(c1.getBalance());
		operation.setState("Deposit");
		operationServiceLocal.addOrUpdateBalanceAccount(operation);
		customerServiceLocal.findCustomerById(idUser2);

		System.out.println("balance:" + user2.getBalance());
		if (user2.getBalance() == null) {

			customerServiceLocal.addAccount(user2);
		} else {
			Customer c = customerServiceLocal.findCustomerById(idUser2);
			Float nbr = c.getBalance();
			nbr += user2.getBalance();
			user2.setBalance(nbr);
			customerServiceLocal.addAccount(user2);

		}
		return "";
	}

	public String retrieveSavings() {
		return "/pages/customerHome/RetrieveSavings?faces-redirect=true";

	}

	public String doRetrieveSavings() {
		Customer c1 = customerServiceLocal.findCustomerById(user.getId());
		if ((c1.getBalance() > user.getBalance()) && (user.getBalance() != 0)) {
			operation.setOperationDate(new Date());
			operation.setCustomer(user);
			operation.setAmount(c1.getBalance());
			operation.setState("Retrieve");
			operationServiceLocal.addOrUpdateBalanceAccount(operation);
			customerServiceLocal.findCustomerById(user.getId());
			Float nbr = c1.getBalance();
			nbr -= user.getBalance();
			user.setBalance(nbr);
			customerServiceLocal.addAccount(user);
			doSendPDFFileViaEmail();
		} else {
			return "/pages/customerHome/ErreurLogin?faces-redirect=true";

		}

		return "";
	}

	public Customer getUser() {
		return user;
	}

	public void setUser(Customer user) {
		this.user = user;
	}

	public Customer getUser2() {
		return user2;
	}

	public void setUser2(Customer user2) {
		this.user2 = user2;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	// Send mail

	public List<Operation> getOperations() {
		operations = operationServiceLocal.findAllOperationsById(user.getId());
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	public String doSendPDFFileViaEmail() {

		try {
			writeStream(user.getAccountNumber(), user.getFisrtName() + " " + user.getLastName(), user.getBalance(),
					operation.getAmount());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			writePdf(user.getEmail());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	public void writeStream(Integer accountNumber, String firstName, Float solde, Float retrait) throws IOException {
		Writer writer = new FileWriter("C:\\Users\\Public\\file.txt");

		try {
			writer.write("Client:  " + firstName + "\n" + "Numéro du compte:  " + accountNumber + "\n"
					+ "Solde actuelle:   " + solde + "\n" + "Le montant de retrait:   " + retrait);
		} finally {
			writer.close();
		}
	}

	public void writePdf(String to) throws Exception {

		// Sender's email ID needs to be mentioned
		String from = "application.bank1@gmail.com";

		final String username = "applicationbank1";// change accordingly
		final String password = "azerty123.";// change accordingly

		// Assuming you are sending email through relay.jangosmtp.net
		String host = "smtp.gmail.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject("Retrieve savings");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText(
					"Bonjour cher client, /n vous trouverez ci joint un document de retrait de votre solde. /n Cordialement.");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			MimeMultipart cover = new MimeMultipart("alternative");
			MimeBodyPart text = new MimeBodyPart();
			cover.addBodyPart(text);

			messageBodyPart.setContent(cover);
			String filename = "C:/Users/Public/file.txt";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			messageBodyPart.setDescription("jjj");
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart);

			// Send message
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

}
