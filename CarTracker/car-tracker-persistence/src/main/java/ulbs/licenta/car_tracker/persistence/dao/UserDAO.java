package ulbs.licenta.car_tracker.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ulbs.licenta.car_tracker.persistence.entities.UserEntity;

public class UserDAO {

	private DBConnector dbConnector = DBConnector.getInstace();

	// login
	// create user
	public boolean login(final UserEntity userEntity) throws SQLException {
		Connection connection = dbConnector.getDbConnection();
		String sqlQuery = "SELECT * FROM users WHERE email = ? AND password = ?";

		PreparedStatement st = connection.prepareStatement(sqlQuery);
		st.setString(1, userEntity.getEmail());
		st.setString(2, userEntity.getPassword());

		ResultSet rs = st.executeQuery();
		if (rs.next()) {
			userEntity.setId(rs.getLong("id"));
			userEntity.setFirstName(rs.getString("first_name"));
			userEntity.setLastName(rs.getString("last_name"));
			return true;
		}
		return false;
	}

	public UserEntity createUser(final UserEntity userEntity) throws SQLException {
		Connection connection = dbConnector.getDbConnection();
		String sqlQuery = "INSERT INTO  users (`first_name`, `last_name`, `email`, `password`) VALUES (?, ?, ?, ?)";

		PreparedStatement st = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
		st.setString(1, userEntity.getFirstName());
		st.setString(2, userEntity.getLastName());
		st.setString(3, userEntity.getEmail());
		st.setString(4, userEntity.getPassword());
		int affectedRows = st.executeUpdate();
        if (affectedRows == 0) {
        	return null;
        }
        
        try (ResultSet generatedKeys = st.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                userEntity.setId(generatedKeys.getLong(1));
                return userEntity;
            } else {
            	return null;
            }
        }
	}
}
