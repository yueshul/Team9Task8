package mf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.formbeanfactory.FormBeanFactory;
import org.formbeanfactory.FormBeanFactoryException;
import org.genericdao.DuplicateKeyException;
import org.genericdao.RollbackException;

import mf.databean.CustomerBean;
import mf.databean.EmployeeBean;
import mf.databean.TransactionBean;
import mf.formbean.Employee_DepositCheckForm;
import mf.formbean.LoginForm;
import mf.model.CustomerDAO;
import mf.model.Model;
import mf.model.TransactionDAO;

public class Employee_DepositCheckAction extends Action {

	private FormBeanFactory<Employee_DepositCheckForm> formBeanFactory = new FormBeanFactory<>(Employee_DepositCheckForm.class);
	//private EmployeeDAO employeeDAO;
	private CustomerDAO customerDAO;
	private TransactionDAO transactionDAO;

	public Employee_DepositCheckAction(Model model) {
		customerDAO = model.getCustomerDAO();
		transactionDAO = model.getTransactionDAO();
		//employeeDAO = model.getEmployeeDAO();
	}

	@Override
	public String getName() {
		return "depositCheck.do";
	}

	public String performGet(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String customerName = (String) request.getParameter("customerName");
		try {
		    if(customerName!=null) {
		        CustomerBean customer = customerDAO.read(customerName);
	            if(customer!=null) {
	                request.setAttribute("customerName", customerName);
	            }
		    }
        } catch (RollbackException e) {
            e.printStackTrace();
        }
		EmployeeBean employeeBean = (EmployeeBean) session.getAttribute("employee");
		
		if (employeeBean == null) {
			return "newlogin.jsp";
		}
			Employee_DepositCheckForm form = new Employee_DepositCheckForm();
			request.setAttribute("form", form);
			request.setAttribute("customerName", customerName);
			return "employee_DepositCheck.jsp";
	}

	public String performPost(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors",errors);
		HttpSession session = request.getSession();
		EmployeeBean employeeBean = (EmployeeBean) session.getAttribute("employee");
		//String customerName = (String) request.getAttribute("customerName");
		String userName = request.getParameter("customerUserName");
		//HttpSession session = request.getSession();
		
		try {
			if (employeeBean == null) {
				//System.out.println("In AddAction - PerformPost. Deposit the check by employee.." + employeeBean.getFirstName());
//				System.out.println("employee bean is null!!!!!!!~~~~~~~");
				request.setAttribute("form", new LoginForm());
				return "newlogin.jsp";
			}
			
			Employee_DepositCheckForm form = formBeanFactory.create(request);
			System.out.print(form.getAction());
			System.out.print(form.getCheckAmount());
			System.out.print(form.getCustomerUserName());
			
		if (form.hasValidationErrors()) {
			//errors.add("invalid Input");
			request.setAttribute("form", form);
			//request.setAttribute("errors", errors);
			System.out.println("<<<<<<<<<<<<>>>>>>>>>>>>");
			System.out.println(userName);
			request.setAttribute("customerName", userName);
			return "employee_DepositCheck.jsp";
		}
		
		
		System.out.println("userName");
	//	int customerIdInt = Integer.parseInt(customerID);
		CustomerBean customerBean = customerDAO.read(userName);
		
		if (customerBean == null) {
			errors.add("No such customer");
			request.setAttribute("form", form);
			request.setAttribute("customerName", userName);
			request.setAttribute("errors", errors);
			return "employee_DepositCheck.jsp";
			
		}
		
		double cashBeforeRequest = customerBean.getCash();
		double depositAmount = 0;
		
		try {
			depositAmount = Double.parseDouble(form.getCheckAmount());
			} catch (NumberFormatException e) {
				errors.add("please input a valid amount");
				request.setAttribute("errors", errors);
				//request.setAttribute("transactions", transactionDAO.getTransactionByCustomer(customerBean.getUserName()));
				request.setAttribute("customerName", userName);
				request.setAttribute("form", form);
				return "employee_DepositCheck.jsp";
		}
		if (depositAmount <= 0 || depositAmount > 250000) {
			errors.add("Invalid amount, please check our policy for the valid amount");
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			request.setAttribute("customerName", userName);
			//request.setAttribute("transactions", transactionDAO.getTransactionByCustomer(customerBean.getUserName()));
			return "employee_DepositCheck.jsp";
		
		}
//		if (requestAmount >= cashBeforeRequest) {
//			errors.add(" The requested amount exceeds your balance");
//			request.setAttribute("errors", errors);
//			request.setAttribute("form", new Customer_RequestCheckForm());
//			return "employee_DepostiCheck.jsp";
//		} 
		
		TransactionBean transactionBean = new TransactionBean();
		transactionBean.setUserName(customerBean.getUserName());
		transactionBean.setTransactionType("Deposit Check");
		//transactionBean.setExecuteDate(new Date());
		transactionBean.setAmount(depositAmount);
		transactionBean.setStatus("Pending");
		//transactionBean.setShares(shares);

//		
		
		try {
                transactionDAO.create(transactionBean);
                //customerBean.setCash(cashBeforeRequest + depositAmount);
                //customerDAO.update(customerBean);
                System.out.println("create transaction bean");
        		
		} catch (DuplicateKeyException e) {
            		request.setAttribute("errors", "Duplicate transaction");
                return "error.jsp";
            }

		request.setAttribute("message","you have successfully submitted the request(deposit)");
        request.setAttribute("form", new Employee_DepositCheckForm());
        request.setAttribute("customerName", userName);
		request.setAttribute("transactions", transactionDAO.getTransactionByCustomer(customerBean.getUserName()));
		
        System.out.println("success");
    	
        return "employee_DepositCheck.jsp";
		
		
		
		} catch (RollbackException | FormBeanFactoryException e) {
			e.printStackTrace();
			request.setAttribute("errors", e.getMessage());
			return "error.jsp";
		}	

}
	
}
