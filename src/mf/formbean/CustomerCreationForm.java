package mf.formbean;

import org.formbeanfactory.FieldOrder;
import org.formbeanfactory.FormBean;
import org.formbeanfactory.InputType;

@FieldOrder("emailAddress, firstName, lastName, password, address1, address2, city, state, zip, balance")
public class CustomerCreationForm extends FormBean {

	private String emailAddress;
	private String firstName;
	private String lastName;
	private String password;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String balance;
	private String action;

	public void setEmailAddress(String s) {
		emailAddress = s.trim();
	}

	@InputType("password")
	public void setPassword(String s) {
		password = s.trim();
	}

	@InputType("text")
	public void setFirstName(String s) {
		firstName = s.trim();
	}

	@InputType("text")
	public void setLastName(String lastName) {
		this.lastName = lastName.trim();
	}

	@InputType("button")
	public void setAction(String s) {
		action = s;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPassword() {
		return password;
	}

	public String getAction() {
		return action;
	}

	public boolean isPresent() {
		return action != null;
	}

	public String getAddress1() {
		return address1;
	}
	
	@InputType("text")
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}
	@InputType("text")
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}
	@InputType("text")
	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}
	@InputType("text")
	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}
	@InputType("text")
	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getBalance() {
		return balance;
	}
	@InputType("text")
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	public void validate() {
		super.validate();
		if (emailAddress == null || emailAddress.trim().length() == 0) {
			this.addFieldError("emailAddress", "Email Address is required");
		} else if (!emailAddress.matches(
				"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
			this.addFieldError("emailAddress", "Please enter valid email address in format x@y.com");

		}

		if (action == null) {
			this.addFormError("Action Button is missing");
		}

		if (hasValidationErrors()) {
			return;
		}

		if (!action.equals("Create Now!")) {
			this.addFormError("Invalid button");
		}

		if (firstName.matches(".*[<>\"].*")) {
			this.addFieldError("firstName", "May not contain angle brackets or quotes");
		}
		if (lastName.matches(".*[<>\"].*")) {
			this.addFieldError("lastName", "May not contain angle brackets or quotes");
		}
		if (password.matches(".*[<>\"].*")) {
			this.addFieldError("password", "May not contain angle brackets or quotes");
		}
	}
}


