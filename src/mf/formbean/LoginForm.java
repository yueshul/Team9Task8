package mf.formbean;

import org.formbeanfactory.FieldOrder;
import org.formbeanfactory.FormBean;
import org.formbeanfactory.InputType;

@FieldOrder("type,userName, password")
public class LoginForm extends FormBean {
	private String userName;
	
	private String password;
	private String type;
	
	public String getType() {
		return type;
	}
	@InputType("type")
	public void setType(String type) {
		this.type = type;
	}

	private String action;

	@InputType("password")
	public void setPassword(String s) {
		password = s.trim();
	}

	@InputType("button")
	public void setAction(String s) {
		action = s;
	}

	public String getUserName() {
		return userName;
	}

	@InputType("text")
	public void setUserName(String userName) {
		this.userName = userName;
	}

//	
//	public void setEmailAddress(String emailAddress) {
//		this.emailAddress = emailAddress.trim();
//	}
//
//	public String getEmailAddress() {
//		return emailAddress;
//	}

	public String getPassword() {
		return password;
	}

	public String getAction() {
		return action;
	}

	public void validate() {
		super.validate();
		if (hasValidationErrors()) {
			return;
		}

		if (!action.equals("Login")) {
			this.addFormError("Invalid Button");
		}
		if (userName.matches(".*[<>\"].*")) {
			this.addFieldError("userName", "May not contain angle brackets or quotes");
		}

	}
}
