package ulbs.licenta.car_tracker.persistence.entities;

public class CarEntity {

	private long carId;
	private long userId;
	private String name;
	private LocationEntity currentLocation;
	
	
	public long getCarId() {
		return carId;
	}

	public void setCarId(long carId) {
		this.carId = carId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocationEntity getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(LocationEntity currentLocation) {
		this.currentLocation = currentLocation;
	}


		
	
}
