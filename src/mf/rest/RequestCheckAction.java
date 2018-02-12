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
import mf.model.EmployeeDAO;
import mf.model.Model;

@Path("/requestCheck")
public class RequestCheckAction {

	private CustomerDAO customerDAO;
	private EmployeeDAO employeeDAO;
	Model model;

	public void init() {
		model = new Model();
		customerDAO = model.getCustomerDAO();
		employeeDAO = model.getEmployeeDAO();
	}

	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response requestCheck(JsonObject object,@Context HttpServletRequest request) {
        if(model == null)init();
        String success = "The check was successfully requested";
        String notEnoughCash = "You don’t have sufficient funds in your account to cover the requested check";
        
        String notLogIn = "You are not currently logged in";
        String notCustomer = "You must be a customer to perform this action";
        String cashValue	    = object.getString("cashValue").toString().replaceAll("\"", "");;
        
        HttpSession session = request.getSession();
        ResponseMessage message = new ResponseMessage();
		try {
	        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
	        EmployeeBean employee = (EmployeeBean) session.getAttribute("employee");
			if (customer == null) {
				if (employee != null) {
					message.setMessage(notCustomer);
					return Response.status(200).entity(message).build();
				} else {
				message.setMessage(notLogIn);
    				return Response.status(200).entity(message).build();
				}
	        }
			
			
			if (cashValue.length() > 20) {
				return Response.status(400).build();
			}
			
			try {
				// limit 15 decimal???
				long amountRequested = Long.parseLong(cashValue);
				if (amountRequested <= 0) {
					return Response.status(400).build();
				}
				
				double curCash =  customer.getCash();
				
				if (curCash < amountRequested) {
					message.setMessage(notEnoughCash);
    					return Response.status(200).entity(message).build();
				}
				
				customer.setCash(curCash - amountRequested);
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



