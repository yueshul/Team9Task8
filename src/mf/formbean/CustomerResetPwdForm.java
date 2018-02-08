package mf.formbean;

import java.util.ArrayList;
import java.util.List;

import org.formbeanfactory.FieldOrder;
import org.formbeanfactory.FormBean;
import org.formbeanfactory.InputType;

@FieldOrder("userName, firstName, lastName,emailAddress")
public class CustomerResetPwdForm extends FormBean {

	private String emailAddress;
	private String userName;
	private String firstName;
	private String lastName;
	private String action;
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@InputType("button")
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getAction() {
		
		return action;
	}

	
	
	

	public void Validate() {
		super.validate();
		if (super.hasValidationErrors()) {
			return;
		}

		if (!action.equals("ResetPwd") || !action.equals("Cancel")) {
			this.addFormError("Invalid Button");
		}
		if (userName.matches(".*[<>\"].*")) {
			this.addFieldError("userName", "May not contain angle brackets or quotes");
		}
		if (userName== null || userName.length() == 0)
            this.addFieldError("userName", "User Name is required");
        if (firstName.matches(".*[<>\"].*")) 
            this.addFieldError("firstName","FistName may not contain angle brackets or quotes");
        if (firstName== null || firstName.length() == 0)
            this.addFieldError("firstName", "FirstName is required");
        if (lastName.matches(".*[<>\"].*")) 
            this.addFieldError("lastName","LastName may not contain angle brackets or quotes");
        if (lastName== null || lastName.length() == 0)
            this.addFieldError("lastName", "Last Name is required");
//        if (firstName.matches(".*[<>\"].*")) 
//            this.addFieldError("firstName","FistName may not contain angle brackets or quotes");
        if (emailAddress== null || emailAddress.length() == 0)
            this.addFieldError("emailAddress", "EmailAddress is required");
        if (emailAddress.matches(".*[<>\"].*")) 
            this.addFieldError("emailAddress","EmailAddress may not contain angle brackets or quotes");
		
		
	}
//		List<String> errors = new ArrayList<String>();
//		
//		if (oldPassword == null || oldPassword.length() == 0) errors.add("Your old Password is required");
//		if (newPassword == null || newPassword.length() == 0) errors.add("New Password is required");
//		if (confirmPassword == null || confirmPassword.length() == 0) {
//			errors.add("Confirm Pwd is required");
//		} else if (!newPassword.equals(confirmPassword)){
//			errors.add("Passwords do not match");
//		}
//	
//
//	
//		
//		//if (errors.size() > 0) return errors;
//				return errors;
//			}
//
//
//			
//		}

}
