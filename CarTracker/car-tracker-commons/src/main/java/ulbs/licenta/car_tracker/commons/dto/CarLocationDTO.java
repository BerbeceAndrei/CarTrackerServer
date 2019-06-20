package ulbs.licenta.car_tracker.commons.dto;

import java.util.Date;

public class CarLocationDTO {

	private Long carId;
	private Long userId;
	private Date timestamp;
	private double longitude;
	private double latitude;

	public CarLocationDTO() {
		super();
	}

	public CarLocationDTO(Long carId, Long userId, Date timestamp, double longitude, double latitude) {
		super();
		this.carId = userId;
		this.timestamp = timestamp;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

}
