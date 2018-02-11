package mf.rest;

import java.util.Date;
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
import org.genericdao.Transaction;
import mf.databean.FundBean;
import mf.databean.FundPriceHistoryBean;
import mf.model.FundDAO;
import mf.model.FundPriceHistoryDAO;
import mf.model.Model;

@Path("/createFund")
public class CreateFundAction {
    private FundDAO fundDAO;
    private FundPriceHistoryDAO fundPriceHistoryDAO;
    private Model model;
    public void init() {
        model = new Model();
        fundDAO = model.getFundDAO();
        fundPriceHistoryDAO = model.getFundPriceHistoryDAO();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response performPost(JsonObject object,@Context HttpServletRequest request) {
        if(model == null)init();
        ResponseMessage message = new ResponseMessage();
        String name = object.get("name").toString().replaceAll("\"", "");
        String symbol = object.get("symbol").toString().replaceAll("\"", "");
        String initial_value = object.get("initial_value").toString().replaceAll("\"", "");
        double value = 0;
        try {
            value = Double.parseDouble(initial_value);
        }catch(NumberFormatException e) {
            String notDoubleMessage = "The initial value is not valid number";
            message.setMessage(notDoubleMessage);
            return Response.status(403).entity(message).build();
        }
        String successMessage = "The fund was successfully created";
        String notLoggedInMessage = "You are not currently logged in";
        String notAdminMessage = "You must be an employee to perform this action";
        HttpSession session = request.getSession();
        
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
            if (fundDAO.match(MatchArg.or(
                    MatchArg.equalsIgnoreCase("symbol", symbol),
                    MatchArg.equalsIgnoreCase("name", name))).length != 0) {
                String alreadyExistMessage = "This fund already exist";
                message.setMessage(alreadyExistMessage);
                return Response.status(403).entity(message).build();
            }
            FundBean fund = new FundBean();
            fund.setName(name);
            fund.setSymbol(symbol);
            fund.setLatestPrice(value);
            fundDAO.create(fund);
            FundPriceHistoryBean history = new FundPriceHistoryBean();
            history.setPrice(value);
            history.setPriceDate(new Date());
            fundPriceHistoryDAO.create(history);
            Transaction.commit();
            message.setMessage(successMessage);
            return Response.status(200).entity(message).build();
        } catch (RollbackException e) {
            String rollBackMessage = "Transaction roll back";
            message.setMessage(rollBackMessage);
            return Response.status(403).entity(message).build();
        } finally {
            if(Transaction.isActive())Transaction.rollback();
        }
    }
}
