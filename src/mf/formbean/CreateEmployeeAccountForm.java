package mf.formbean;

import java.util.ArrayList;
import java.util.List;

import org.formbeanfactory.FieldOrder;
import org.formbeanfactory.FormBean;

@FieldOrder("userName, password, firstName, lastName")
public class CreateEmployeeAccountForm extends FormBean {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    
    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();

        if (userName== null || userName.length() == 0)
            errors.add("User Name is required");
        if (password == null || password.length() == 0)
            errors.add("Password is required");
        if (userName.matches(".*[<>\"].*")) 
            errors.add("User Name may not contain angle brackets or quotes");
        if (password.matches(".*[<>\"].*")) 
            errors.add("User Name may not contain angle brackets or quotes");

        if (errors.size() > 0)
            return errors;

        return errors;
    }
}
