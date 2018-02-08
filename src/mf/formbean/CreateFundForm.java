package mf.formbean;

import java.util.ArrayList;
import java.util.List;

import org.formbeanfactory.FormBean;

public class CreateFundForm extends FormBean{
    private String name;
    private String symbol;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name.trim();
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol.trim();
    }
    
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();
        if (name== null || name.length() == 0) {
            errors.add("Fund name is required");
            return errors;
        }
        if (symbol == null || symbol.length() == 0){
            errors.add("Ticker name is required");
            return errors;
        }
        if (symbol.length()<1 || symbol.length()>5)
            errors.add("Ticker name can only consist 1 to 5 character");
        if (name.matches(".*[<>\"].*")) 
            errors.add("Name may not contain angle brackets or quotes");
        boolean problem = false;
        for(int i =0;i<symbol.length();i++){
            char ch = symbol.charAt(i);
            if((!(ch>='A' && ch<='Z')) && (!(ch >='a' && ch<='z'))){
                problem = true;
            }
        }
        if(problem)errors.add("Ticker can only contains characters [A-Z] and [a-z]");
        return errors;
    }
}

