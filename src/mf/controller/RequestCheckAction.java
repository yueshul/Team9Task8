package mf.controller;

import mf.databean.CustomerBean;
import mf.databean.TransactionBean;
import mf.formbean.Customer_RequestCheckForm;
import mf.model.CustomerDAO;
import mf.model.Model;
import mf.model.TransactionDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.formbeanfactory.FormBeanFactory;
import org.formbeanfactory.FormBeanFactoryException;
import org.genericdao.DuplicateKeyException;
import org.genericdao.RollbackException;

public class RequestCheckAction extends Action{

		private FormBeanFactory<Customer_RequestCheckForm> formBeanFactory = new FormBeanFactory<>(Customer_RequestCheckForm.class);
		private CustomerDAO customerDAO;
		private TransactionDAO transactionDAO;

		public RequestCheckAction(Model model) {
			customerDAO = model.getCustomerDAO();
			transactionDAO = model.getTransactionDAO();
		}

		@Override
		public String getName() {
			return "requestCheck.do";
		}

		public String performGet(HttpServletRequest request) {
			HttpSession session = request.getSession();
			CustomerBean customerBean = (CustomerBean) session.getAttribute("customer");
			
			if (customerBean == null) {
				return "newlogin.jsp";
			}
			
				try {
				TransactionBean[] transations = transactionDAO.getTransactionByCustomer(customerBean.getUserName());
				
				Customer_RequestCheckForm form = new Customer_RequestCheckForm();
				//long balanceBeforeRequest = userBean.getBalance();
				request.setAttribute("form", form);
				request.setAttribute("transactions", transations);
				return "customer_requestCheck.jsp";
				
				} catch (RollbackException e) {
					
					e.printStackTrace();
					request.setAttribute("errors", e.getMessage());
					return "error.jsp";
				}
			
		}

		public String performPost(HttpServletRequest request) {
			List<String> errors = new ArrayList<String>();
			request.setAttribute("errors",errors);
			
			System.out.println("In AddAction - PerformPost...");
			HttpSession session = request.getSession();
			CustomerBean customerBean = (CustomerBean) session.getAttribute("customer");
			
			if (customerBean == null) {
				return "newlogin.jsp";
			}
			try {
			Customer_RequestCheckForm form = formBeanFactory.create(request);
				
			if (form.hasValidationErrors()) {
				//errors.add("empty amount");
				//request.setAttribute("errors", errors);
				request.setAttribute("form", form);
				request.setAttribute("transactions", transactionDAO.getTransactionByCustomer(customerBean.getUserName()));
				return "customer_requestCheck.jsp";
            }
			
			double cashBeforeRequest = customerBean.getCash();
			double requestAmount = 0;
			
			try {
			String amount = form.getCheckAmount();
			if (amount != null) {
				requestAmount = Double.parseDouble(form.getCheckAmount());
				if (requestAmount < 0) {
					errors.add("Invalid amount");
					request.setAttribute("errors", errors);
					request.setAttribute("form", form);
					request.setAttribute("transactions", transactionDAO.getTransactionByCustomer(customerBean.getUserName()));
					return "customer_requestCheck.jsp";
				}
			} else {
//				System.out.println("empty amount llllllllllllls");
				errors.add("Invalid amount");
				//request.setAttribute("errors", errors);
				request.setAttribute("form", form);
				request.setAttribute("transactions", transactionDAO.getTransactionByCustomer(customerBean.getUserName()));
				return "customer_requestCheck.jsp";
			}
				} catch (NumberFormatException e) {
					errors.add("please input a valid amount");
					request.setAttribute("errors", errors);
					request.setAttribute("form", form);
					request.setAttribute("transactions", transactionDAO.getTransactionByCustomer(customerBean.getUserName()));
					return "customer_requestCheck.jsp";
			}
			
			if (requestAmount >= cashBeforeRequest) {
				errors.add(" The requested amount exceeds your balance, talk to your manager about our loan policy");
				request.setAttribute("errors", errors);
				request.setAttribute("form", form);
				request.setAttribute("transactions", transactionDAO.getTransactionByCustomer(customerBean.getUserName()));
				return "customer_requestCheck.jsp";
			} 
			if (requestAmount <= 0) {
				errors.add(" please input a valid amount");
				request.setAttribute("errors", errors);
				request.setAttribute("form", form);
				request.setAttribute("transactions", transactionDAO.getTransactionByCustomer(customerBean.getUserName()));
				return "customer_requestCheck.jsp";
			} 
			
			TransactionBean transactionBean = new TransactionBean();
			transactionBean.setUserName(customerBean.getUserName());
			transactionBean.setTransactionType("Request Check");
			//transactionBean.setExecuteDate(new Date());
			transactionBean.setAmount(requestAmount);
			transactionBean.setStatus("Pending");
			
			
			try {
	                transactionDAO.create(transactionBean);
//	                customerBean.setCash(cashBeforeRequest - requestAmount);
//	                customerDAO.update(customerBean);
	           
			} catch (DuplicateKeyException e) {
	            		request.setAttribute("errors", "Duplicate transaction");
	                return "error.jsp";
	            }

			request.setAttribute("message","you have successfully submitted the request");
            request.setAttribute("form", new Customer_RequestCheckForm());
            request.setAttribute("transactions", transactionDAO.getTransactionByCustomer(customerBean.getUserName()));
            return "customer_requestCheck.jsp";
			
			
			
			} catch (RollbackException | FormBeanFactoryException e) {
				e.printStackTrace();
				request.setAttribute("errors", e.getMessage());
				return "error.jsp";
			}	

	}
}
