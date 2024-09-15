package phi.fjpiedade.apiorder;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/")
@RequestScoped
public class HelloResource {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Welcome to Order Manager Application";
    }
}