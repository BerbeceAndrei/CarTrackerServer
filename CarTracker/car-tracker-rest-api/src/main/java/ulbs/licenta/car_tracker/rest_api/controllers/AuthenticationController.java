package ulbs.licenta.car_tracker.rest_api.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ulbs.licenta.car_tracker.business.manager.UserManager;
import ulbs.licenta.car_tracker.commons.dto.UserDTO;
import ulbs.licenta.car_tracker.rest_api.model.response.ErrorMessageModel;
import ulbs.licenta.car_tracker.rest_api.model.response.UserModel;

@Path("/auth")
public class AuthenticationController {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private UserManager userManager = new UserManager();

	/**
	 * Login the user.
	 * 
	 * @param userModel
	 * @return
	 */
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@FormParam("email") final String email, @FormParam("password") final String password) {
		// try to login the user
		final UserDTO userDTO = userManager.loginUser(email, password);
		
		// if the logged in user id is not -1 then the user has been logged in
		if (userDTO != null) {
			final UserModel loggedInUserModel = new UserModel();
			loggedInUserModel.setId(userDTO.getId());
			loggedInUserModel.setEmail(email);
			loggedInUserModel.setFirstName(userDTO.getFirstName());
			loggedInUserModel.setLastName(userDTO.getLastName());
			
			try {
				return Response.status(Status.OK)
						.entity(OBJECT_MAPPER.writeValueAsString(loggedInUserModel))
						.build();
			} catch (JsonProcessingException e) {
				return Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity(e.toString())
						.build();
			}
		} else {
			final ErrorMessageModel errorMessage = new ErrorMessageModel();
			errorMessage.setErrorMessage("username or password is incorrect");
			return Response.status(Status.UNAUTHORIZED)
					.entity(errorMessage)
					.build();
		}
	}
	
	/**
	 * Signup user.
	 * 
	 */
	@PUT
	@Path("/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signup(final UserModel userModel) {
		if (userModel == null || userModel.getFirstName() == null || userModel.getLastName() == null || userModel.getPassword().trim().isEmpty()) {
			final ErrorMessageModel errorMessage = new ErrorMessageModel();
			errorMessage.setErrorMessage("user model incorrect");
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(errorMessage)
					.build();
		} else {
			final UserDTO userDTO = new UserDTO();
			userDTO.setEmail(userModel.getEmail());
			userDTO.setFirstName(userModel.getFirstName());
			userDTO.setLastName(userModel.getLastName());
			userDTO.setPassword(userModel.getPassword());
			
			final UserDTO createdUserDTO = userManager.createUser(userDTO);
			if (createdUserDTO == null) {
				return Response.status(Status.NOT_FOUND).build();
			} else {
				return Response.status(Status.OK).entity(userModel).build();
			}
		}
	}

}
