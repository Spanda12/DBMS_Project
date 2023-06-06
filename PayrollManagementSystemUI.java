import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.*;

public class PayrollManagementSystemUI extends JFrame {
    private JTextField employeeNameField;
    private JTextField employeeIdField;
    private JTextField hoursWorkedField;
    private JTextField hourlyRateField;
    private JLabel totalSalaryLabel;
    private JTextArea displayArea;
    private Connection connection;

    public PayrollManagementSystemUI() {
        setTitle("Payroll Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new GridLayout(8, 2));

        JLabel nameLabel = new JLabel("Employee Name:");
        employeeNameField = new JTextField();
        JLabel idLabel = new JLabel("Employee ID:");
        employeeIdField = new JTextField();
        JLabel hoursLabel = new JLabel("Task completed:");
        hoursWorkedField = new JTextField();
        JLabel rateLabel = new JLabel("Rate per task:");
        hourlyRateField = new JTextField();
        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new CalculateButtonListener());
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteButtonListener());
        JButton displayButton = new JButton("Display");
        displayButton.addActionListener(new DisplayButtonListener());
        JLabel totalLabel = new JLabel("Total Salary:");
        totalSalaryLabel = new JLabel();
        JLabel displayLabel = new JLabel("Salary Information:");
        displayArea = new JTextArea();

        add(nameLabel);
        add(employeeNameField);
        add(idLabel);
        add(employeeIdField);
        add(hoursLabel);
        add(hoursWorkedField);
        add(rateLabel);
        add(hourlyRateField);
        add(calculateButton);
        add(deleteButton);
        add(displayButton);
        add(new JLabel()); // Empty label for alignment
        add(totalLabel);
        add(totalSalaryLabel);
        add(displayLabel);
        add(new JScrollPane(displayArea));

        connectToDatabase();

        setVisible(true);
    }

    private void connectToDatabase() {
        try {
            String jdbcUrl = "jdbc:mysql://localhost:3306/payroll";
            String username = "root";
            String password = "Spandan@12";

            connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connected to the database successfully!");

            // Create the table if it doesn't exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS payroll (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "employee_name VARCHAR(100)," +
                    "employee_id VARCHAR(20)," +
                    "total_salary DECIMAL(10, 2)" +
                    ")";
            PreparedStatement createTableStatement = connection.prepareStatement(createTableQuery);
            createTableStatement.executeUpdate();
            System.out.println("Table created successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String employeeName = employeeNameField.getText();
            String employeeId = employeeIdField.getText();
            double hoursWorked = Double.parseDouble(hoursWorkedField.getText());
            double hourlyRate = Double.parseDouble(hourlyRateField.getText());

            double totalSalary = hoursWorked * hourlyRate;
            totalSalaryLabel.setText(Double.toString(totalSalary));

            // Insert the calculated salary into the database
            try {
                String query = "INSERT INTO payroll (employee_name, employee_id, total_salary) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, employeeName);
                statement.setString(2, employeeId);
                statement.setDouble(3, totalSalary);
                statement.executeUpdate();
                System.out.println("Salary information saved to the database.");
            } catch (SQLException ex) {
                System.out.println("Failed to save salary information to the database.");
                ex.printStackTrace();
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Delete salary information from the database
            try {
                String employeeId = employeeIdField.getText();
                String query = "DELETE FROM payroll WHERE employee_id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, employeeId);
                int rowsDeleted = statement.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Salary information deleted from the database.");
                } else {
                    System.out.println("No salary information found for the given employee ID.");
                }
            } catch (SQLException ex) {
                System.out.println("Failed to delete salary information from the database.");
                ex.printStackTrace();
            }
        }
    }

    private class DisplayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Display salary information from the database
            try {
                String query = "SELECT employee_name, employee_id, total_salary FROM payroll";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                StringBuilder sb = new StringBuilder();
                while (resultSet.next()) {
                    String employeeName = resultSet.getString("employee_name");
                    String employeeId = resultSet.getString("employee_id");
                    double totalSalary = resultSet.getDouble("total_salary");
                    sb.append("Employee Name: ").append(employeeName).append("\n");
                    sb.append("Employee ID: ").append(employeeId).append("\n");
                    sb.append("Total Salary: ").append(totalSalary).append("\n\n");
                }

                displayArea.setText(sb.toString());
            } catch (SQLException ex) {
                System.out.println("Failed to fetch salary information from the database.");
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PayrollManagementSystemUI();
            }
        });
    }
}
