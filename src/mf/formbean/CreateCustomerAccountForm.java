package mf.formbean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.formbeanfactory.FormBean;

public class CreateCustomerAccountForm extends FormBean{
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zip;
    private String cash;
    
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

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getCash() {
        return cash;
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

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();
        if (email == null || email.length() == 0)
            errors.add("email address is required");
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()) {
            errors.add("Email format wrong");
        }
        
        if (userName== null || userName.length() == 0)
            errors.add("User Name is required");
        if (userName.matches(".*[<>\"].*")) 
            errors.add("User Name may not contain angle brackets or quotes");
        if (password == null || password.length() == 0)
            errors.add("Password is required");
        if (password.matches(".*[<>\"].*") || firstName.matches(".*[<>\"].*") 
                || lastName.matches(".*[<>\"].*")
                || addressLine1.matches(".*[<>\"].*") || addressLine2.matches(".*[<>\"].*")
                || city.matches(".*[<>\"].*") || state.matches(".*[<>\"].*") || zip.matches(".*[<>\"].*")) 
            errors.add("Input should contain angle brackets or quotes");
        if (cash == null || cash.length() == 0) 
            errors.add("Cash is required");
        String[] nums = this.cash.split("\\.");
        if (nums.length != 1 && nums.length != 2 && nums.length != 0) {
            errors.add("Invalid cash");
        } else if (nums.length > 0){
            if (nums[0].length() > 11 || nums[0].charAt(0) == '-') {
                errors.add("Invalid cash");
            }
            if (nums.length == 2 && nums[1].length() > 5) {
                errors.add("Invalid cash");
            }
        } else if (nums.length == 0 && this.cash.length() > 11) {
            errors.add("Invalid cash");
        }
        double amount = 0;
        try {
            amount = Double.parseDouble(cash);
        } catch (NumberFormatException e) {
            errors.add("Cash must be numbers");
            return errors;
        }
        if (!(amount > 0)){
            errors.add("Amount should be positive");
            return errors;
        }
        
        DecimalFormat df = new DecimalFormat("0.00");
        cash = df.format(amount);

        return errors;
    }

}
