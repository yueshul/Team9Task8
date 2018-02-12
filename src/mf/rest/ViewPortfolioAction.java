package mf.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;

import mf.databean.CustomerBean;
import mf.databean.FundBean;
import mf.databean.PositionBean;
import mf.model.CustomerDAO;
import mf.model.FundDAO;
import mf.model.Model;
import mf.model.PositionDAO;

@Path("/viewportfolio")
public class ViewPortfolioAction {
	private CustomerDAO customerDAO;
	private FundDAO fundDAO;
	private PositionDAO positionDAO;
	Model model;

	public void init() {
		model = new Model();
		customerDAO = model.getCustomerDAO();
		fundDAO = model.getFundDAO();
		positionDAO = model.getPositionDAO();
	}

	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getPortfolio(@Context HttpServletRequest request) {
		if(model == null)init();
		String notLogIn = "You are not currently logged in";
		String fundNotOwned = "You don’t have any funds in your Portfolio";
		String notCustomer = "You must be a customer to perform this action";
		HttpSession session = request.getSession();
		ResponseMessage message = new ResponseMessage();
		ResponseFunds funds = new ResponseFunds();
		ResponseCash cash = new ResponseCash();
       
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
		    PositionBean position[] = positionDAO.getPositionsByCustomer(customer.getUserName());
			if (position == null || position.length == 0) {
				message.setMessage(fundNotOwned);
				return Response.status(200).entity(message).build();
			}
			
			Map<FundBean, Long> fundShares = new HashMap<FundBean, Long>();
			for (PositionBean p : position) {
				fundShares.put(fundDAO.read(p.getFundId()), p.getShares());
			}
			
			String success = "The action was successful";
			String balance = customer.getCash()+"";
			//get funds
			ArrayList<HashMap<String,String>> fundList = new ArrayList<HashMap<String, String>>();
			
			for(FundBean fund:fundShares.keySet()) {
				HashMap<String, String> oneFund = new HashMap<String, String>();
				oneFund.put("name", fund.getName());
				oneFund.put("shares", fundShares.get(fund).toString());
				oneFund.put("price", fund.getLatestPrice()+"");
				fundList.add(oneFund);
			}
			String fundlist = fundList.toString();
			message.setMessage(success);
			cash.setCash(balance);
			funds.setFunds(fundlist);
			return Response.status(200).entity(message).entity(cash).entity(funds).build();
			} catch(NumberFormatException |RollbackException e) {
				e.printStackTrace();
				return Response.status(500).build();
			}
	}
}
