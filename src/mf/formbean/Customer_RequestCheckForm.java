package mf.formbean;

import org.formbeanfactory.FieldOrder;
import org.formbeanfactory.FormBean;
import org.formbeanfactory.InputType;

@FieldOrder("checkAmount")
public class Customer_RequestCheckForm extends FormBean{
	private String action;
	private String checkAmount;
	//private String requestDate;
	
	
	public String getAction() {
		return action;
	}
	@InputType("button")
	public void setAction(String action) {
		this.action = action;
	}
	public String getCheckAmount() {
		return checkAmount;
	}
	public void setCheckAmount(String checkAmount) {
		this.checkAmount = checkAmount;
	}
	
//	public String getRequestDate() {
//		return requestDate;
//	}
//	
//	public void setRequestDate(String requestDate) {
//		this.requestDate = requestDate;
//	}
	
	public void validate() {
		super.validate();
		if (hasValidationErrors()) {
			return;
		}
		if (!action.equals("SubmitRequest")) {
			this.addFormError("Invalid Button");
		}
//		if (checkAmount < 0) {
//			this.addFormError("Invalid Amount");
//		}
		String[] nums = this.checkAmount.split("\\.");
		if (nums.length != 1 && nums.length != 2 && nums.length != 0) {
			this.addFormError("Invalid amount");
			return;
		} else if (nums.length > 0){
			if (nums[0].length() > 5) {
				this.addFormError("Invalid amount");
				return;
			}
			if (nums.length == 2 && nums[1].length() > 5) {
				this.addFormError("Invalid amount");
				return;
			}
		} else if (nums.length == 0 && this.checkAmount.length() > 5) {
			this.addFormError("Invalid amount");
			return;
		}
		
	}
}
