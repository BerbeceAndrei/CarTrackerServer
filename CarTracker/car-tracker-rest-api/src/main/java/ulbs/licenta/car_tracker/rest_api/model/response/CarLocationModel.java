package ulbs.licenta.car_tracker.rest_api.model.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonInclude(Include.NON_NULL)
public class CarLocationModel {

	@JsonProperty("car_id")
	private Long carId;

	@JsonProperty("user_id")
	private Long userId;

	@JsonProperty("timestamp")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date timestamp;

	@JsonProperty("longitude")
	private double longitude;

	@JsonProperty("latitude")
	private double latitude;

	public CarLocationModel() {
		super();
	}

	public CarLocationModel(Long carId, Long userId, Date timestamp, double longitude, double latitude) {
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
