package controller;

import gui.FormEvent;
import model.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class Controller {
    DataBase dataBase = new DataBase();

    public List<Person> getPeople(){
      return dataBase.getPeople();
    }



//    public DataBase getDataBase() {
//        return dataBase;
//    }

//    public void setDataBase(DataBase dataBase) {
//        this.dataBase = dataBase;
//    }

    public void removePerson(int index){
       dataBase.removePerson(index);
    }

    public void save() throws SQLException {
        dataBase.save();
    }
    public void load() throws SQLException {
        dataBase.load();
    }
    public void connect() throws Exception {
        dataBase.connect();

    }
    public void disconnect(){
        dataBase.disconnect();
    }
    public void addPerson(FormEvent event) {
        String name = event.getName();
        String occupation = event.getOccupation();
        int ageCatId = event.getAgeCategory();
        String empCat = event.getEmploymentCategory();
        boolean isUs = event.isUsCitizen();
        String taxId = event.getTaxId();
        String gender = event.getGender();

        AgeCategory ageCategory = null;
        switch (ageCatId) {
            case 0:
                ageCategory = AgeCategory.child;
                break;
            case 1:
                ageCategory = AgeCategory.adult;
                break;
            case 2:
                ageCategory = AgeCategory.senior;
                break;
        }
        EmploymentCategory empCategory;
        if (empCat.equals("employed")) {
            empCategory = EmploymentCategory.employed;
        } else if (empCat.equals("self-employed")) {
            empCategory = EmploymentCategory.selfEmployed;
        } else if (empCat.equals("unemployed")) {
            empCategory = EmploymentCategory.unemployed;
        } else {
            empCategory = EmploymentCategory.other;
            System.out.println(empCat);
        }
        Gender genderCat;
        if (gender.equals("male")) {
            genderCat = Gender.male;
        } else {
            genderCat = Gender.female;
        }
        Person person = new Person(name, occupation,
                ageCategory, empCategory,
                taxId, isUs, genderCat);

        dataBase.addPerson(person);
    }

    public void saveToFile(File file) throws IOException {
        dataBase.saveToFile(file);
    }
    public void loadfromFile(File file) throws IOException{
        dataBase.loadFromFile(file);

    }

}
