package gui;

import model.Person;

import javax.swing.table.AbstractTableModel;
import java.util.List;


public class PersonTableModel extends AbstractTableModel {

    private List<Person> database;
    private String [] colNames = {"ID","Name","Occupation","Age Category","Employment Category",
            "US Citizen","Gender","Tax ID",};


    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }

    public PersonTableModel() {
    }

    public void setData(List<Person> database) {
        this.database = database;
    }

    @Override
    public int getRowCount() {
        return database.size();
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Person person = database.get(row);
        switch (column) {
            case 0:
                return person.getId();
            case 1:
                return person.getName();
            case 2:
                return person.getOccupation();
            case 3:
                return person.getAgeCategory();
            case 4:
                return person.getEmpCat();
            case 5:
                return person.isUsCitizen();
            case 6:
                return person.getGender();
            case 7:
                return person.getTaxId();
        }
        return null;
    }
}
