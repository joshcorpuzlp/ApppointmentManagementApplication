package Model;

public class Division {
    private int divisionId;
    private String division;
    private int countryId;

    //we are not CREATING ANY DIVISIONS

    public int getDivisionId() {
        return divisionId;
    }

    public String getDivision() {
        return division;
    }

    public Division(int divisionId, String division, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
    }
}
