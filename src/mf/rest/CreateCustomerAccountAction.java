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
import org.genericdao.Transaction;
import mf.databean.CustomerBean;
import mf.model.CustomerDAO;
import mf.model.EmployeeDAO;
import mf.model.Model;

@Path("/createCustomerAccount")
public class CreateCustomerAccountAction {
    private CustomerDAO customerDAO;
    private EmployeeDAO employeeDAO;
    private Model model;
    public void init() {
        model = new Model();
        customerDAO = model.getCustomerDAO();
        employeeDAO = model.getEmployeeDAO();
    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response performPost(JsonObject object,@Context HttpServletRequest request) {
        if(model == null)init();
        ResponseMessage message = new ResponseMessage();
        String userName = object.get("username").toString().replaceAll("\"", "");
        String password = object.get("password").toString().replaceAll("\"", "");
        String first_name = object.get("fname").toString().replaceAll("\"", "");
        String last_name = object.get("lname").toString().replaceAll("\"", "");
        String street_address = object.get("address").toString().replaceAll("\"", "");
        String city = object.get("city").toString().replaceAll("\"", "");
        String state = object.get("state").toString().replaceAll("\"", "");
        String zip = object.get("zip").toString().replaceAll("\"", "");
        String email = object.get("zip").toString().replaceAll("\"", "");
        String initial_cash = object.get("cash").toString().replaceAll("\"", "");
        double cash = 0;
        try {
            cash = Double.parseDouble(initial_cash);
        }catch(NumberFormatException e) {
            String notDoubleMessage = "The initial value is not valid number";
            message.setMessage(notDoubleMessage);
            return Response.status(403).entity(message).build();
        }
        HttpSession session = request.getSession();
        String successMessage = "fname was registered successfully";
        String notLoggedInMessage = "You are not currently logged in";
        String notAdminMessage = "You must be an employee to perform this action";
        
        if (session.getAttribute("customer")!=null && session.getAttribute("employee") == null) {
            message.setMessage(notAdminMessage);
            return Response.status(200).entity(message).build();
        }
        if (session.getAttribute("customer")==null && session.getAttribute("employee") == null) {
            message.setMessage(notLoggedInMessage);
            return Response.status(200).entity(message).build();
        }
        
        try {
            Transaction.begin();
            if (customerDAO.read(userName) != null || employeeDAO.read(userName) != null) {
                String duplicateMessage = "The input you provided is not valid";
                message.setMessage(duplicateMessage);
                return Response.status(200).entity(message).build();
            }
            CustomerBean customer = new CustomerBean();
            customer.setFirstName(first_name);
            customer.setLastName(last_name);
            customer.setUserName(userName);
            customer.setCash(cash);
            customer.setCity(city);
            customer.setZip(zip);
            customer.setEmail(email);
            customer.setState(state);
            customer.setPassword(password);
            customer.setAddressLine1(street_address);
            customer.setAddressLine2("");
            customerDAO.create(customer);
            Transaction.commit();
            message.setMessage(successMessage);
            return Response.status(200).entity(message).build();
        } catch (RollbackException e) {
            if (Transaction.isActive()) Transaction.rollback();
            String rollBackMessage = "Transaction roll back";
            message.setMessage(rollBackMessage);
            return Response.status(200).entity(message).build();
        }
    }
}
