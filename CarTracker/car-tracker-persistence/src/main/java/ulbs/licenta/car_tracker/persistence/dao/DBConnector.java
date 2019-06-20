package ulbs.licenta.car_tracker.persistence.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

	private static DBConnector INSTANCE = new DBConnector();
	private Connection connection = null;

	private DBConnector() {
		super();
		String url = "jdbc:mysql://localhost:3306/car_trackerdb";
		String user = "root";
		String password = "root";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static DBConnector getInstace() {
		return INSTANCE;
	}

	public Connection getDbConnection() {
		return connection;
	}
}
