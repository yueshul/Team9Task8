package mf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.formbeanfactory.FormBeanFactory;
import org.formbeanfactory.FormBeanFactoryException;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;

import mf.databean.CustomerBean;
import mf.databean.EmployeeBean;
import mf.databean.FundBean;
import mf.databean.FundPriceHistoryBean;
import mf.databean.PositionBean;
import mf.databean.TransactionBean;
import mf.formbean.LoginForm;
import mf.model.CustomerDAO;
import mf.model.EmployeeDAO;
import mf.model.FundDAO;
import mf.model.FundPriceHistoryDAO;
import mf.model.Model;
import mf.model.PositionDAO;
import mf.model.TransactionDAO;

public class LoginAction extends Action {
	private FormBeanFactory<LoginForm> formBeanFactory = new FormBeanFactory<>(LoginForm.class);
	private EmployeeDAO employeeDAO;
	private CustomerDAO customerDAO;
	private PositionDAO positionDAO;
	private TransactionDAO transactionDAO;
	private FundDAO fundDAO;
	private FundPriceHistoryDAO fundPriceHistoryDAO;

	public LoginAction(Model model) {
		employeeDAO = model.getEmployeeDAO();
		customerDAO = model.getCustomerDAO();
		positionDAO = model.getPositionDAO();
		transactionDAO = model.getTransactionDAO();
		fundDAO = model.getFundDAO();
		fundPriceHistoryDAO = model.getFundPriceHistoryDAO();

	}

	@Override
	public String getName() {
		return "login.do";
	}

	public String performGet(HttpServletRequest request) {
		// If user is already logged in, redirect to todolist.do
		HttpSession session = request.getSession();

		if (session.getAttribute("employee") != null) {
			// need to create new .do servlet
			return "employee_homepage.jsp";
		}

		if (session.getAttribute("customer") != null) {
		    try {
		        CustomerBean customer = (CustomerBean) request.getSession().getAttribute("customer");
	            PositionBean[] position= positionDAO.getPositionsByCustomer(customer.getUserName());
	            if (position != null && position.length != 0) {
	                FundBean bean = fundDAO.read(position[0].getFundId());
	                if (bean != null) {
	                    FundPriceHistoryBean[] tmp = fundPriceHistoryDAO
	                            .match(MatchArg.equals("fundId", bean.getFundId()));
	                    if (tmp != null && tmp.length != 0) {
	                        request.setAttribute("date", tmp[tmp.length - 1].getPriceDate());
	                    }
	                    Map<FundBean, Double> map = new HashMap<FundBean, Double>();
	                    for (PositionBean p : position) {
	                        map.put(fundDAO.read(p.getFundId()), p.getShares());
	                    }
	                    request.setAttribute("funds", map);
	                }
	            }
	            TransactionBean[] transactions = transactionDAO.getTransactionByCustomer(customer.getUserName());
	            request.setAttribute("transactions", transactions);
	            return "customer_homepage.jsp";
		    }catch (RollbackException e) {
	            e.printStackTrace();
	            request.setAttribute("errors", e.getMessage());
	            return "error.jsp";
	        }
		}

		// Otherwise, just display the login page.
		request.setAttribute("form", new LoginForm());

		return "newlogin.jsp";

	}

	public String performPost(HttpServletRequest request) {

		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		// If user is already logged in, redirect to todolist.do

		HttpSession session = request.getSession();

		try {
			LoginForm form = formBeanFactory.create(request);

			request.setAttribute("form", form);

			if (form.hasValidationErrors()) {
				request.setAttribute("errors", errors);
				request.setAttribute("form", form);
				return "newlogin.jsp";
			}
			String userName = request.getParameter("userName");
			String type = request.getParameter("type");
			System.out.println(userName + " log in with this above username");
			EmployeeBean employee = null;
			CustomerBean customer = null;
			if(type.equals("Customer")) {
			    customer = customerDAO.read(userName);
			}else if(type.equals("Employee")) {
			    employee = employeeDAO.read(userName);
			}

			if (employee != null) {
				if (employee.getPassword().equals(form.getPassword())) {
					session.setAttribute("employee", employee);
					return "employee_homepage.jsp";
				} else {
					form.addFieldError("password", "Incorrect password");
					return "newlogin.jsp";
				}
			} else if (customer != null) {
				if (customer.getPassword().equals(form.getPassword())) {
					
					PositionBean[] position= positionDAO.getPositionsByCustomer(customer.getUserName());
					if (position != null && position.length != 0) {

						FundBean bean = fundDAO.read(position[0].getFundId());
						if (bean != null) {

							FundPriceHistoryBean[] tmp = fundPriceHistoryDAO
									.match(MatchArg.equals("fundId", bean.getFundId()));
							if (tmp != null && tmp.length != 0) {
								request.setAttribute("date", tmp[tmp.length - 1].getPriceDate());
							}
							Map<FundBean, Double> map = new HashMap<FundBean, Double>();
							for (PositionBean p : position) {
								map.put(fundDAO.read(p.getFundId()), p.getShares());
							}
							request.setAttribute("funds", map);

						}
					}
					session.setAttribute("customer", customer);

					TransactionBean[] transactions = transactionDAO.getTransactionByCustomer(customer.getUserName());
					System.out.println("len:" + transactions.length);
					request.setAttribute("transactions", transactions);
					return "customer_homepage.jsp";
				} else {
					form.addFieldError("password", "Incorrect password");
					return "newlogin.jsp";
				}
			} else {
				form.addFieldError("userName", "UserName not found");
				return "newlogin.jsp";
			}

		} catch (RollbackException | FormBeanFactoryException e) {
			e.printStackTrace();
			request.setAttribute("errors", e.getMessage());
			return "error.jsp";
		}
	}
}
