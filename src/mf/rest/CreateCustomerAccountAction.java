package mf.rest;

import java.util.Arrays;

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
    private static CustomerDAO customerDAO;
    private static EmployeeDAO employeeDAO;
    private static Model model;
    public void init() {
        model = MyApplication.getModel();
        customerDAO = model.getCustomerDAO();
        employeeDAO = model.getEmployeeDAO();
    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response performPost(JsonObject object,@Context HttpServletRequest request) {
        System.out.println("Create Customer Account");
        if(model == null)init();
        ResponseMessage message = new ResponseMessage();
        String userName = null;
        String password = null;
        String first_name = null;
        String last_name = null;
        String street_address = null;
        String city = null;
        String state = null;
        String zip = null;
        String email = null;
        String initial_cash = null;
        try {
            userName = object.get("username").toString().replaceAll("\"", "");
            password = object.get("password").toString().replaceAll("\"", "");
            first_name = object.get("fname").toString().replaceAll("\"", "");
            last_name = object.get("lname").toString().replaceAll("\"", "");
            street_address = object.get("address").toString().replaceAll("\"", "");
            city = object.get("city").toString().replaceAll("\"", "");
            state = object.get("state").toString().replaceAll("\"", "");
            zip = object.get("zip").toString().replaceAll("\"", "");
            email = object.get("email").toString().replaceAll("\"", "");
        }catch (NullPointerException e) {
            return Response.status(400).build();
        }
        if(object.get("cash") != null) {
            initial_cash = object.get("cash").toString().replaceAll("\"", "");
        }
        double cash = 0;
        if(initial_cash == null || initial_cash.length() == 0)initial_cash = "0";
        try {
            cash = Double.parseDouble(initial_cash);
        }catch(NumberFormatException e) {
            String notDoubleMessage = "The initial value is not valid number";
            message.setMessage(notDoubleMessage);
            return Response.status(400).entity(message).build();
        }
        HttpSession session = request.getSession();
        String duplicateMessage = "The input you provided is not valid";
        String successMessage = first_name+" was registered successfully";
        String notLoggedInMessage = "You are not currently logged in";
        String notAdminMessage = "You must be an employee to perform this action";
        
        String []states = {"Alaska","Alabama", "Arkansas",  "American Samoa",
                "Arizona","California", "Colorado", "Connecticut", "District of Columbia",
                "Delaware","Florida","Georgia","Guam", "Hawaii","Iowa", "Idaho", "Illinois",
                "Indiana",  "Kansas",   "Kentucky",  "Louisiana", "Massachusetts",
                "Maryland", "Maine", "Michigan",
                "Minnesota", "Missouri",
                "Mississippi",
                "Montana",
                "North Carolina", "North Dakota",
                "Nebraska",
                "New Hampshire",
                "New Jersey",
                "New Mexico",
                "Nevada",
                "New York",
                "Ohio",
                "Oklahoma",
                "Oregon",
                "Pennsylvania",
                "Puerto Rico",
                "Rhode Island",
                "South Carolina",
                "South Dakota",
                "Tennessee",
                "Texas",
                "Utah",
                "Virginia",
                "Virgin Islands",
                "Vermont",
                "Washington",
                "Wisconsin",
                "West Virginia",
                "Wyoming","AK",
                "AL",  "AR",  "AS", "AZ", "CA","CO",
                "CT", "DC", "DE",
                "FL",
                "GA",
                "GU",
                "HI",
                "IA",
                "ID",
                "IL",
                "IN",
                "KS",
                "KY",
                "LA",
                "MA",
                "MD",
                "ME",
                "MI",
                "MN",
                "MO",
                "MS",
                "MT",
                "NC",
                "ND",
                "NE",
                "NH",
                "NJ",
                "NM",
                "NV",
                "NY",
                "OH", "OK", "OR", "PA",
                "PR", "RI", "SC",  "SD",  "TN",  "TX",  "UT", "VA", "VI", "VT", "WA","WI","WV","WY"};
        
        if (session.getAttribute("customer")!=null && session.getAttribute("employee") == null) {
            message.setMessage(notAdminMessage);
            return Response.status(200).entity(message).build();
        }
        if (session.getAttribute("customer")==null && session.getAttribute("employee") == null) {
            message.setMessage(notLoggedInMessage);
            return Response.status(200).entity(message).build();
        }
        if (!Arrays.asList(states).contains(state)||!zip.matches("[a-zA-Z0-9]*")) {
            message.setMessage(duplicateMessage);
            return Response.status(200).entity(message).build();
        }
        try {
            Transaction.begin();
            synchronized (customerDAO) {
                if (customerDAO.read(userName) != null || employeeDAO.read(userName) != null) {
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
            }
            Transaction.commit();
            message.setMessage(successMessage);
            return Response.status(200).entity(message).build();
        } catch (RollbackException e) {
            if (Transaction.isActive()) Transaction.rollback();
            String rollBackMessage = "Transaction roll back";
            message.setMessage(rollBackMessage);
            return Response.status(400).entity(message).build();
        } finally {
            if(Transaction.isActive())Transaction.rollback();
        }
    }
}
