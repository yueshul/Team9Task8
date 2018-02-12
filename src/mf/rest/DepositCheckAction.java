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
import mf.databean.EmployeeBean;
import mf.databean.FundPriceHistoryBean;
import mf.databean.PositionBean;
import mf.model.CustomerDAO;
import mf.model.EmployeeDAO;


import mf.model.Model;


@Path("/depositCheck")
public class DepositCheckAction {

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
		public Response depositCheck(JsonObject object,@Context HttpServletRequest request) {
	        if(model == null)init();
	        String success = "The check was successfully deposited";
	        String notLogIn = "You are not currently logged in";
	        String notEmployee = "You must be an employee to perform this action";
	      // to clarify <<<>>>>
	        String noSuchCustomer = "No such customer in the database";
	        
	        String username = object.getString("username").toString().replaceAll("\"", "");
	        String cash	    = object.getString("cash").toString().replaceAll("\"", "");
//	        double cash = object.g
	        
	        HttpSession session = request.getSession();
	        ResponseMessage message = new ResponseMessage();
			try {
		        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
		        EmployeeBean employee = (EmployeeBean) session.getAttribute("employee");
				if (employee == null) {
					if (customer != null) {
						message.setMessage(notEmployee);
						return Response.status(200).entity(message).build();
					} else {
					message.setMessage(notLogIn);
	    				return Response.status(200).entity(message).build();
					}
		        }
				
				if (customerDAO.read(username) == null) {
					message.setMessage(noSuchCustomer);
    					return Response.status(200).entity(message).build();
				}
				
				CustomerBean customerToDeposit = customerDAO.read(username);
				if (cash.length() > 20) {
					return Response.status(400).build();
				}
				
				try {
					// limit 15 decimal???
					long amountDeposit = Long.parseLong(cash);
					if (amountDeposit <= 0) {
						return Response.status(400).build();
					}
					
					double curCash =  customerToDeposit.getCash();
					double limit = 10000000000000000000000000000000000.00;
					if (curCash + amountDeposit > limit) {
						return Response.status(400).build();
					}
					
					customerToDeposit.setCash(curCash + amountDeposit);
					customerDAO.update(customerToDeposit);
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



