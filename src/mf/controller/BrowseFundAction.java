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

import mf.databean.FundBean;
import mf.databean.FundPriceHistoryBean;
import mf.formbean.SearchForm;
import mf.model.FundDAO;
import mf.model.FundPriceHistoryDAO;
import mf.model.Model;

public class BrowseFundAction extends Action {
	private FundDAO fundDAO;
	private FundPriceHistoryDAO fundPriceHistoryDAO;
	private FormBeanFactory<SearchForm> formBeanFactory = new FormBeanFactory<>(SearchForm.class);

	public BrowseFundAction(Model model) {
		fundDAO = model.getFundDAO();
		fundPriceHistoryDAO = model.getFundPriceHistoryDAO();
	}

	@Override
	public String getName() {
		return "browseFund.do";
	}

	public String performGet(HttpServletRequest request) {
		HttpSession session = request.getSession();
		List<String> errors = new ArrayList<String>();
		
		request.setAttribute("errors", errors);
		if (session.getAttribute("customer") == null) {
			return "login.do";
		}

		try {
			SearchForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
			FundBean[] fund = fundDAO.getAllFunds();
			request.setAttribute("funds", fund);
			return "customer_browseFund.jsp";
		} catch (FormBeanFactoryException |RollbackException e) {
			e.printStackTrace();
			return "error.jsp";
		}
	}

	public String performPost(HttpServletRequest request) {
		HttpSession session = request.getSession();
		List<String> errors = new ArrayList<String>();
		
		request.setAttribute("errors", errors);
		if (session.getAttribute("customer") == null) {
			return "login.do";
		}

		try {
			SearchForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
			FundBean[] fund = fundDAO.getAllFunds();
			request.setAttribute("funds", fund);
			return "customer_browseFund.jsp";
		} catch (FormBeanFactoryException |RollbackException e) {
			e.printStackTrace();
			return "error.jsp";
		}
	}
}