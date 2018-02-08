package mf.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;
import mf.databean.CustomerBean;
import mf.model.CustomerDAO;
import mf.model.Model;

public class ManageCustomerAction extends Action {
    private CustomerDAO customerDAO;
    
    public ManageCustomerAction(Model model) {
        customerDAO = model.getCustomerDAO();
    }

    public String getName() {
        return "ManageCustomer.do";
    }

    public String performGet(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);
        if (session.getAttribute("employee") == null) {
            return "login.do";
        }     
        try {
            CustomerBean[] customers = customerDAO.match();
            for(CustomerBean cus:customers) {
                System.out.println(cus.getUserName());
            }
            System.out.println("customers:"+customers);
            request.setAttribute("customers", customers);
        } catch (RollbackException e1) {
            return "employee_manageCustomer.jsp";
        }
        if (request.getParameter("resetUserName") != null) {           
            try {
                Transaction.begin();
                CustomerBean customer = customerDAO.read(request.getParameter("resetUserName"));
                String newPassword = CustomerResetPwdAction.genRandomNum(10);
                String email = customer.getEmail();
                
                customer.setPassword(newPassword);
                customerDAO.update(customer);
                Transaction.commit();
                EmployeeSendEmail.sendResetPasswordEmail(newPassword, email);        
                request.setAttribute("message", "Customer " + customer.getUserName() 
                        + "'s password has been reset to: " + newPassword);
                return "employee_manageCustomer.jsp";
            } catch (RollbackException e) {
                if (Transaction.isActive()) Transaction.rollback();
                errors.add("Wrong user request!");
                return "error.jsp";
            }
        }      
        return "employee_manageCustomer.jsp";
    }
}

