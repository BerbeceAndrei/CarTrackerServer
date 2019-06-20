package ulbs.licenta.car_tracker.rest_api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ulbs.licenta.car_tracker.business.manager.CarManager;
import ulbs.licenta.car_tracker.commons.dto.CarDTO;
import ulbs.licenta.car_tracker.commons.dto.CarLocationDTO;
import ulbs.licenta.car_tracker.rest_api.model.request.CarModel;
import ulbs.licenta.car_tracker.rest_api.model.response.CarLocationModel;
import ulbs.licenta.car_tracker.rest_api.model.response.ErrorMessageModel;
import ulbs.licenta.car_tracker.rest_api.model.response.SuccessMessageModel;

@Path("/cars")
public class CarController {

	private CarManager carManager = new CarManager();
	
	/**
	 * Get the current location for all the user's cars.
	 * 
	 * @param userId
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCar(final CarModel carModel) {
		if (carModel == null || carModel.getName() == null || carModel.getName().trim().isEmpty()) {
			final ErrorMessageModel errorMessage = new ErrorMessageModel();
			errorMessage.setErrorMessage("car model incorrect");
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(errorMessage)
					.build();
		} else {
			final CarDTO carDTO = new CarDTO();
			carDTO.setLatitude(carModel.getLatitude());
			carDTO.setLongitude(carModel.getLongitude());
			carDTO.setName(carModel.getName());
			carDTO.setUserId(carModel.getUserId());
			
			final CarLocationDTO carLocationDTO = carManager.createCar(carDTO);
			if (carLocationDTO != null) {
				final CarLocationModel carLocationModel = new CarLocationModel();
				carLocationModel.setCarId(carLocationDTO.getCarId());
				carLocationModel.setLatitude(carLocationDTO.getLatitude());
				carLocationModel.setLongitude(carLocationDTO.getLongitude());
				carLocationModel.setTimestamp(carLocationDTO.getTimestamp());
				carLocationModel.setUserId(carLocationDTO.getUserId());
				return Response.status(Status.OK).entity(carLocationModel).build();
			} else {
				return Response.status(Status.NOT_FOUND).build();
			}
		}
	}
	
	/**
	 * Get the current location for all the user's cars.
	 * 
	 * @param userId
	 * @return
	 */
	@GET
	@Path("/location/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserAllCarsCurrentLocation(final @PathParam("user_id") String userIdStr) {
		final Long userId = this.getLong(userIdStr);
		if (userId == null ) {
			final ErrorMessageModel errorMessage = new ErrorMessageModel();
			errorMessage.setErrorMessage("user ID is incorrect");
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(errorMessage)
					.build();
		} else {
			final List<CarLocationDTO> carLocationDTOList = carManager.getUserAllCarsCoordinates(userId);
			
			final List<CarLocationModel> carLocationModelList = carLocationDTOList
					.stream()
					.map(dto -> new CarLocationModel(dto.getCarId(), userId, dto.getTimestamp(), dto.getLongitude(), dto.getLatitude()))
					.collect(Collectors.toList());
			return Response.status(Status.OK)
					.entity(carLocationModelList)
					.build();
		}
	}
	
	/**
	 * Get the current location for the user's specified car.
	 * 
	 * @param userId
	 * @param carId
	 * @return
	 */
	@GET
	@Path("/location/{user_id}/{car_id}/current")
	@Produces(MediaType.APPLICATION_JSON)
	public Response  getUserCurrentCarLocation(final @PathParam("user_id") String userIdStr,
			final @PathParam("car_id") String carIdStr) {
		final Long userId = this.getLong(userIdStr);
		final Long carId = this.getLong(carIdStr);
		
		if (userId == null || carId == null) {
			final ErrorMessageModel errorMessage = new ErrorMessageModel();
			errorMessage.setErrorMessage("user ID or car ID values are incorrect");
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(errorMessage)
					.build();
		} else {
			final CarLocationDTO carLocationDTO = carManager.getUserCurrentCarLocation(userId, carId);
			
			if (carLocationDTO != null) {
				final CarLocationModel carLocationModel = new CarLocationModel();
				carLocationModel.setCarId(carId);
				carLocationModel.setLatitude(carLocationDTO.getLatitude());
				carLocationModel.setLongitude(carLocationDTO.getLongitude());
				carLocationModel.setTimestamp(carLocationDTO.getTimestamp());
				carLocationModel.setUserId(userId);
				
				return Response.status(Status.OK)
						.entity(carLocationModel)
						.build();
			} else {
				return Response.status(Status.NOT_FOUND).build();
			}
		}
	}
	
	/**
	 * Saves the user's car coordinates.
	 * 
	 * @param userId
	 * @param carId
	 * @param carCoordinate
	 * @return
	 */
	@GET
	@Path("/location/{user_id}/{car_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveOrUpdateLocation(final @PathParam("user_id") String userIdStr,
			final @PathParam("car_id") String carIdStr,
			final @QueryParam("long") String longitudeStr,
			final @QueryParam("lat") String latitudeStr) {
		final Long userId = this.getLong(userIdStr);
		final Long carId = this.getLong(carIdStr);
		final Double longitude = this.getDouble(longitudeStr);
		final Double latitude = this.getDouble(latitudeStr);
		
		if (userId == null || carId == null || longitude == null || latitude == null) {
			final ErrorMessageModel errorMessage = new ErrorMessageModel();
			errorMessage.setErrorMessage("one or more mandatory fields are missing");
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(errorMessage)
					.build();
		} else {
			final boolean saved = carManager.saveOrUpdateLocation(carId, longitude, latitude);
			if (saved) {
				final SuccessMessageModel successMessage = new SuccessMessageModel();
				successMessage.setSuccessMessage("car coordinates have been updated");
				return Response.status(Status.CREATED)
						.entity(successMessage)
						.build();
			} else {
				final ErrorMessageModel errorMessage = new ErrorMessageModel();
				errorMessage.setErrorMessage("could not update car coordinates, please try again");
				return Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity(errorMessage)
						.build();
			}
		}
	}
	
	private Long getLong(String pathParamStr) {
		Long pathParam = null;
		try {
			if (pathParamStr != null) {
				pathParam = Long.valueOf(pathParamStr);
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		return pathParam;
	}
	
	private Double getDouble(String pathParamStr) {
		Double pathParam = null;
		try {
			if (pathParamStr != null) {
				pathParam = Double.valueOf(pathParamStr);
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		return pathParam;
	}
}
