package mf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.MatchArg;
import org.genericdao.RollbackException;

import mf.databean.CustomerBean;
import mf.databean.FundBean;
import mf.databean.FundPriceHistoryBean;
import mf.databean.PositionBean;
import mf.databean.TransactionBean;
import mf.model.FundDAO;
import mf.model.FundPriceHistoryDAO;
import mf.model.Model;
import mf.model.PositionDAO;
import mf.model.TransactionDAO;

public class ViewAccountAction extends Action {
	private PositionDAO positionDAO;
	private TransactionDAO transactionDAO;
	private FundDAO fundDAO;
	private FundPriceHistoryDAO fundPriceHistoryDAO;

	public ViewAccountAction(Model model) {
		positionDAO = model.getPositionDAO();
		transactionDAO = model.getTransactionDAO();
		fundDAO = model.getFundDAO();
		fundPriceHistoryDAO = model.getFundPriceHistoryDAO();
	}

	@Override
	public String getName() {
		return "viewAccount.do";
	}

	public String performGet(HttpServletRequest request) {
		return performPost(request);
	}

	public String performPost(HttpServletRequest request) {
		HttpSession session = request.getSession();
		try {
			List<String> errors = new ArrayList<String>();
			request.setAttribute("errors", errors);
			CustomerBean customer = (CustomerBean) session.getAttribute("customer");
			PositionBean[] position = positionDAO.getPositionsByCustomer(customer.getUserName());
			if (position != null && position.length != 0) {

				FundBean bean = fundDAO.read(position[0].getFundId());
				if (bean != null) {

					FundPriceHistoryBean[] tmp = fundPriceHistoryDAO.match(MatchArg.equals("fundId", bean.getFundId()));
					if (tmp != null && tmp.length != 0) {
						request.setAttribute("date", tmp[tmp.length - 1].getPriceDate());
					}
					Map<FundBean, Long> map = new HashMap<FundBean, Long>();
					for (PositionBean p : position) {
						map.put(fundDAO.read(p.getFundId()), p.getShares());
					}
					request.setAttribute("funds", map);

				}
			}

			TransactionBean[] transactions = transactionDAO.getTransactionByCustomer(customer.getUserName());
			System.out.println("len:" + transactions.length);
			request.setAttribute("transactions", transactions);

			return "customer_homepage.jsp";
		} catch (RollbackException e) {
			request.setAttribute("errors", e.getMessage());
			return "error.jsp";
		}
	}
}
