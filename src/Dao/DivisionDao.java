package Dao;

import Model.AppointmentManager;
import Model.Customer;
import Model.Division;
import Utility.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DivisionDao implements Dao<Division> {

    private ObservableList<Division> tempDivisionHolder = FXCollections.observableArrayList();
    private Division tempDivision;

    @Override
    public void loadDbObjects() {
        String query = "SELECT * FROM first_level_divisions;";

        try {
            ResultSet rs = dbConnection.getStatement().executeQuery(query);
            while (rs.next()) {
                int divisionId = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                int countryId = rs.getInt("Country_ID");

                tempDivision = new Division(divisionId, division, countryId);
                tempDivisionHolder.add(tempDivision);

                //Hashmap created for a keyValue Pair of DivisionID and Division
                //need to ensure that the code only runs once
                AppointmentManager.addToHashMap(tempDivision.getDivision(), tempDivision.getDivisionId());
            }

            AppointmentManager.setDivisions(tempDivisionHolder);



        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //console viewer of the effects. can be removed later.
        for (Division d : AppointmentManager.getAllDivisions()) {
            System.out.println(d.getDivisionId());
        }
        System.out.println(AppointmentManager.getAllHashMaps());

    }

    @Override
    public void addObject(Division division) throws SQLException {

    }

    @Override
    public void modifyObject(Division division) {

    }

    @Override
    public void removeObject(Division division) {

    }
}
