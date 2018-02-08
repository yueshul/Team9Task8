package mf.formbean;

import org.formbeanfactory.FieldOrder;
import org.formbeanfactory.FormBean;
import org.formbeanfactory.InputType;
import org.formbeanfactory.Label; 

@FieldOrder("symbol,amount")
public class BuyFundForm extends FormBean {
	private String symbol;
	private String amount;
	private String action;

	public String getAction() {
		return action;
	}

	public String getSymbol() {
		return symbol;
	}
	
	public String getAmount() {
		return amount;
	}
	
	public double getAmountAsDouble() {
		// The call validate() to ensures that errors will be detected before
		// NullPointerException or NumberFormatException are thrown!
		return Double.parseDouble(amount);
	}

	@InputType("button")
	public void setAction(String action) {
		this.action = action;
	}

	@Label("Ticker:")
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Label("Purchase Amount:")
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public void validate() {
		super.validate();

		if (hasValidationErrors()) {
			return;
		}
		String[] nums = this.amount.split("\\.");
		if (nums.length != 1 && nums.length != 2 && nums.length != 0) {
			this.addFieldError("amount","Invalid amount");
			return;
		} else if (nums.length > 0){
			if (nums[0].length() > 5) {
				this.addFieldError("amount", "Invalid amount");
				return;
			}
			if (nums.length == 2 && nums[1].length() > 5) {
				this.addFieldError("amount", "Invalid amount");
				return;
			}
		} else if (nums.length == 0 && this.amount.length() > 5) {
			this.addFieldError("amount", "Invalid amount");
			return;
		}
		

		if (!action.equals("buy")) {
			this.addFormError("Invalid action: " + action);
		}
		
//		if (symbol.matches(".*[<>\"].*")) {
//			this.addFieldError("post", "May not contain angle brackets or quotes");
//		}
//		
//		if (amount.matches(".*[<>\"].*")) {
//			this.addFieldError("post", "May not contain angle brackets or quotes");
//		}
		try {
			Double.parseDouble(amount);
		} catch (NumberFormatException e) {
			this.addFormError("Amount is not a double");
		}

	}
}

