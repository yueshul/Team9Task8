package mf.formbean;

import java.util.ArrayList;
import java.util.List;

import org.formbeanfactory.FieldOrder;
import org.formbeanfactory.FormBean;
import org.formbeanfactory.InputType;

@FieldOrder("oldPassword, newPassword, confirmPassword")
public class ChangePwdForm extends FormBean{
	
		private String oldPassword;
		
		private String confirmPassword;
		private String newPassword;
		private String action;
		
		

		public String getOldPassword() {
			return oldPassword;
		}
		@InputType("password")
		public void setOldPassword(String oldPassword) {
			this.oldPassword = oldPassword;
		}

		@InputType("button")
		public void setAction(String action) {
			this.action = action;
		}
		
		public String getAction() {
			
			return action;
		}

		
		
		
		public String getConfirmPassword() { 
			return confirmPassword; 
			}
		
		
		public String getNewPassword() { 
		return newPassword;    
		}
		@InputType("password")
		public void setConfirmPassword(String s) { 
			confirmPassword = s.trim(); 
			}
		
		@InputType("password")
		public void setNewPassword(String s) { 
			newPassword = s.trim(); 
			}

		public void getValidationErrors() {
			super.validate();
			
			if (!action.equals("ChangePassword")) {
				this.addFormError("Invalid action: " + action);
				return;
			}
			
			if (oldPassword == null || oldPassword.length() == 0) {
				this.addFormError("Your old Password is required");
				return;
				
			}
			if (newPassword == null || newPassword.length() == 0) {
				this.addFormError("New Password is required");
				return;
				
			}
			if (confirmPassword == null || confirmPassword.length() == 0) {
				this.addFormError("Confirm Pwd is required");
				return;
				
			} else 
			if (!newPassword.equals(confirmPassword)){
				this.addFormError("Passwords do not match");
				return;
				
			}
			if (hasValidationErrors()) {
				return;
			}
		

		}


		
	}
