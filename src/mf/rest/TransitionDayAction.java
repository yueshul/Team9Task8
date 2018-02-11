package mf.rest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;
import mf.databean.FundBean;
import mf.databean.FundPriceHistoryBean;
import mf.model.FundDAO;
import mf.model.FundPriceHistoryDAO;
import mf.model.Model;

@Path("/transitionDay")
public class TransitionDayAction {
    private Model model;
    private FundDAO fundDAO;
    private FundPriceHistoryDAO fundPriceHistoryDAO;
    
    public void init() {
        model = new Model();
        this.fundDAO = model.getFundDAO();
        this.fundPriceHistoryDAO = model.getFundPriceHistoryDAO();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response performPost(HttpServletRequest request) {
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
            FundPriceHistoryBean bean = new FundPriceHistoryBean();
            FundBean[] funds = fundDAO.match();
            for(FundBean fund:funds) {
                int fundId = fund.getFundId();
                double oldPrice = fund.getLatestPrice();
                double upper = oldPrice * 1.1;
                double lower = oldPrice * 0.9;
                double gap = upper - lower;
                double newPrice = Math.random() * gap + lower;
                bean.setFundId(fundId);
                bean.setPrice(newPrice);
                fundPriceHistoryDAO.create(bean);
                fund.setLatestPrice(newPrice);
                fundDAO.update(fund);
            }
            Transaction.commit();
        } catch (RollbackException e) {
            String rollBackMessage = "Transaction roll back";
            message.setMessage(rollBackMessage);
            return Response.status(403).entity(message).build();
        } finally {
            if(Transaction.isActive())Transaction.rollback();
        }
        message.setMessage(successMessage);
        return Response.status(200).entity(message).build();
    }
}
