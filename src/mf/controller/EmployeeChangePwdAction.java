
package mf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.formbeanfactory.FormBeanFactory;
import org.formbeanfactory.FormBeanFactoryException;
import org.genericdao.RollbackException;

import mf.databean.CustomerBean;
import mf.databean.EmployeeBean;
import mf.formbean.ChangePwdForm;
import mf.formbean.LoginForm;
import mf.model.CustomerDAO;
import mf.model.EmployeeDAO;
import mf.model.Model;


public class EmployeeChangePwdAction extends Action {
	private FormBeanFactory<ChangePwdForm> formBeanFactory = new FormBeanFactory<>(ChangePwdForm.class);
//	private static final long serialVersionUID = 1L;
	
	
	private EmployeeDAO employeeDAO;

	public EmployeeChangePwdAction(Model model) {
		employeeDAO = model.getEmployeeDAO();
	}

	public String getName() { return "employee_change_pwd.do"; }

	public String performGet(HttpServletRequest request) {
        // If user is already logged in, redirect to todolist.do
        HttpSession session = request.getSession();
        if (session.getAttribute("employee") == null) {
            return "login.do";
        }

        // Otherwise, just display the login page.
        request.setAttribute("form", new ChangePwdForm());
        return "employee_changePassword.jsp";
    }
        
	public String performPost(HttpServletRequest request) {
		// Set up error list
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors",errors);
		
		HttpSession session = request.getSession();
		EmployeeBean employeeBean = (EmployeeBean) request.getSession().getAttribute("employee");
		
		if(employeeBean==null) {
			request.setAttribute("form",new LoginForm());
			return "newlogin.jsp";
			
		}
		
		try {
			ChangePwdForm form = formBeanFactory.create(request);
			if (form.hasValidationErrors()) {
				form.getValidationErrors();
				request.setAttribute("form", form);
				//request.setAttribute("errors", errors);
				return "employee_changePassword.jsp";
			}
//			errors.addAll(form.getValidationErrors());
//			if (errors.size() != 0) {
//				 request.setAttribute("form", form);
//				request.setAttribute("errors", errors);
//				return "employee_changePassword.jsp";
//			}
			// Load the form parameters into a form bean
			
			
			if (!employeeBean.getPassword().equals(form.getOldPassword())) {
				form.addFormError("Incorrect password");
				request.setAttribute("form", form);
				//request.setAttribute("errors", errors);
				return "employee_changePassword.jsp";
			}
			if(!form.getConfirmPassword().equals(form.getNewPassword())) {
				form.addFormError("Password does not match");
				//errors.add("Please insert a different password");
				request.setAttribute("form", form);
				request.setAttribute("errors", errors);
				return "employee_changePassword.jsp";
			}
			
			if(employeeBean.getPassword().equals(form.getNewPassword())) {
				form.addFormError("Please insert a different password");
				request.setAttribute("form", form);
				//request.setAttribute("errors", errors);
				return "employee_changePassword.jsp";
			}
			
			
			// Change the password
			//userDAO.setPassword(userBean.getUserEmail(),form.getNewPassword());
			employeeBean.setPassword(form.getNewPassword());
			employeeDAO.update(employeeBean);

			request.setAttribute("message","Password changed! Please log in now! ");
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
