package ulbs.licenta.car_tracker.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ulbs.licenta.car_tracker.persistence.entities.CarEntity;
import ulbs.licenta.car_tracker.persistence.entities.LocationEntity;

public class CarDAO {
	private DBConnector dbConnector = DBConnector.getInstace();
	
	/**
	 * 
	 * @param carEntity
	 * @return
	 * @throws SQLException
	 */
	public boolean createCar(final CarEntity carEntity) throws SQLException {
		Connection connection = dbConnector.getDbConnection();
		String sqlQuery = "INSERT INTO user_cars (id_user, car_name, longitude, latitude) VALUES (?, ?, ?, ?)";

		PreparedStatement st = connection.prepareStatement(sqlQuery);
		st.setLong(1, carEntity.getUserId());
		st.setString(2, carEntity.getName());
		st.setDouble(3, carEntity.getCurrentLocation().getLatitude());
		st.setDouble(4, carEntity.getCurrentLocation().getLongitude());

		boolean found = st.executeUpdate() > 0;
		return found;
	}
	
	/**
	 * 
	 * @param carId
	 * @param longitude
	 * @param latitude
	 * @throws SQLException
	 */
	public boolean updateCarLocation(long carId, double longitude, double latitude) throws SQLException {
		this.doUpdateCarLocation(carId, longitude, latitude);
		this.doInserCarLocationHistory(carId, longitude, latitude);
		return true;
	}
	
	/**
	 * 
	 * @param carId
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public CarEntity getUserCar(long carId, long userId) throws SQLException {
		Connection connection = dbConnector.getDbConnection();
		String sqlQuery = "SELECT * FROM user_cars WHERE id = ? AND id_user = ?";

		PreparedStatement st = connection.prepareStatement(sqlQuery);
		st.setLong(1, carId);
		st.setLong(2, userId);
		
		ResultSet rs = st.executeQuery();
		
		CarEntity entity = null;
		if(rs.next()) {
			entity = this.getCarEntityFromResultSet(rs);
		}
		return entity;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public List<CarEntity> getUsersAllCars(long userId) throws SQLException {
		Connection connection = dbConnector.getDbConnection();
		String sqlQuery = "SELECT * FROM user_cars WHERE id_user = ?";

		PreparedStatement st = connection.prepareStatement(sqlQuery);
		st.setLong(1, userId);
		
		ResultSet rs = st.executeQuery();
		
		List<CarEntity> entityList = new ArrayList<>();
		while(rs.next()) {
			final CarEntity entity = this.getCarEntityFromResultSet(rs);
			entityList.add(entity);
		}
		return entityList;
	}

	private CarEntity getCarEntityFromResultSet(ResultSet rs) throws SQLException {
		final CarEntity entity = new CarEntity();
		entity.setCarId(rs.getLong("id"));
		entity.setCurrentLocation(new LocationEntity(rs.getDate("timestamp"), rs.getDouble("longitude"), rs.getDouble("latitude")));
		entity.setName(rs.getString("car_name"));
		entity.setUserId(rs.getLong("id_user"));
		return entity;
	}

	private boolean doUpdateCarLocation(long carId, double longitude, double latitude) throws SQLException {
		Connection connection = dbConnector.getDbConnection();

		String sqlQuery = "UPDATE user_cars SET longitude = ?, latitude = ? WHERE id = ?";

		PreparedStatement st = connection.prepareStatement(sqlQuery);
		st.setDouble(1, longitude);
		st.setDouble(2, latitude);
		st.setLong(3, carId);

		st.executeUpdate();
		return true;
	}

	private boolean doInserCarLocationHistory(long carId, double longitude, double latitude) throws SQLException {
		Connection connection = dbConnector.getDbConnection();

		String sqlQuery = "INSERT INTO cars_history (`id_car`, `longitude`, `latitude`) VALUES(?, ?, ?)";

		PreparedStatement st = connection.prepareStatement(sqlQuery);
		st.setLong(1, carId);
		st.setDouble(2, longitude);
		st.setDouble(3, latitude);

		st.executeUpdate();
		return true;
	}

}
