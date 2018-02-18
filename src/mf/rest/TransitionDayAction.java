package mf.rest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;
import mf.databean.FundBean;
import mf.model.FundDAO;
import mf.model.Model;

@Path("/transitionDay")
public class TransitionDayAction {
    private static Model model;
    private static FundDAO fundDAO;
    
    public void init() {
        model = MyApplication.getModel();
        fundDAO = model.getFundDAO();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response performPost(@Context HttpServletRequest request) {
        System.out.println("Transition Day");
        if(model == null)init();
        String successMessage = "The fund prices have been successfully recalculated";
        String notLoggedInMessage = "You are not currently logged in";
        String notAdminMessage = "You must be an employee to perform this action";
        HttpSession session = request.getSession();
        ResponseMessage message = new ResponseMessage();
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
            FundBean[] funds = fundDAO.match();
            for(FundBean fund:funds) {
                fund.setLatestPrice(generateNewPrice(fund));
                fundDAO.update(fund);
            }
            Transaction.commit();
        } catch (RollbackException e) {
            String rollBackMessage = "Transaction roll back"+e.getMessage();
            message.setMessage(rollBackMessage);
            return Response.status(400).entity(message).build();
        } finally {
            if(Transaction.isActive())Transaction.rollback();
        }
        message.setMessage(successMessage);
        return Response.status(200).entity(message).build();
    }
    
    public double generateNewPrice(FundBean fund) {
        double oldPrice = fund.getLatestPrice();
        double upper = oldPrice * 1.1;
        double lower = oldPrice * 0.9;
        double gap = upper - lower;
        return Math.random() * gap + lower;
    }
}
