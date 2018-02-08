
package mf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.formbeanfactory.FormBeanFactory;
import org.formbeanfactory.FormBeanFactoryException;
import org.genericdao.RollbackException;

import mf.databean.CustomerBean;
import mf.formbean.ChangePwdForm;
import mf.formbean.CustomerResetPwdForm;
import mf.formbean.LoginForm;
import mf.model.CustomerDAO;
import mf.model.Model;


public class CustomerChangePwdAction extends Action {
	private FormBeanFactory<ChangePwdForm> formBeanFactory = new FormBeanFactory<>(ChangePwdForm.class);
//	private static final long serialVersionUID = 1L;
	
	
	private CustomerDAO customerDAO;

	public CustomerChangePwdAction(Model model) {
		customerDAO = model.getCustomerDAO();
	}

	public String getName() { return "customer_change_pwd.do"; }

	public String performGet(HttpServletRequest request) {
        // If user is already logged in, redirect to todolist.do
        HttpSession session = request.getSession();
        if (session.getAttribute("customer") == null) {
        		request.setAttribute("form", new LoginForm());
            return "login.do";
        }

        request.setAttribute("form", new ChangePwdForm());
        return "customer_changePassword.jsp";
    }
        
	public String performPost(HttpServletRequest request) {
		// Set up error list
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors",errors);
		
		HttpSession session = request.getSession();
		CustomerBean customerBean = (CustomerBean) request.getSession().getAttribute("customer");
		
		if(customerBean==null) {
			request.setAttribute("form", new ChangePwdForm());
			return "newlogin.jsp";
		}
		
		try {
			ChangePwdForm form = formBeanFactory.create(request);
			if (form.hasValidationErrors()) {
				System.out.println("form is worng  ooooooooooooooo");
				
				form.getValidationErrors();
				request.setAttribute("form", form);
				//request.setAttribute("errors", errors);
				return "customer_changePassword.jsp";
			}
			
			
			
			// Load the form parameters into a form bean
			
			
			if (!customerBean.getPassword().equals(form.getOldPassword())) {
				//System.out.println("check pppppppppppp");
				//System.out.println(customerBean.getPassword());
				//System.out.println(form.getOldPassword());
				form.addFormError("Incorrect password");
				//errors.add("Incorrect password");
				request.setAttribute("form", form);
				//request.setAttribute("errors", errors);
				return "customer_changePassword.jsp";
			}
			
			if(!form.getConfirmPassword().equals(form.getNewPassword())) {
				form.addFormError("Password does not match");
				//errors.add("Please insert a different password");
				request.setAttribute("form", form);
				request.setAttribute("errors", errors);
				return "customer_changePassword.jsp";
			}
			if(customerBean.getPassword().equals(form.getNewPassword())) {
				form.addFormError("Please insert a different password");
				//errors.add("Please insert a different password");
				request.setAttribute("form", form);
				request.setAttribute("errors", errors);
				return "customer_changePassword.jsp";
			}
			
			
			
			
			

			// Change the password
			//userDAO.setPassword(userBean.getUserEmail(),form.getNewPassword());
			customerBean.setPassword(form.getNewPassword());
			customerDAO.update(customerBean);

			request.setAttribute("message","Password changed for "+customerBean.getUserName());
			request.setAttribute("form", new LoginForm());
			session.setAttribute("customer",null);
			return "newlogin.jsp";
		} catch (RollbackException  | FormBeanFactoryException e) {
			errors.add(e.toString());
			return "error.jsp";
//		} catch (FormBeanException e) {
//			errors.add(e.toString());
//			return "error.jsp";
//		}
		}
	}
}
