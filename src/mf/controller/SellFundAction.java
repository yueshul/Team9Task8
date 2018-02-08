package mf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.formbeanfactory.FormBeanFactory;
import org.formbeanfactory.FormBeanFactoryException;
import org.genericdao.DuplicateKeyException;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import mf.databean.CustomerBean;
import mf.databean.FundBean;
import mf.databean.PositionBean;
import mf.databean.TransactionBean;
import mf.formbean.BuyFundForm;
import mf.formbean.SellFundForm;
import mf.model.Model;
import mf.model.PositionDAO;
import mf.model.TransactionDAO;
import mf.model.CustomerDAO;
import mf.model.FundDAO;


public class SellFundAction extends Action {
	private FormBeanFactory<SellFundForm> formBeanFactory = new FormBeanFactory<>(SellFundForm.class);
	private CustomerDAO customerDAO;
	private FundDAO fundDAO;
	private TransactionDAO transactionDAO;
	private PositionDAO positionDAO;

	public SellFundAction(Model model) {
		customerDAO = model.getCustomerDAO();
		fundDAO = model.getFundDAO();
        transactionDAO = model.getTransactionDAO();
        positionDAO = model.getPositionDAO();
	}

	@Override
	public String getName() {
		return "sellFund.do";
	}
	public String performGet(HttpServletRequest request) {
		HttpSession session = request.getSession();
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
		if (customer == null) {
            return "login.do";
        }
		SellFundForm form = new SellFundForm();
        request.setAttribute("form", form);
        if (request.getParameter("symbol") != null) {
        		form.setSymbol(request.getParameter("symbol"));
        		request.setAttribute("form", form);
        }
        PositionBean[] position;
		try {
			position = positionDAO.getPositionsByCustomer(customer.getUserName());
			Map<String, Double> map = new HashMap<String, Double>();
			if (position.length != 0) {
				for (PositionBean p : position) {
        			map.put(fundDAO.read(p.getFundId()).getSymbol(), p.getShares());
				}
			}
			request.setAttribute("funds", map);
	        return "customer_sellFund.jsp";
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("errors", e.getMessage());
			return "error.jsp";
		}
    }

	public String performPost(HttpServletRequest request) {
		HttpSession session = request.getSession();
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
        List<String> errors = new ArrayList<String>();
		request.setAttribute("errors",errors);
		try {
			if (customer == null) {
                return "login.do";
            }
			SellFundForm form = formBeanFactory.create(request);
            request.setAttribute("form", form);
            PositionBean[] position = positionDAO.getPositionsByCustomer(customer.getUserName());
            Map<String, Double> map = new HashMap<String, Double>();
            if (position.length != 0) {
				for (PositionBean p : position) {
        			map.put(fundDAO.read(p.getFundId()).getSymbol(), p.getShares());
				}
			}
            request.setAttribute("funds", map);
            if (form.hasValidationErrors()) {
                return "customer_sellFund.jsp";
            }
            if (form.getSharesAsDouble() <= 0) {
            		errors.add("Number of shares should be positive.");
            		request.setAttribute("errors", errors);
            		return "customer_sellFund.jsp";
            }
            FundBean[] fund = fundDAO.getFunds(MatchArg.equals("symbol", form.getSymbol()));
            if (fund.length == 0) {
            		errors.add("Invalid Ticker");
            		request.setAttribute("errors", errors);
            		return "customer_sellFund.jsp";
            } else if (positionDAO.read(customer.getUserName(), fund[0].getFundId()) == null) {
            		errors.add("You do not own the fund.");
            		request.setAttribute("errors", errors);
            		return "customer_sellFund.jsp";
            }
//            PositionBean position = positionDAO.read(customer.getUserName(),fund[0].getFundId());
//            if (position.getShares() < form.getSharesAsInt()) {
//            		request.setAttribute("errors", "The selling number of shares exceeds your available shares");
//            		return "error.jsp";
//            }
            TransactionBean bean = new TransactionBean();
            bean.setUserName(customer.getUserName());
            bean.setShares(form.getSharesAsDouble());
            bean.setTransactionType("Sell Fund");
            bean.setStatus("Pending");
            bean.setSymbol(form.getSymbol());
            try {
                transactionDAO.create(bean);
                request.setAttribute("message", "The transaction is processing. Thank you!");
                return "customer_sellFund.jsp";
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

