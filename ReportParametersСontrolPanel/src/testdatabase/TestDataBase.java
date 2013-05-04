package testdatabase;


import model.*;

import java.sql.SQLException;

public class TestDataBase {
    public static void main(String[] args) {
        System.out.println("Running database test");

        DataBase db = new DataBase();
        try {
            db.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }


        db.addPerson(new Person("Joe", "lion tamer", AgeCategory.adult, EmploymentCategory.employed, "777", true, Gender.male));
        db.addPerson(new Person("Sue", "junior java developer", AgeCategory.senior, EmploymentCategory.selfEmployed, null, false, Gender.female));
        try {
            db.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.load();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.disconnect();
    }
}
