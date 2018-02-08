package mf.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mf.formbean.LoginForm;
import mf.model.Model;

/*
 * Logs out by setting the "user" session attribute to null.
 * (Actions don't be much simpler than this.)
 */
public class LogoutAction extends Action {

	public LogoutAction(Model model) {
	}

	public String getName() {
		return "logout.do";
	}
	
	public String performGet(HttpServletRequest request) {
	    return performPost(request);
	}
	public String performPost(HttpServletRequest request) {
		HttpSession session = request.getSession();
		// session.setAttribute("Isuserloggedin", null);
		session.setAttribute("emailAddress", null);
		session.invalidate();
		// session.setAttribute("username", null);
		// session.setAttribute("users", null);
		request.setAttribute("form", new LoginForm());
		return "login.do";
		// return "newlogin.jsp";

		// HttpSession session = request.getSession();
		// session.setAttribute("Isuserloggedin", null);
		// session.setAttribute("EmailAddress", null);
		// session.setAttribute("username", null);
		// session.setAttribute("users", null);
		// return "newlogin.jsp";
	}
}