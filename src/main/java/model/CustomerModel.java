package model;

public class CustomerModel {
    private final String cellphone;
    private final String name;
    private final String address;
    private final String dlicense;


    public CustomerModel(String cellphone, String name, String address, String dlicense) {
        this.cellphone = cellphone;
        this.name = name;
        this.address = address;
        this.dlicense = dlicense;
    }

    public String getDlicense() {
        return dlicense;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getCellphone() {
        return cellphone;
    }

    @Override
    public String toString() {
        return "CustomerModel{" +
                "cellphone='" + cellphone + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", dlicense='" + dlicense + '\'' +
                '}';
    }
}
