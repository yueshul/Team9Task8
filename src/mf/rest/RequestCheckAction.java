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

import org.genericdao.RollbackException;

import mf.databean.CustomerBean;
import mf.databean.EmployeeBean;
import mf.model.CustomerDAO;
import mf.model.Model;

@Path("/requestCheck")
public class RequestCheckAction {

	private static CustomerDAO customerDAO;
	private static Model model;

	public void init() {
	    model = MyApplication.getModel();
		customerDAO = model.getCustomerDAO();
	}

	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response requestCheck(JsonObject object,@Context HttpServletRequest request) {
	    System.out.println("request check");
        if(model == null)init();
        String success = "The check has been successfully requested";
        String notEnoughCash = "You don't have sufficient funds in your account to cover the requested check";
        String notLogIn = "You are not currently logged in";
        String notCustomer = "You must be a customer to perform this action";
        String cashValue	    = object.getString("cashValue").toString().replaceAll("\"", "");
        HttpSession session = request.getSession();
        ResponseMessage message = new ResponseMessage();
		try {
	        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
	        EmployeeBean employee = (EmployeeBean) session.getAttribute("employee");
			if (customer == null) {
				if (employee != null) {
					message.setMessage(notCustomer);
					System.out.println(message);
					System.out.println("not customer");
					return Response.status(200).entity(message).build();
				} else {
    				    message.setMessage(notLogIn);
    				    System.out.println("not logged in");
        				return Response.status(200).entity(message).build();
				}
	        }
			
			
			if (cashValue.length() > 20) {
				System.out.println("too much");
				return Response.status(400).build();
			}
			
			try {
				// limit 15 decimal???
				long amountRequested = Long.parseLong(cashValue);
				if (amountRequested <= 0) {
					System.out.println("negative");
					return Response.status(400).build();
				}
				customer = customerDAO.read(customer.getUserName());
				double curCash =  customer.getCash();
				System.out.println("curCash: "+curCash+" amountRequested: "+amountRequested);
				if (curCash < amountRequested) {
					message.setMessage(notEnoughCash);
					System.out.println("not enough cash");
    					return Response.status(200).entity(message).build();
				}
				
				customer.setCash(curCash - amountRequested);
				customerDAO.update(customer);
				message.setMessage(success);
//				System.out.println(message);
				System.out.println("success");
				return Response.status(200).entity(message).build();
				
			} catch(NumberFormatException e) {
				System.out.println("number format exception");
				return Response.status(400).build();
			}

		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("rolled back");
			return Response.status(400).build();
		}
    }
}




