package Model;

public class Customer {
    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private int customerDivisionId;
    private String customerDivision;
    private String phoneNumber;


    //setters and getters for the Customer class
    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    public int getCustomerDivisionId() {
        return customerDivisionId;
    }

    public String getCustomerDivision() {
        return customerDivision;
    }

    public void setCustomerDivision(String customerDivision) {
        this.customerDivision = customerDivision;
    }





    //constructor
    public Customer(int customerId, String customerName,
                    String customerAddress, String customerPostalCode, int customerDivisionId,
                    String customerDivision, String phoneNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerDivisionId = customerDivisionId;
        this.customerDivision = customerDivision;

    }








}
