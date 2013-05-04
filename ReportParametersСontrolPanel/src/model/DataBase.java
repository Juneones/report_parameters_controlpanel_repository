package model;

import java.io.*;
import java.sql.*;
import java.util.*;


public class DataBase {
    //  we changed the ArrayList to LinkedList - it is made for add
// and deleting item from (quicker)
    private List<Person> people;
    private Connection connection;

    public DataBase() {
        people = new LinkedList<Person>();
    }

    public void connect() throws Exception {
        if (connection != null) return;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver not found");
        }

        String url = "jdbc:mysql://localhost:3306/my_desktop_schema";

        connection = DriverManager.getConnection(url, "root",
                "ROOT_root_123456");
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Can't close connection");
            }


        }

    }

    public void save() throws SQLException {
        String checkSql = "SELECT COUNT(*) AS COUNT FROM people WHERE id=?";
        PreparedStatement checkStmt = connection.prepareStatement(checkSql);

        String insertSql = "INSERT INTO people " +
                "(id,NAME,age, employment_status,tax_id,us_citizen,gender,occupation) VALUES(?,?,?,?,?,?,?,?)";

        PreparedStatement insertStatement = connection.prepareStatement(insertSql);

        String updateSql = "UPDATE people SET NAME=?,age=?,employment_status=?,tax_id=?,us_citizen=?,gender=?,occupation=? WHERE id=?";
        PreparedStatement updateStatement = connection.prepareStatement(updateSql);

        for (Person person : people) {
            int id = person.getId();
            String name = person.getName();
            String occupation = person.getOccupation();
            AgeCategory age = person.getAgeCategory();
            EmploymentCategory emp = person.getEmpCat();
            String tax = person.getTaxId();
            boolean isUs = person.isUsCitizen();
            Gender gender = person.getGender();


            checkStmt.setInt(1, id);

            ResultSet checkResult = checkStmt.executeQuery();
            checkResult.next();
            int count = checkResult.getInt(1);

            if (count == 0) {

                System.out.println("Inserting person with ID " + id);

                int col = 1;
                insertStatement.setInt(col++, id);
                insertStatement.setString(col++, name);
                insertStatement.setString(col++, age.name());
                insertStatement.setString(col++, emp.name());
                insertStatement.setString(col++, tax);
                insertStatement.setBoolean(col++, isUs);
                insertStatement.setString(col++, gender.name());
                insertStatement.setString(col++, occupation);

                insertStatement.executeUpdate();
            } else {
                System.out.println("Updating person with ID " + id);

                int col = 1;

                updateStatement.setString(col++, name);
                updateStatement.setString(col++, age.name());
                updateStatement.setString(col++, emp.name());
                updateStatement.setString(col++, tax);
                updateStatement.setBoolean(col++, isUs);
                updateStatement.setString(col++, gender.name());
                updateStatement.setString(col++, occupation);
                updateStatement.setInt(col++, id);

                updateStatement.executeUpdate();
            }
//            System.out.println("Count for person with ID " + id + " is " + count);
        }
        updateStatement.close();
        insertStatement.close();
        checkStmt.close();
    }

    public void load() throws SQLException {
        people.clear();
        String sql = "SELECT id,NAME,age, employment_status,tax_id,us_citizen,gender,occupation FROM people ORDER BY NAME";
        Statement selectStatement = connection.createStatement();
        ResultSet results = selectStatement.executeQuery(sql);

        while (results.next()) {

            int id = results.getInt("id");
            String name = results.getString("name");
            String age = results.getString("age");
            String emp = results.getString("employment_status");
            String taxId = results.getString("tax_id");
            boolean isUs = results.getBoolean("us_citizen");
            String gender = results.getString("gender");
            String occ = results.getString("occupation");

            Person person = new Person(id, name, occ, AgeCategory.valueOf(age), EmploymentCategory.valueOf(emp), taxId, isUs, Gender.valueOf(gender));
            people.add(person);

            System.out.println(person);
        }


        results.close();
        selectStatement.close();

    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public void removePerson(int index) {
        people.remove(index);
    }

    public List<Person> getPeople() {
        return Collections.unmodifiableList(people);// prevent
        // other classes from modifying list by the reference
        // they retrieve
    }

    public void saveToFile(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        Person[] persons = people.toArray(new Person[people.size()]);
        oos.writeObject(persons);
        oos.close();
    }

    public void loadFromFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        try {
            Person[] persons = (Person[]) ois.readObject();
            people.clear();
            people.addAll(Arrays.asList(persons));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ois.close();
    }
}
