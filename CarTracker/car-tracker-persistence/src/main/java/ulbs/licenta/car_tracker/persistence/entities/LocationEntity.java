package ulbs.licenta.car_tracker.persistence.entities;

import java.util.Date;

public class LocationEntity {

	private Date timestamp;
	private double longitude;
	private double latitude;

	public LocationEntity(Date timestamp, double longitude, double latitude) {
		super();
		this.timestamp = timestamp;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public LocationEntity(double longitude, double latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

}
