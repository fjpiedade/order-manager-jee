package phi.fjpiedade.api8demo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import phi.fjpiedade.api8demo.dominio.user.UserModel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Path("/user")
public class UserResource {
//    @GET
//    @Produces("text/plain")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String hello() {return "Hello, User Resource";}

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserModel> getUser() {
        return em.createQuery("SELECT o FROM UserModel o", UserModel.class).getResultList();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserModel user) {
        em.persist(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }
}
