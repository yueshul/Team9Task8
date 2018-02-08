package mf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import mf.databean.EmployeeBean;
import mf.model.EmployeeDAO;
import mf.model.Model;

public class ManageEmployeeAction extends Action{
    private EmployeeDAO employeeDAO;
    
    public ManageEmployeeAction(Model model) {
        employeeDAO = model.getEmployeeDAO();
    }

    public String getName() {
        return "ManageEmployee.do";
    }

    public String performGet(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);
        if (session.getAttribute("employee") == null) {
            return "login.do";
        }     
        try {
            EmployeeBean[] employees = employeeDAO.match();
            for(EmployeeBean cus:employees) {
                System.out.println(cus.getUserName());
            }
            System.out.println("employees:"+employees);
            request.setAttribute("employees", employees);
        } catch (RollbackException e1) {
            return "employee_manageEmployee.jsp";
        }
        if (request.getParameter("resetUserName") != null) {           
            try {
                Transaction.begin();
                EmployeeBean employee = employeeDAO.read(request.getParameter("resetUserName"));
                String newPassword = CustomerResetPwdAction.genRandomNum(10);                
                employee.setPassword(newPassword);
                employeeDAO.update(employee);  
                Transaction.commit();
                request.setAttribute("message", "Employee " + employee.getUserName() 
                        + "'s password has been reset to: " + newPassword);
                return "employee_manageEmployee.jsp";
            } catch (RollbackException e) {
                if (Transaction.isActive()) Transaction.rollback();
                errors.add("Wrong user request!");
                return "error.jsp";
            }
        }      
        return "employee_manageEmployee.jsp";
    }
}
