package fr.ihm.accidents;

public class Accident {

    private final double latitude;
    private final double longitude;

    public Accident(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
