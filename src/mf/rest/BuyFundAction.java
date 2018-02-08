package mf.rest;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.formbeanfactory.FormBeanFactory;
import org.formbeanfactory.FormBeanFactoryException;
import org.genericdao.DuplicateKeyException;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;

import mf.databean.CustomerBean;
import mf.databean.FundBean;
import mf.databean.FundPriceHistoryBean;
import mf.databean.PositionBean;
import mf.databean.TransactionBean;
import mf.formbean.BuyFundForm;
import mf.model.CustomerDAO;
import mf.model.FundDAO;
import mf.model.FundPriceHistoryDAO;
import mf.model.Model;
import mf.model.PositionDAO;
import mf.model.TransactionDAO;


public class BuyFundAction {
	private CustomerDAO customerDAO;
	private FundDAO fundDAO;
	private PositionDAO positionDAO;
	private FundPriceHistoryDAO fundPriceHistoryDAO;
	Model model;

	public void init() {
		customerDAO = model.getCustomerDAO();
		fundDAO = model.getFundDAO();
        positionDAO = model.getPositionDAO();
        fundPriceHistoryDAO = model.getFundPriceHistoryDAO();
	}

	@POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response BuyFund(JsonObject object,@Context HttpServletRequest request) {
		if(model == null)init();
		String fundSymbol = object.get("fundSymbol").toString().replaceAll("\"", "");
		String cashValue = object.get("cashValue").toString().replaceAll("\"", "");
		String success = "The fundhas been successfully purchased";
		String notLogIn = "You are not currently logged in";
		String notEnoughCashA = "You don’t have enough cash in your account to make this purchase";
		String notEnoughCashP = "You didn’t provide enough cash to make this purchase";
		String fundNotExist = "The fund you provided does not exist";
		String notCustomer = "You must be a customer to perform this action";
		HttpSession session = request.getSession();
		ResponseMessage message = new ResponseMessage();
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
        if (customer == null) {
        		message.setMessage(notLogIn);
			return Response.status(200).entity(message).build();
        }
        try {
			if (customerDAO.read(customer.getUserName()) == null) {
				message.setMessage(notCustomer);
				return Response.status(200).entity(message).build();
			}
			if (fundSymbol == null || fundSymbol.length() == 0) {
				message.setMessage(fundNotExist);
				return Response.status(400).entity(message).build();
			}
			if (fundDAO.match(MatchArg.equals("symbol", fundSymbol)).length == 0) {
				message.setMessage(fundNotExist);
				return Response.status(200).entity(message).build();
			}
			int fundId = fundDAO.match(MatchArg.equals("symbol", fundSymbol))[0].getFundId();
			FundPriceHistoryBean[] history = fundPriceHistoryDAO.match(MatchArg.equals("fundId",fundId));
			double price = history[history.length - 1].getPrice();
			try {
				double amount = Double.parseDouble(cashValue);
				if (amount <= 0) {
					return Response.status(400).build();
				}
				if (customer.getCash() < amount) {
					message.setMessage(notEnoughCashA);
					return Response.status(200).entity(message).build();
				} 
				if (positionDAO.read(customer.getUserName(),fundId) == null) {
					PositionBean newPos = new PositionBean();
					newPos.setFundId(fundId);
					newPos.setUserName(customer.getUserName());
					newPos.setShares((long) (amount / price));
					positionDAO.create(newPos);
				} else {
					PositionBean oldPos = positionDAO.read(customer.getUserName(), fundId);
					long oldShares = oldPos.getShares();
					oldPos.setShares(oldShares + (long) (amount / price));
					positionDAO.update(oldPos);
				}
				double purchase = price * (long) (amount / price);
				customerDAO.read(customer.getUserName()).setCash(customer.getCash() - purchase);
				customerDAO.update(customer);
				message.setMessage(success);
				return Response.status(200).entity(message).build();
			} catch(NumberFormatException e) {
				return Response.status(500).build();
			}
        } catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(500).build();
		}
	}
}

