package ulbs.licenta.car_tracker.business.manager;

import java.sql.SQLException;

import ulbs.licenta.car_tracker.commons.dto.UserDTO;
import ulbs.licenta.car_tracker.persistence.dao.UserDAO;
import ulbs.licenta.car_tracker.persistence.entities.UserEntity;

public class UserManager {

	private final UserDAO userDAO = new UserDAO();

	/**
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public UserDTO createUser(final UserDTO userDTO) {
		try {
			final UserEntity entity = new UserEntity();
			entity.setEmail(userDTO.getEmail());
			entity.setFirstName(userDTO.getFirstName());
			entity.setLastName(userDTO.getLastName());
			entity.setPassword(userDTO.getPassword());
			
			final UserEntity userEntity = userDAO.createUser(entity);
			if (userEntity != null) {
				userDTO.setId(entity.getId());
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userDTO;
	}

	/**
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public UserDTO loginUser(String email, String password) {
		final UserEntity entity = new UserEntity();
		entity.setEmail(email);
		entity.setPassword(password);
		try {
			if (userDAO.login(entity)) {
				UserDTO model = new UserDTO();
				model.setId(entity.getId());
				model.setEmail(entity.getEmail());
				model.setFirstName(entity.getFirstName());
				model.setLastName(entity.getLastName());
				model.setPassword(entity.getPassword());
				return model;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
