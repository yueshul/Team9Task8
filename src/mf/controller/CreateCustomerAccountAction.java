package mf.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.formbeanfactory.FormBeanFactory;
import org.formbeanfactory.FormBeanFactoryException;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import mf.databean.CustomerBean;
import mf.formbean.CreateCustomerAccountForm;
import mf.model.CustomerDAO;
import mf.model.EmployeeDAO;
import mf.model.Model;

public class CreateCustomerAccountAction extends Action{
    private FormBeanFactory<CreateCustomerAccountForm> formBeanFactory = new FormBeanFactory<>(CreateCustomerAccountForm.class);
    private CustomerDAO customerDAO;
    private EmployeeDAO employeeDAO;
    
    public CreateCustomerAccountAction(Model model) {
        customerDAO = model.getCustomerDAO();
        employeeDAO = model.getEmployeeDAO();
    }

    public String getName() {
        return "CreateCustomerAccount.do";
    }

    public String performGet(HttpServletRequest request) {
    	List<String> errors = new ArrayList<String>();
    	try {
    		CreateCustomerAccountForm form = formBeanFactory.create(request);
        request.setAttribute("form", form);
        return "employee_createCustomerAccount.jsp";
    	} catch (FormBeanFactoryException e) {
            errors.add(e.getMessage());
            request.setAttribute("errors", errors);
            return "error.jsp";

        }
    }

    public String performPost(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("employee") == null) {
            return "login.do";
        }

        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);

        try {
            CreateCustomerAccountForm form = formBeanFactory.create(request);
            request.setAttribute("form", form);
            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                return "employee_createCustomerAccount.jsp";
            }
            Transaction.begin();
            if (customerDAO.read(form.getUserName()) != null || employeeDAO.read(form.getUserName()) != null) {
                errors.add("User name already exists, please try another one");
                request.setAttribute("errors", errors);
                return "employee_createCustomerAccount.jsp";
            }
            double cash = Double.parseDouble(form.getCash());
            if ((""+cash).length()>18 ) {
                errors.add("Cash amount it too big");
                request.setAttribute("errors", errors);
                return "employee_createCustomerAccount.jsp";
            }
            CustomerBean customer = new CustomerBean();
            customer.setFirstName(form.getFirstName());
            customer.setLastName(form.getLastName());
            customer.setUserName(form.getUserName());
            customer.setCash(cash);
            customer.setEmail(form.getEmail());
            customer.setState(form.getState());
            customer.setPassword(form.getPassword());
            customer.setAddressLine1(form.getAddressLine1());
            customer.setAddressLine2(form.getAddressLine2());
            customerDAO.create(customer);
            Transaction.commit();
            request.setAttribute("message", "Successfully created a customer!");
            return "employee_createCustomerAccount.jsp";
        } catch (RollbackException e) {
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
