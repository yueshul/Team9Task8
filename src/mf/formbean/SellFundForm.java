package mf.formbean;

import org.formbeanfactory.FieldOrder;
import org.formbeanfactory.FormBean;
import org.formbeanfactory.InputType;
import org.formbeanfactory.Label; 

@FieldOrder("symbol,shares")
public class SellFundForm extends FormBean {
	private String symbol;
	private String shares;
	private String action;

	public String getAction() {
		return action;
	}

	public String getSymbol() {
		return symbol;
	}
	
	public String getShares() {
		return shares;
	}
	
	public double getSharesAsDouble() {
		// The call validate() to ensures that errors will be detected before
		// NullPointerException or NumberFormatException are thrown!
		return Double.parseDouble(shares);
	}

	@InputType("button")
	public void setAction(String action) {
		this.action = action;
	}

	@Label("Ticker:")
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Label("Number of Shares:")
	public void setShares(String shares) {
		this.shares = shares;
	}
	
	public void validate() {
		super.validate();

		if (hasValidationErrors()) {
			return;
		}
		String[] nums = this.shares.split("\\.");
		if (nums.length != 1 && nums.length != 2 && nums.length != 0) {
			this.addFieldError("shares","Invalid number of shares");
			return;
		} else if (nums.length > 0){
			if (nums[0].length() > 5) {
				this.addFieldError("shares", "Invalid number of shares");
				return;
			}
			if (nums.length == 2 && nums[1].length() > 5) {
				this.addFieldError("shares", "Invalid number of shares");
				return;
			}
		} else if (nums.length == 0 && this.shares.length() > 5) {
			this.addFieldError("shares", "Invalid number of shares");
			return;
		}
		

		if (!action.equals("sell")) {
			this.addFormError("Invalid action: " + action);
		}
		
//		if (symbol.matches(".*[<>\"].*")) {
//			this.addFieldError("symbol", "May not contain angle brackets or quotes");
//			
//		}
//		
//		if (shares.matches(".*[<>\"].*")) {
//			this.addFieldError("shares", "May not contain angle brackets or quotes");
//		}
		try {
			Double.parseDouble(shares);
		} catch (NumberFormatException e) {
			this.addFormError("Shares is not a double");
		}

	}
}

