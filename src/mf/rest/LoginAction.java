package mf.rest;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.genericdao.MatchArg;
import org.genericdao.RollbackException;

import com.google.gson.Gson;
import mf.rest.ResponseMessage;

import mf.databean.CustomerBean;
import mf.databean.EmployeeBean;
import mf.databean.FundBean;
import mf.databean.FundPriceHistoryBean;
import mf.databean.PositionBean;
import mf.databean.TransactionBean;
import mf.model.CustomerDAO;
import mf.model.EmployeeDAO;
import mf.model.FundDAO;
import mf.model.FundPriceHistoryDAO;
import mf.model.Model;
import mf.model.PositionDAO;
import mf.model.TransactionDAO;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginAction{
    private EmployeeDAO employeeDAO;
    private CustomerDAO customerDAO;
    private PositionDAO positionDAO;
    private TransactionDAO transactionDAO;
    private FundDAO fundDAO;
    Model model;
    private FundPriceHistoryDAO fundPriceHistoryDAO;
    public void init() {
        model = new Model();
        employeeDAO = model.getEmployeeDAO();
        customerDAO = model.getCustomerDAO();
        positionDAO = model.getPositionDAO();
        transactionDAO = model.getTransactionDAO();
        fundDAO = model.getFundDAO();
        fundPriceHistoryDAO = model.getFundPriceHistoryDAO();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginAction(JsonObject object,@Context HttpServletRequest request) {
        //Gson gson = new Gson();
        if(model == null)init();
        String userName = object.get("userName").toString().replaceAll("\"", "");
        //String type = object.get("type").toString().replaceAll("\"", "");
        String password = object.get("password").toString().replaceAll("\"", "");
        String successMessage = "Welcome "+userName;
        String failMessage = "There seems to be an issue with the username/password combination that you entered";
        HttpSession session = request.getSession();
        ResponseMessage message = new ResponseMessage();
        try {
            EmployeeBean employee = null;
            CustomerBean customer = null;
//            if(type.equals("Customer")) {
//                customer = customerDAO.read(userName);
//            }else if(type.equals("Employee")) {
//                employee = employeeDAO.read(userName);
//            }
            if (employeeDAO.read(userName) != null) {
                employee = employeeDAO.read(userName);
            		if (employee.getPassword().equals(password)) {
                    session.setAttribute("employee", employee);
                    
            			message.setMessage(successMessage);
            			return Response.status(200).entity(message).build();
                   
                } else {
	                	message.setMessage(failMessage);
	        			return Response.status(200).entity(message).build();
               
                }
            } else if (customerDAO.read(userName) != null) {
                customer = customerDAO.read(userName);
            		if (customer.getPassword().equals(password)) {           
                    PositionBean[] position= positionDAO.getPositionsByCustomer(customer.getUserName());
                    if (position != null && position.length != 0) {
                        FundBean bean = fundDAO.read(position[0].getFundId());
                        if (bean != null) {
                            FundPriceHistoryBean[] tmp = fundPriceHistoryDAO
                                    .match(MatchArg.equals("fundId", bean.getFundId()));
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
                    session.setAttribute("customer", customer);
                    TransactionBean[] transactions = transactionDAO.getTransactionByCustomer(customer.getUserName());
                    System.out.println("len:" + transactions.length);
                    request.setAttribute("transactions", transactions);
                    message.setMessage("Welcome "+userName);
        				return Response.status(200).entity(message).build();
               
                } else {
	                	message.setMessage(failMessage);
	    				return Response.status(200).entity(message).build();
                }
            } else {
             	message.setMessage(failMessage);
				return Response.status(200).entity(message).build();
            }

        } catch (RollbackException e) {
         	message.setMessage(failMessage);
			return Response.status(200).entity(message).build();
        }
    }
}
