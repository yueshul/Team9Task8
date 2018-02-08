package mf.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.formbeanfactory.FormBeanFactory;
import org.formbeanfactory.FormBeanFactoryException;
import org.genericdao.RollbackException;

import mf.databean.CustomerBean;
import mf.databean.EmployeeBean;
import mf.formbean.CustomerResetPwdForm;
import mf.formbean.LoginForm;
import mf.model.CustomerDAO;
import mf.model.EmployeeDAO;
import mf.model.Model;

public class CustomerResetPwdAction extends Action{
	private FormBeanFactory<CustomerResetPwdForm> formBeanFactory = new FormBeanFactory<>(CustomerResetPwdForm.class); 	
	private CustomerDAO customerDAO;
	private EmployeeDAO employeeDAO;
	    
	    public CustomerResetPwdAction(Model model) {
	        customerDAO = model.getCustomerDAO();
	        employeeDAO = model.getEmployeeDAO();
	    }

	    public String getName() {
	        return "customerResetPwd.do";
	    }
	    public static String genRandomNum(int pwd_len) {  
        	     // 35=26+10  
        	     final int maxNum = 36;  
        	     int i; // random number  
        	     int count = 0; // the length of password
        	     char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',  
        	     'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',  
        	     'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };  
        	     StringBuffer pwd = new StringBuffer("");  
        	     Random r = new Random();  
        	     while (count < pwd_len) {  
        	     // generate random number
        	     i = Math.abs(r.nextInt(maxNum)); //max possible result: 36-1
        	     if (i >= 0 && i < str.length) {  
        	     pwd.append(str[i]);  
        	     count++;  
        	     }  
        	     }  
        	     return pwd.toString();  
	     }  
	   
	    public String performGet(HttpServletRequest request) {
	        HttpSession session = request.getSession();
	        List<String> errors = new ArrayList<String>();
	        request.setAttribute("errors", errors);
	        if (session.getAttribute("customer") != null) {
	            return "logout.do";
	        }     
	        
	        request.setAttribute("form", new CustomerResetPwdForm());
	        return "customer_resetPwd.jsp";
	    }
	    
	    public String performPost(HttpServletRequest request) {
	        HttpSession session = request.getSession();
	        List<String> errors = new ArrayList<String>();
	        request.setAttribute("errors", errors);
	        if (session.getAttribute("customer") != null) {
	            return "logout.do";
	        }
	        
	        try {
	        	
	        	CustomerResetPwdForm form = formBeanFactory.create(request);
	         
	     if (request.getParameter("action").equals("Cancel")) {
	        	request.setAttribute("form", new LoginForm());
        		return "newlogin.jsp";
	     }	else if (request.getParameter("action").equalsIgnoreCase("ResetPwd")) {
	        	
	        	 if (form.hasValidationErrors() || errors.size() != 0) {
		        		form.validate();
		        		request.setAttribute("form", form);
					request.setAttribute("errors", errors);
					return "customer_resetPwd.jsp";
		        }
			 	
	        	 	String userName = form.getUserName();
	        		CustomerBean customer = customerDAO.read(userName);
	        		EmployeeBean employee = employeeDAO.read(userName);
	        		
	        		if(customer == null && employee == null) {
	        			form.addFieldError("userName", "user name not found");
	        			request.setAttribute("form", form);
	        			return "customer_resetPwd.jsp";
	        		}
	        		
	        		String firstName = form.getFirstName();
	        		if (customer != null && (!customer.getFirstName().equalsIgnoreCase(firstName))){
	        			form.addFieldError("firstName", "First name not found");
	        			request.setAttribute("form", form);
	        			return "customer_resetPwd.jsp";
	        		}
	        		if (employee != null && (!employee.getFirstName().equalsIgnoreCase(firstName))){
	        			form.addFieldError("firstName", "First name not found");
	        			request.setAttribute("form", form);
	        			return "customer_resetPwd.jsp";
	        		}
	        		
	        		String lastName = form.getLastName();
	        		if (customer != null && (!customer.getLastName().equalsIgnoreCase(lastName))){
	        			form.addFieldError("lastName", "Last name not found");
	        			request.setAttribute("form", form);
	        			return "customer_resetPwd.jsp";
	        		}
	        		if (employee != null && (!employee.getLastName().equalsIgnoreCase(lastName))){
	        			form.addFieldError("lastName", "Last name not found");
	        			request.setAttribute("form", form);
	        			return "customer_resetPwd.jsp";
	        		}
	        		
	        		if(customer != null && employee == null) {
	        		    String newPassword = genRandomNum(10);
	        		    String email = customer.getEmail();
	        		    customer.setPassword(newPassword);
	        		    customerDAO.update(customer);
                    request.setAttribute("message", "your password is reset, check your email for the default password");
                    request.setAttribute("form", new LoginForm());
	        		    EmployeeSendEmail.sendResetPasswordEmail(newPassword, email);
        	        		return "newlogin.jsp";
        	        		//return "login.do";
	        	
	        		}
	        		else {
	        		    String newPassword = genRandomNum(10);
	        		    String email = customer.getEmail();
	        			employee.setPassword(newPassword);
	        			employeeDAO.update(employee);
	        			request.setAttribute("message", "your password is reset, check your email for the default password");
		        		request.setAttribute("form", new LoginForm());
		        		EmployeeSendEmail.sendResetPasswordEmail(newPassword, email);
		        		return "newlogin.jsp";
		        	}
	         		
	        } else {
        		errors.add("invalid action");
        		request.setAttribute("errors", errors);
        		request.setAttribute("form", new CustomerResetPwdForm());
        		return "customer_resetPwd.jsp";
        }
	     	
			
	        		
	    } catch (RollbackException | FormBeanFactoryException e) {
			e.printStackTrace();
			request.setAttribute("errors", e.getMessage());
			return "error.jsp";
		}	
	}
}


