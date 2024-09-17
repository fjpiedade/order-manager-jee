package phi.fjpiedade.apiorder.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.transaction.Transactional;

import java.util.List;

import phi.fjpiedade.apiorder.domain.GeneralResponse;
import phi.fjpiedade.apiorder.domain.user.UserModel;
import phi.fjpiedade.apiorder.service.UserService;

import jakarta.inject.Inject;

@Path("/user")
@RequestScoped
public class UserResource {
    @Inject
    private UserService userService;

    public UserResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        List<UserModel> listOfUsers = userService.getAllUsers();
        if (listOfUsers.isEmpty()) {
            GeneralResponse successResponse = new GeneralResponse(
                    Response.Status.OK.getStatusCode(),
                    Response.Status.OK.toString(),
                    "List of Users is Empty!",
                    null);
            return Response.status(Response.Status.OK).entity(successResponse).build();
        }
        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "User(s) retrieves successfully",
                listOfUsers);
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserModel user) {
        UserModel createdUser = userService.createUser(user);
        if (createdUser == null) {
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
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "User not found",
                    null);
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "User retrieve successfully",
                user
        );
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, UserModel updatedUser) {
        UserModel userUpdated = userService.updateUser(id, updatedUser);
        if (userUpdated == null) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "User not found!",
                    null
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "User updated successfully",
                userUpdated
        );
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Long id) {
        boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "User not found!",
                    null
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "User deleted successfully",
                null
        );
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }
}
