package mf.rest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/logout")
public class LogoutAction {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response performPost(@Context HttpServletRequest request) {
        System.out.println("Logout");
        HttpSession session = request.getSession();
        ResponseMessage message = new ResponseMessage();
        if(session.getAttribute("customer") == null && session.getAttribute("employee")==null) {
            String failureMessage = "You are not currently logged in";
            message.setMessage(failureMessage);
            return Response.status(200).entity(message).build();
        }
        session.invalidate();
        String successMessage = "You have been successfully logged out";
        message.setMessage(successMessage);
        return Response.status(200).entity(message).build();
    }
}
