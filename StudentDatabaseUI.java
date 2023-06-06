import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentDatabaseUI extends Frame {
    // Components
    private Label idLabel, nameLabel, ageLabel, genderLabel, phoneLabel, addressLabel, searchLabel;
    private TextField idField, nameField, ageField, phoneField, addressField, searchField;
    private Choice genderChoice;
    private Button addButton, updateButton, deleteButton, clearButton, displayButton, searchButton;
    private TextArea displayArea;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dbms";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Spandan@12";
    private Connection connection;
    private PreparedStatement insertStatement, updateStatement, deleteStatement, searchStatement;
    private Statement selectStatement;

    public StudentDatabaseUI() {
        // Frame setup
        setTitle("Student Database Management");
        setSize(500, 500);
        setLayout(new GridLayout(12, 2, 10, 10)); // 12 rows, 2 columns with spacing
        setBackground(Color.lightGray);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                closeConnection();
                System.exit(0);
            }
        });

        // ID field
        idLabel = new Label("ID: ");
        idLabel.setForeground(Color.blue);
        add(idLabel);
        idField = new TextField(10);
        add(idField);

        // Name field
        nameLabel = new Label("Name: ");
        nameLabel.setForeground(Color.blue);
        add(nameLabel);
        nameField = new TextField(20);
        add(nameField);

        // Age field
        ageLabel = new Label("Age: ");
        ageLabel.setForeground(Color.blue);
        add(ageLabel);
        ageField = new TextField(3);
        add(ageField);

        // Phone number field
        phoneLabel = new Label("Phone: ");
        phoneLabel.setForeground(Color.blue);
        add(phoneLabel);
        phoneField = new TextField(10);
        add(phoneField);

        // Address field
        addressLabel = new Label("Address: ");
        addressLabel.setForeground(Color.blue);
        add(addressLabel);
        addressField = new TextField(30);
        add(addressField);

        // Gender choice
        genderLabel = new Label("Gender: ");
        genderLabel.setForeground(Color.blue);
        add(genderLabel);
        genderChoice = new Choice();
        genderChoice.add("Male");
        genderChoice.add("Female");
        genderChoice.add("Other");
        add(genderChoice);

        // Empty space for spacing
        add(new Label());
        add(new Label());

        // Add button
        addButton = new Button("Add");
        addButton.setBackground(Color.green);
        addButton.setForeground(Color.white);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addStudent();
                clearFields();
            }
        });
        add(addButton);

        // Update button
        updateButton = new Button("Update");
        updateButton.setBackground(Color.orange);
        updateButton.setForeground(Color.white);
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateStudent();
                clearFields();
            }
        });
        add(updateButton);

        // Delete button
        deleteButton = new Button("Delete");
        deleteButton.setBackground(Color.red);
        deleteButton.setForeground(Color.white);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
                clearFields();
            }
        });
        add(deleteButton);

        // Clear button
        clearButton = new Button("Clear");
        clearButton.setBackground(Color.red);
        clearButton.setForeground(Color.white);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        add(clearButton);

        // Display button
        displayButton = new Button("Display");
        displayButton.setBackground(Color.blue);
        displayButton.setForeground(Color.white);
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayStudents();
            }
        });
        add(displayButton);
        add(new Label());

        // Search label
        searchLabel = new Label("Search ID: ");
        searchLabel.setForeground(Color.blue);
        add(searchLabel);

//        add(new Label());


        // Search field
        searchField = new TextField(10);
        add(searchField);

        // Search button
        searchButton = new Button("Search");
        searchButton.setBackground(Color.cyan);
        searchButton.setForeground(Color.white);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });
        add(searchButton);

        // Display area
        displayArea = new TextArea();
        add(displayArea);

        // Establish database connection
        establishConnection();

        // Create the table if it doesn't exist
        createTable();

        // Show the UI
        setVisible(true);
    }

    private void establishConnection() {
        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Prepare the insert statement
            String insertQuery = "INSERT INTO students (id, name, age, phone, address, gender) VALUES (?, ?, ?, ?, ?, ?)";
            insertStatement = connection.prepareStatement(insertQuery);

            // Prepare the update statement
            String updateQuery = "UPDATE students SET name=?, age=?, phone=?, address=?, gender=? WHERE id=?";
            updateStatement = connection.prepareStatement(updateQuery);

            // Prepare the delete statement
            String deleteQuery = "DELETE FROM students WHERE id=?";
            deleteStatement = connection.prepareStatement(deleteQuery);

            // Prepare the select statement
            String selectQuery = "SELECT * FROM students";
            selectStatement = connection.createStatement();

            // Prepare the search statement
            String searchQuery = "SELECT * FROM students WHERE id=?";
            searchStatement = connection.prepareStatement(searchQuery);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            if (insertStatement != null) {
                insertStatement.close();
            }
            if (updateStatement != null) {
                updateStatement.close();
            }
            if (deleteStatement != null) {
                deleteStatement.close();
            }
            if (selectStatement != null) {
                selectStatement.close();
            }
            if (searchStatement != null) {
                searchStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        try {
            // Create the table if it doesn't exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS students (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), age INT, phone VARCHAR(20), address VARCHAR(255), gender VARCHAR(10))";
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableQuery);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addStudent() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String phone = phoneField.getText();
            String address = addressField.getText();
            String gender = genderChoice.getSelectedItem();

            // Set the values for the prepared statement
            insertStatement.setInt(1, id);
            insertStatement.setString(2, name);
            insertStatement.setInt(3, age);
            insertStatement.setString(4, phone);
            insertStatement.setString(5, address);
            insertStatement.setString(6, gender);

            // Execute the insert statement
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStudent() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String phone = phoneField.getText();
            String address = addressField.getText();
            String gender = genderChoice.getSelectedItem();

            // Set the values for the prepared statement
            updateStatement.setString(1, name);
            updateStatement.setInt(2, age);
            updateStatement.setString(3, phone);
            updateStatement.setString(4, address);
            updateStatement.setString(5, gender);
            updateStatement.setInt(6, id);

            // Execute the update statement
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteStudent() {
        try {
            int id = Integer.parseInt(idField.getText());

            // Set the values for the prepared statement
            deleteStatement.setInt(1, id);

            // Execute the delete statement
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayStudents() {
        try {
            // Clear the display area
            displayArea.setText("");

            // Execute the select statement
            ResultSet resultSet = selectStatement.executeQuery("SELECT * FROM students");

            // Display the results in the display area
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String gender = resultSet.getString("gender");

                String studentInfo = "ID: " + id + "\n"
                        + "Name: " + name + "\n"
                        + "Age: " + age + "\n"
                        + "Phone: " + phone + "\n"
                        + "Address: " + address + "\n"
                        + "Gender: " + gender + "\n\n";

                displayArea.append(studentInfo);
            }

            // Close the result set
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchStudent() {
        try {
            int id = Integer.parseInt(searchField.getText());

            // Set the value for the search statement
            searchStatement.setInt(1, id);

            // Execute the search statement
            ResultSet resultSet = searchStatement.executeQuery();

            // Display the results in the display area
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String gender = resultSet.getString("gender");

                String studentInfo = "ID: " + id + "\n"
                        + "Name: " + name + "\n"
                        + "Age: " + age + "\n"
                        + "Phone: " + phone + "\n"
                        + "Address: " + address + "\n"
                        + "Gender: " + gender + "\n\n";

                displayArea.setText(studentInfo);
            } else {
                displayArea.setText("Student with ID " + id + " not found.");
            }

            // Close the result set
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        phoneField.setText("");
        addressField.setText("");
        searchField.setText("");
        displayArea.setText("");
    }

    public static void main(String[] args) {
        new StudentDatabaseUI();
    }
}
