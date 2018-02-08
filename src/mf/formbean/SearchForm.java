package mf.formbean;

import java.util.ArrayList;
import java.util.List;

import org.formbeanfactory.FieldOrder;
import org.formbeanfactory.FormBean;
import org.formbeanfactory.InputType;

@FieldOrder("name,symbol")
public class SearchForm extends FormBean {
	private String name;
	private String symbol;

	private String action;

	@InputType("button")
	public void setAction(String s) {
		action = s;
	}

	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	@InputType("text")
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@InputType("text")
	public void setName(String name) {
		this.name = name;
	}

	public String getAction() {
		return action;
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		if (!action.equals("Search")) {
			errors.add("Invalid button");
		}
		if (symbol.matches(".*[<>\"].*") || name.matches(".*[<>\"].*")) {
			errors.add("Invalid Search Keyword!");
		}

		if (symbol.length()==0&&name.length()==0) {
			errors.add("Please Enter Search Keyword!");
		}
		
		return errors;
	}
}

//
// public void validate() {
// super.validate();
// if (hasValidationErrors()) {
// return;
// }
// if (!action.equals("Search")) {
// this.addFormError("Invalid Button");
// }
// if (symbol.matches(".*[<>\"].*")||name.matches(".*[<>\"].*")) {
// this.addFieldError("keyword", "May not contain angle brackets or quotes");
// }
//
// }
// }
