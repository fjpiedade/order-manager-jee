package phi.fjpiedade.api8demo;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import phi.fjpiedade.api8demo.dominio.user.UserModel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Path("/user")
public class UserResource {
//    @GET
//    @Produces("text/plain")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String hello() {return "Hello, User Resource";}

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    // Get all
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserModel> getUsers() {
        return em.createQuery("SELECT o FROM UserModel o", UserModel.class).getResultList();
    }

    // Create new
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(UserModel user) {
        user.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        em.persist(user);
        em.flush();
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    // Get by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id) {
        UserModel user = em.find(UserModel.class, id);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "User not found"))
                    .build();
        }
        return Response.ok(user).build();
    }

    // Update by ID
    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, UserModel updatedUser) {
        UserModel user = em.find(UserModel.class, id);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "User not found"))
                    .build();
        }

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        em.merge(user);
        return Response.ok(user).build();
    }

    // Delete by ID
    @DELETE
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Long id) {
        UserModel user = em.find(UserModel.class, id);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "User not found"))
                    .build();
        }

        em.remove(user);
        return Response.ok(Collections.singletonMap("message", "User deleted successfully")).build();
    }
}
