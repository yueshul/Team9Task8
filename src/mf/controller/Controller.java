package mf.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;

import mf.databean.CustomerBean;
import mf.databean.EmployeeBean;
import mf.model.CustomerDAO;
import mf.model.EmployeeDAO;
import mf.model.Model;
import mf.rest.BuyFundAction;
import mf.rest.SellFundAction;

public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private EmployeeDAO employeeDAO;
	private CustomerDAO customerDAO;


	public void init() throws ServletException {
		// initiate the model and the action servlets

		System.out.println("In INti");
		Model model = new Model();
		
		employeeDAO = model.getEmployeeDAO();
		customerDAO = model.getCustomerDAO();

		Action.add(new LoginAction(model));
		Action.add(new LogoutAction(model));
		Action.add(new CustomerChangePwdAction(model));
		Action.add(new RequestCheckAction(model));
		Action.add(new Employee_DepositCheckAction(model));
		Action.add(new CreateEmployeeAccountAction(model));
		Action.add(new CreateCustomerAccountAction(model));
		Action.add(new CreateFundAction(model));
		Action.add(new ManageCustomerAction(model));
		Action.add(new EmployeeChangePwdAction(model));
		Action.add(new ViewAccountAction(model));
		Action.add(new ViewCustomerAction(model));
		Action.add(new ManageEmployeeAction(model));
		Action.add(new TransitionDayAction(model));
		Action.add(new CustomerResetPwdAction(model));
		Action.add(new SearchFundAction(model));
		Action.add(new BrowseFundAction(model));
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("in Controler doGet");
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("in Control doPOST");
		String nextPage = performTheAction(request);
		System.out.println("nextPage value is" + nextPage);
		sendToNextPage(nextPage, request, response);

	}

	/*
	 * Extracts the requested action and (depending on whether the user is logged
	 * in) perform it (or make the user login).
	 * 
	 * @param request
	 * 
	 * @return the next page (the view)
	 */
	private String performTheAction(HttpServletRequest request) {
		System.out.println(new Date());
		System.out.println("in controller the action");
		// HttpSession session = request.getSession(true);
		HttpSession session = request.getSession();			// rr session
		String servletPath = request.getServletPath();
		EmployeeBean employee = (EmployeeBean) session.getAttribute("employee");
		CustomerBean customer = (CustomerBean) session.getAttribute("customer");
		String action = getActionName(servletPath);
		System.out.println(" action =" + action);

		try {
			if ((customer != null &&  customerDAO.read(customer.getUserName()).getPassword().equals(customer.getPassword()))|| (employee != null && employeeDAO.read(employee.getUserName()).getPassword().equals(employee.getPassword()))) {
				// Let the logged in user run his chosen action
				return Action.perform(action, request);
			}else {
				session.invalidate();
			}
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			session.invalidate();
				
		}

		System.out.println("user is null!");

		// If the user hasn't logged in, login is the only option

		return Action.perform("login.do", request);
		// }
	}

	/*
	 * If nextPage is null, send back 404 If nextPage ends with ".do", redirect to
	 * this page. If nextPage ends with ".jsp", dispatch (forward) to the page (the
	 * view) This is the common case
	 */
	private void sendToNextPage(String nextPage, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (nextPage == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getServletPath());
			return;
		}

		if (nextPage.endsWith(".do")) {
			response.sendRedirect(nextPage);
			return;
		}

		if (nextPage.endsWith(".jsp")) {
			RequestDispatcher d = request.getRequestDispatcher("WEB-INF/" + nextPage);
			d.forward(request, response);
			return;
		}

//		throw new ServletException(
//				Controller.class.getName() + ".sendToNextPage(\"" + nextPage + "\"): invalid extension.");
		request.setAttribute("error", "invalid Page");
		RequestDispatcher d = request.getRequestDispatcher("WEB-INF/" + "error.jsp");
		d.forward(request, response);
		return;
	}
	

	/*
	 * Returns the path component after the last slash removing any "extension" if
	 * present.
	 */
	private String getActionName(String path) {
		// We're guaranteed that the path will start with a slash
		int slash = path.lastIndexOf('/');
		return path.substring(slash + 1);
	}
}


