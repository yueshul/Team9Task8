package mf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.genericdao.MatchArg;
import org.genericdao.RollbackException;

import mf.databean.CustomerBean;
import mf.databean.FundBean;
import mf.databean.FundPriceHistoryBean;
import mf.databean.PositionBean;
import mf.model.CustomerDAO;
import mf.model.FundDAO;
import mf.model.FundPriceHistoryDAO;
import mf.model.Model;
import mf.model.PositionDAO;
import mf.model.TransactionDAO;

public class ViewCustomerAction extends Action {
	private CustomerDAO customerDAO;
	private PositionDAO positionDAO;
	private TransactionDAO transactionDAO;
	private FundDAO fundDAO;
	private FundPriceHistoryDAO fundPriceHistoryDAO;

	public ViewCustomerAction(Model model) {
		customerDAO = model.getCustomerDAO();
		positionDAO = model.getPositionDAO();
		transactionDAO = model.getTransactionDAO();
		fundDAO = model.getFundDAO();
		fundPriceHistoryDAO=model.getFundPriceHistoryDAO();
	}

	@Override
	public String getName() {
		return "viewCustomer.do";
	}

	public String performGet(HttpServletRequest request) {
		return performPost(request);
	}

	public String performPost(HttpServletRequest request) {
		String userName = (String) request.getParameter("userName");
		try {
			CustomerBean customer = customerDAO.read(userName);
//			for list of funds
			PositionBean[] position = positionDAO.getPositionsByCustomer(customer.getUserName());
			if (position != null && position.length != 0) {

				FundBean bean = fundDAO.read(position[0].getFundId());
				if (bean != null) {

					FundPriceHistoryBean[] tmp = fundPriceHistoryDAO.match(MatchArg.equals("fundId", bean.getFundId()));
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
	//for list of funds	
			request.setAttribute("customer", customer);
			request.setAttribute("transaction", transactionDAO.getTransactionByCustomer(userName));
			// if (fundDAO.getAllFunds() != null) {
			// System.out.println("funds are not null here");
			// request.setAttribute("funds", fundDAO.getAllFunds());
			// }
			return "employee_viewAccount.jsp";
		} catch (RollbackException e) {
			request.setAttribute("errors", e.getMessage());
			return "error.jsp";
		}
	}

}
