package mf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.formbeanfactory.FormBeanFactory;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;
import org.formbeanfactory.FormBeanFactoryException;
import mf.databean.EmployeeBean;
import mf.formbean.CreateEmployeeAccountForm;
import mf.model.CustomerDAO;
import mf.model.EmployeeDAO;
import mf.model.Model;

public class CreateEmployeeAccountAction extends Action{
    private FormBeanFactory<CreateEmployeeAccountForm> formBeanFactory = new FormBeanFactory<>(CreateEmployeeAccountForm.class);
    private CustomerDAO customerDAO;
    private EmployeeDAO employeeDAO;
    
    public CreateEmployeeAccountAction(Model model) {
        customerDAO = model.getCustomerDAO();
        employeeDAO = model.getEmployeeDAO();
    }

    public String getName() {
        return "CreateEmployeeAccount.do";
    }
    
    public String performGet(HttpServletRequest request) {
        	List<String> errors = new ArrayList<String>();
        	try {
        		CreateEmployeeAccountForm form = formBeanFactory.create(request);
            request.setAttribute("form", form);
            return "employee_createEmployeeAccount.jsp";
        	} catch (FormBeanFactoryException e) {
            errors.add(e.getMessage());
            request.setAttribute("errors", errors);
            return "error.jsp";
        }
    }
    public String performPost(HttpServletRequest request) {
        HttpSession session  = request.getSession();
        Object session_employee = session.getAttribute("employee");
        if(session_employee == null) return "employee_homepage.jsp";
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);
        try {
            CreateEmployeeAccountForm form = formBeanFactory.create(request);
            request.setAttribute("form", form);
            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                request.setAttribute("errors", errors);
            		return "employee_createEmployeeAccount.jsp";
            }
            Transaction.begin();
            if (customerDAO.read(form.getUserName()) != null || employeeDAO.read(form.getUserName()) != null) {
                errors.add("User name already exists");
                request.setAttribute("errors", errors);
                return "employee_createEmployeeAccount.jsp";
            }
            EmployeeBean employee = new EmployeeBean(form.getUserName(), form.getPassword(),form.getFirstName(), form.getLastName());          
            employeeDAO.create(employee);
            Transaction.commit();
            System.out.println("emp:"+employee);
            request.setAttribute("message", "Successfully created an employee!");
            return "employee_createEmployeeAccount.jsp";
        }catch (RollbackException e) {
            if (Transaction.isActive()) Transaction.rollback();
            errors.add(e.getMessage());
            request.setAttribute("errors", errors);
            return "error.jsp";
        } catch (FormBeanFactoryException e) {
            errors.add(e.getMessage());
            request.setAttribute("errors", errors);
            return "error.jsp";

        }
    }
}
