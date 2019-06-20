package ulbs.licenta.car_tracker.business.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ulbs.licenta.car_tracker.commons.dto.CarDTO;
import ulbs.licenta.car_tracker.commons.dto.CarLocationDTO;
import ulbs.licenta.car_tracker.persistence.dao.CarDAO;
import ulbs.licenta.car_tracker.persistence.entities.CarEntity;
import ulbs.licenta.car_tracker.persistence.entities.LocationEntity;

public class CarManager {
	private final CarDAO carDAO = new CarDAO();
	
	/**
	 * 
	 * @param carDTO
	 * @return
	 */
	public CarLocationDTO createCar(CarDTO carDTO) {
		CarLocationDTO output = null;
		try {
			final CarEntity carEntity = new CarEntity();
			carEntity.setUserId(carDTO.getUserId());
			carEntity.setName(carDTO.getName());
			carEntity.setCurrentLocation(new LocationEntity(carDTO.getLongitude(), carDTO.getLatitude()));
			
			if (carDAO.createCar(carEntity)) {
				output = new CarLocationDTO();
				output.setCarId(carEntity.getCarId());
				output.setLatitude(carEntity.getCurrentLocation().getLatitude());
				output.setLongitude(carEntity.getCurrentLocation().getLongitude());
				output.setTimestamp(carEntity.getCurrentLocation().getTimestamp());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return output;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<CarLocationDTO> getUserAllCarsCoordinates(long userId) {
		List<CarLocationDTO> output = new ArrayList<CarLocationDTO>();
		try {
			List<CarEntity> userCars = carDAO.getUsersAllCars(userId);
			output = userCars
				.stream()
				.map(entity ->  {
					final LocationEntity location = entity.getCurrentLocation();
						return new CarLocationDTO(entity.getCarId(), entity.getUserId(), location.getTimestamp(), location.getLongitude(), location.getLatitude()); 
					})
				.collect(Collectors.toList());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return output;
	}
	
	/**
	 * 
	 * @param carId
	 * @param userId
	 * @return
	 */
	public CarLocationDTO getUserCurrentCarLocation(long carId, long userId) {
		CarLocationDTO output = null;
		try {
			CarEntity carEntity = carDAO.getUserCar(carId, userId);
			if (carEntity != null) {
				output = new CarLocationDTO();
				output.setCarId(carEntity.getCarId());
				output.setLatitude(carEntity.getCurrentLocation().getLatitude());
				output.setLongitude(carEntity.getCurrentLocation().getLongitude());
				output.setTimestamp(carEntity.getCurrentLocation().getTimestamp());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return output;
	}
	
	/**
	 * 
	 * @param carId
	 * @param carCoordinate
	 * @throws SQLException
	 */
	public boolean saveOrUpdateLocation(long carId, double longitude, double latitude) {
		boolean saved = false;
		try {
			saved = carDAO.updateCarLocation(carId, longitude, latitude);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return saved;
	}
}
