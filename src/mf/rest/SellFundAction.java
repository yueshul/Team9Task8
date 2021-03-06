package mf.rest;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import mf.databean.CustomerBean;
import mf.databean.PositionBean;
import mf.model.Model;
import mf.model.PositionDAO;
import mf.model.CustomerDAO;
import mf.model.FundDAO;

@Path("/sellFund")
public class SellFundAction{
	private static CustomerDAO customerDAO;
	private static FundDAO fundDAO;
	private static PositionDAO positionDAO;
	private static Model model;

	public void init() {
	    model = MyApplication.getModel();
		customerDAO = model.getCustomerDAO();
		fundDAO = model.getFundDAO();
        positionDAO = model.getPositionDAO();
	}

	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response SellFund(JsonObject object,@Context HttpServletRequest request) {
	    System.out.println("Sell Fund");
        if(model == null)init();
        	String fundSymbol = object.get("symbol").toString().replaceAll("\"", "");
        String numShares = object.get("numShares").toString().replaceAll("\"", "");
        String success = "The shares have been successfully sold";
        String notLogIn = "You are not currently logged in";
        String notEnoughShare = "You don’t have that many shares in your portfolio";
        String fundNotExist = "The fund you provided does not exist";
        String notCustomer = "You must be a customer to perform this action";
        HttpSession session = request.getSession();
        ResponseMessage message = new ResponseMessage();
		try {
	        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
			if (session.getAttribute("employee")==null && customer == null) {
				System.out.println("not log in");
				message.setMessage(notLogIn);
    				return Response.status(200).entity(message).build();
	        }
			if (customer==null || customerDAO.read(customer.getUserName()) == null) {
				System.out.println("not customer");
				message.setMessage(notCustomer);
				return Response.status(200).entity(message).build();
			}
			if (fundSymbol == null || fundSymbol.length() == 0) {
				System.out.println("Fund not exist");
				message.setMessage(fundNotExist);
				return Response.status(200).entity(message).build();
			}
			if (fundDAO.match(MatchArg.equals("symbol", fundSymbol)).length == 0) {
				System.out.println("Fund not exist");
				message.setMessage(fundNotExist);
				return Response.status(200).entity(message).build();
			}
			int fundId = fundDAO.match(MatchArg.equals("symbol", fundSymbol))[0].getFundId();
			PositionBean position = positionDAO.read(customer.getUserName(),fundId);
			if (position == null) {
				System.out.println("Fund not exist");
				message.setMessage(fundNotExist);
				return Response.status(200).entity(message).build();
			}
			double price = fundDAO.read(fundId).getLatestPrice();
			try {
				long numOfShares = Long.parseLong(numShares);
				if (numOfShares <= 0) {
					return Response.status(400).build();
				}
				long availableShares = position.getShares();
				double curCash = customer.getCash();
				if (availableShares < numOfShares) {
					System.out.println("not enough share");
					message.setMessage(notEnoughShare);
					return Response.status(200).entity(message).build();
				} else if (availableShares == numOfShares) {
					positionDAO.delete(customer.getUserName(), fundId);
				} else {
					position.setShares(availableShares - numOfShares);
					positionDAO.update(position);
				}
				customerDAO.read(customer.getUserName()).setCash(curCash + numOfShares * price);
				customerDAO.update(customer);
				System.out.println("Success");
				message.setMessage(success);
				return Response.status(200).entity(message).build();
			} catch(NumberFormatException e) {
				return Response.status(400).build();
			}

		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(400).build();
		}
    }
}

