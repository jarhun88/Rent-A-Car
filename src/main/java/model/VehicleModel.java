package model;

public class VehicleModel {
    private final int vid ;
    private final String vlicense;
    private final String make;
    private final String model;
    private final String year;
    private final String color;
    private final double odometer;
    private final String status ;
    private final String vtname;
    private final String location;
    private final String city;

    public VehicleModel(int vid, String vlicense, String make, String model, String year, String color,
                        double odometer, String status, String vtname, String location, String city) {
        this.vid = vid;
        this.vlicense = vlicense;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.odometer = odometer;
        this.status = status;
        this.vtname = vtname;
        this.location = location;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public String getLocation() {
        return location;
    }

    public String getVtname() {
        return vtname;
    }

    public String getStatus() {
        return status;
    }

    public double getOdometer() {
        return odometer;
    }

    public String getColor() {
        return color;
    }

    public String getYear() {
        return year;
    }

    public String getModel() {
        return model;
    }

    public String getMake() {
        return make;
    }

    public String getVlicense() {
        return vlicense;
    }

    public int getVid() {
        return vid;
    }

    @Override
    public String toString() {
        return "VehicleModel{" +
                "vid=" + vid +
                ", vlicense='" + vlicense + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year='" + year + '\'' +
                ", color='" + color + '\'' +
                ", odometer=" + odometer +
                ", status='" + status + '\'' +
                ", vtname='" + vtname + '\'' +
                ", location='" + location + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
