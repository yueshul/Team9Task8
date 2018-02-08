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

public class SearchFundAction extends Action {
	private FundDAO fundDAO;
	private FundPriceHistoryDAO fundPriceHistoryDAO;
	private FormBeanFactory<SearchForm> formBeanFactory = new FormBeanFactory<>(SearchForm.class);

	public SearchFundAction(Model model) {
		fundDAO = model.getFundDAO();
		fundPriceHistoryDAO = model.getFundPriceHistoryDAO();
	}

	@Override
	public String getName() {
		return "researchFund.do";
	}

	public String performGet(HttpServletRequest request) {
		System.out.println("test:"+request.getParameter("fundName"));
		HttpSession session = request.getSession();
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);
		if (session.getAttribute("customer") == null) {
			return "login.do";
		}

		try {
			SearchForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
			String fundName = request.getParameter("fundName");
			System.out.println("fun:"+fundName);
			if (fundName != null) {// first: request comes from view detail
				FundBean[] funds = fundDAO.getFunds(MatchArg.equals("name", fundName));
				if (funds == null || funds.length == 0) {
					errors.add("Opps The fund is gone, search again");
					request.setAttribute("funds", fundDAO.getAllFunds());
					System.out.println("funds==null");
					return "customer_browseFund.jsp";
				}
				FundBean fund = funds[0];
				int fundId = fund.getFundId();
				FundPriceHistoryBean[] history = fundPriceHistoryDAO.match(MatchArg.equals("fundId", fundId));
				if (history != null &&history.length != 0) {// The price has price history, set attributes for chart
					List<Double> priceHistory= new ArrayList();
					for(FundPriceHistoryBean x : history) {
						priceHistory.add(x.getPrice());
					}
					request.setAttribute("priceHistory", priceHistory);
				}
				double change = 1;
				if (history.length > 1) {
					double lastPrice = history[history.length - 2].getPrice();
					double latestPrice = fund.getLatestPrice();
					change = (latestPrice - lastPrice) / lastPrice;
					request.setAttribute("change", change);
				}
				request.setAttribute("fund", fund);
				return "customer_researchFund.jsp";
			}else {
				System.out.println("else");
				return "customer_browseFund.jsp";
			}
				
		} catch (FormBeanFactoryException | RollbackException e) {
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
			errors.addAll(form.getValidationErrors());
			if (errors.size() > 0) {
				request.setAttribute("errors", errors);
				request.setAttribute("form", new SearchForm());
				request.setAttribute("funds", fundDAO.getAllFunds());
				return "customer_browseFund.jsp";
			}
			String symbol = form.getSymbol();
			String name = form.getName();

			if (name != null && name.length() != 0) {// request comes from search by name
				FundBean[] funds = fundDAO.getFunds(MatchArg.equals("name", name));
				if (funds == null || funds.length == 0) {
					errors.add("Fund is not found");
					request.setAttribute("errors", errors);
					request.setAttribute("form", new SearchForm());
					request.setAttribute("funds", fundDAO.getAllFunds());
					return "customer_browseFund.jsp";
				}
				FundBean fund = funds[0];
				int fundId = fund.getFundId();
				FundPriceHistoryBean[] history = fundPriceHistoryDAO.match(MatchArg.equals("fundId", fundId));
				if (history != null &&history.length != 0) {// The price has price history, set attributes for chart
					List<Double> priceHistory= new ArrayList();
					for(FundPriceHistoryBean x : history) {
						priceHistory.add(x.getPrice());
					}
					request.setAttribute("priceHistory", priceHistory);
				}
				request.setAttribute("fund", fund);
				return "customer_researchFund.jsp";

			} else if (symbol != null && symbol.length() != 0) {// request comes from search by symbol
				FundBean[] funds = fundDAO.getFunds(MatchArg.equals("symbol", symbol));
				if (funds == null || funds.length == 0) {
					errors.add("Invalid symbol");
					request.setAttribute("errors", errors);
					request.setAttribute("form", new SearchForm());
					request.setAttribute("funds", fundDAO.getAllFunds());
					return "customer_browseFund.jsp";
				} else {
					FundBean fund = funds[0];
					int fundId = fund.getFundId();
					FundPriceHistoryBean[] history = fundPriceHistoryDAO.match(MatchArg.equals("fundId", fundId));
					if (history != null &&history.length != 0) {// The price has price history, set attributes for chart
						List<Double> priceHistory= new ArrayList();
						for(FundPriceHistoryBean x : history) {
							priceHistory.add(x.getPrice());
						}
						request.setAttribute("priceHistory", priceHistory);
					}
					request.setAttribute("fund", fund);
					return "customer_researchFund.jsp";
				}
			} else {
				request.setAttribute("errors", errors);
				request.setAttribute("form", new SearchForm());
				request.setAttribute("funds", fundDAO.getAllFunds());
				return "customer_browseFund.jsp";
			}
		} catch (RollbackException | FormBeanFactoryException e) {
			e.printStackTrace();
			request.setAttribute("errors", e.getMessage());
			return "error.jsp";
		}
	}
}