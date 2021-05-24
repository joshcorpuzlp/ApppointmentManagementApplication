package Model;

public class Division {
    private int divisionId;
    private String division;
    private int countryId;
    private String country;

    //we are not CREATING ANY DIVISIONS

    public int getDivisionId() {
        return divisionId;
    }

    public String getDivision() {
        return division;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Division class constructor
     * @param divisionId Passes an integer value
     * @param division Passes a String object
     * @param countryId Passes a an integer value.
     */
    public Division(int divisionId, String division, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
    }
    /**
     * Division class constructor overload
     * @param divisionId Passes an integer value
     * @param division Passes a String object
     * @param countryId Passes a an integer value.
     */
    public Division(int divisionId, String division, int countryId, String country) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
        this.country = country;
    }
}
