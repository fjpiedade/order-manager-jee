package phi.fjpiedade.apiorder.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;

import phi.fjpiedade.apiorder.domain.GeneralResponse;
import phi.fjpiedade.apiorder.domain.user.UserModel;
import phi.fjpiedade.apiorder.service.UserService;

import jakarta.inject.Inject;

@Path("/user")
@RequestScoped
public class UserResource {
    private UserService userService;

    public UserResource() {
    }

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserModel> getUsers() {
        return userService.getAllUsers();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserModel user) {
        UserModel createdUser = userService.createUser(user);
        if (createdUser == null) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(Collections.singletonMap("message", "Email of User already Exist!"))
//                    .build();

            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "Email of User already exists!",
                    null
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.CREATED.getStatusCode(),
                Response.Status.CREATED.toString(),
                "User created successfully",
                createdUser
        );

        return Response.status(Response.Status.CREATED).entity(successResponse).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id) {
        UserModel user = userService.getUserById(id);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "User not found"))
                    .build();
        }
        return Response.ok(user).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, UserModel updatedUser) {
        UserModel user = userService.updateUser(id, updatedUser);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "User not found"))
                    .build();
        }
        return Response.ok(user).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Long id) {
        boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "User not found"))
                    .build();
        }
        return Response.ok(Collections.singletonMap("message", "User deleted successfully")).build();
    }
}
