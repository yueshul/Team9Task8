package mf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.formbeanfactory.FormBeanFactory;
import org.formbeanfactory.FormBeanFactoryException;
import org.genericdao.DuplicateKeyException;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;

import mf.databean.CustomerBean;
import mf.databean.FundBean;
import mf.databean.TransactionBean;
import mf.formbean.BuyFundForm;
import mf.model.CustomerDAO;
import mf.model.FundDAO;
import mf.model.Model;
import mf.model.TransactionDAO;


public class BuyFundAction extends Action {
	private FormBeanFactory<BuyFundForm> formBeanFactory = new FormBeanFactory<>(BuyFundForm.class);
	private CustomerDAO customerDAO;
	private FundDAO fundDAO;
	private TransactionDAO transactionDAO;

	public BuyFundAction(Model model) {
		customerDAO = model.getCustomerDAO();
		fundDAO = model.getFundDAO();
		transactionDAO = model.getTransactionDAO();
	}

	@Override
	public String getName() {
		return "buyFund.do";
	}
	public String performGet(HttpServletRequest request) {
		HttpSession session = request.getSession();
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
        if (customer == null) {
        		return "login.do";
        }
	    BuyFundForm form = new BuyFundForm();
        request.setAttribute("form", form);
        if (request.getParameter("symbol") != null) {
    			form.setSymbol(request.getParameter("symbol"));
    			request.setAttribute("form", form);
        }
        FundBean[] fund;
		try {
			fund = fundDAO.getAllFunds();
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("errors", e.getMessage());
			return "error.jsp";
		}
		request.setAttribute("funds", fund);
        return "customer_buyFund.jsp";
    }
	public String performPost(HttpServletRequest request) {		
		HttpSession session = request.getSession();
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
        if (customer == null) {
    			return "login.do";
        }
        List<String> errors = new ArrayList<String>();
		request.setAttribute("errors",errors);
		try {
			FundBean[] fund = fundDAO.getAllFunds();
			request.setAttribute("funds", fund);
			BuyFundForm form = formBeanFactory.create(request);
            request.setAttribute("form", form);
            if (form.hasValidationErrors()) {
                return "customer_buyFund.jsp";
            }
            if (form.getAmountAsDouble() <= 0) {
            		errors.add("Amount should be positive");
            		request.setAttribute("errors", errors);
            		return "customer_buyFund.jsp";
            }
//            if (fundDAO.getFunds(MatchArg.equals("symbol", form.getSymbol())) == null) {
//            		request.setAttribute("errors", "Invalid Ticker");
//            		return "error.jsp";
//            }
            if (fundDAO.getFundByTicker(form.getSymbol()).length == 0 ) {
            		errors.add("Invalid Ticker");
	            	request.setAttribute("errors", errors);
	       		return "customer_buyFund.jsp";
            }

//            if (customer.getCash() < form.getAmountAsInt()) {
//            		request.setAttribute("errors", "The purchase amount exceeds your available balance");
//            		return "error.jsp";
//            }
            TransactionBean bean = new TransactionBean();
            bean.setUserName(customer.getUserName());
            bean.setAmount(form.getAmountAsDouble());
            bean.setSymbol(form.getSymbol());
            bean.setStatus("Pending");
            bean.setTransactionType("Buy Fund");
            try {
                transactionDAO.create(bean);
                request.setAttribute("message", "The purchase is processing. Thank you!");
                return "customer_buyFund.jsp";
            } catch (DuplicateKeyException e) {
            		request.setAttribute("errors", "Duplicate transaction");
                return "error.jsp";
            }

		} catch (RollbackException | FormBeanFactoryException e) {
			e.printStackTrace();
			request.setAttribute("errors", e.getMessage());
			return "error.jsp";
		}
	}
}

