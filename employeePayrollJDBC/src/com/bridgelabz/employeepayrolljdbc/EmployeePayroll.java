package com.bridgelabz.employeepayrolljdbc;

import java.io.*;
import java.sql.*;
import java.util.*;



public class EmployeePayroll 
{	
	    private String URL;
	    private String USERNAME;
	    private String PASSWORD;

	    public EmployeePayroll() {
	        loadConfig();
	    }

	    public void loadConfig() 
	    {
	        Properties properties = new Properties();
	        try (FileInputStream input = new FileInputStream("C:\\Users\\KIIT\\eclipse-workspace\\GE\\employeePayrollJDBC\\config.properties")) {
	            properties.load(input);

	            // Load values from properties file
	            URL = properties.getProperty("URL");
	            USERNAME = properties.getProperty("USERNAME");
	            PASSWORD = properties.getProperty("PASSWORD");

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
    /*
	 * @desc:Checks if the MySQL JDBC driver class is loaded
	 * 
	 * @params:none
	 * 
	 * @return:void
	 */
	void checkMySQLJDBCDriver() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("MySQL JDBC driver class is loaded.");
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC driver class is not loaded.");
			e.printStackTrace();
		}
	}

	/*
	 * @desc:Lists the registered JDBC drivers
	 * 
	 * @params:none
	 * 
	 * @returns:void
	 */
	 void listRegisteredDrivers() {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		System.out.println("Registered JDBC drivers:");
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			System.out.println(driver.getClass().getName());
		}
	}

	/*
	 * @desc:Gets the SQL Connection from the JDBC Driver passing the DB URL,
	 * Username, and Password
	 * 
	 * @params:none
	 * 
	 * @returns:void
	 */
	Connection establishConnection() 
	{
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}
	/*
	 * @desc:Prints details of all employees from employee_payroll table
	 * 
	 * @params:none
	 * 
	 * @returns:list of employees
	 */
	List<Employee> readEmployeePayrollData() 
	{
		List<Employee> employees = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			String query = "SELECT * FROM employee_payroll";
			System.out.println("SQL Query : "+query+"\n");
			try (PreparedStatement preparedStatement = connection.prepareStatement(query);
					ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					int id = resultSet.getInt("id");
					String name = resultSet.getString("NAME");
					double salary = resultSet.getDouble("salary");
					String startDate = resultSet.getString("start_date");

					Employee employee = new Employee(id, name, salary, startDate);
					employees.add(employee);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle exceptions appropriately in a real-world scenario
		}

		return employees;
	}
	/*
	 * @desc:Updates salary for a specific employee
	 * 
	 * @params:none
	 * 
	 * @returns:none
	 */
    void updateEmployeeSalary(String employeeName, double newSalary) 
    {
    	String updateQuery = "UPDATE employee_payroll SET salary = ? WHERE name = ?";
        System.out.println("\nSQL Query : "+updateQuery+"\n");
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
        	preparedStatement.setDouble(1,newSalary);
        	preparedStatement.setString(2,employeeName);
        	
            

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Salary updated successfully for " + employeeName+"\n");
            } else {
                System.out.println("Employee not found or no changes made.");
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
    /*
	 * @desc:Prints employees within a specific date range
	 * 
	 * @params:startdate , enddate
	 * 
	 * @returns:void
	 */
    void retrieveEmployeesInDateRange(String startDateRange, String endDateRange) {
       String QUERY = "SELECT * FROM employee_payroll WHERE start_date BETWEEN ? AND ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        	 
             PreparedStatement preparedStatement = connection.prepareStatement(QUERY)) {

            // Set the start and end dates for the date range
            preparedStatement.setString(1, startDateRange);
            preparedStatement.setString(2, endDateRange);

            // Retrieve data for employees who joined in the date range
            ResultSet resultSet = preparedStatement.executeQuery();

            // Reuse the ResultSet to populate EmployeePayrollData objects
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                String startDate = resultSet.getString("start_date");

                Employee employeePayrollData = new Employee(id, name, salary, startDate);
                System.out.println(employeePayrollData.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*
   	 * @desc:Calculate SUM, AVG, MIN, MAX, COUNT for Male or Female
   	 * 
   	 * @params:none
   	 * 
   	 * @returns:void
   	 */
       void analyzeEmployeeDataByGender() {
           try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

           	// Calculate SUM, AVG, MIN, MAX, COUNT for Male
               analyzeGender(connection, "M");

               // Calculate SUM, AVG, MIN, MAX, COUNT for Female
               analyzeGender(connection, "F");

           } catch (SQLException e) {
               e.printStackTrace();
           }
       }

   	/*
   	 * @desc:Calculate SUM, AVG, MIN, MAX, COUNT for Male or Female
   	 * 
   	 * @params:Connection and gender
   	 * 
   	 * @returns:void
   	 */
       private void analyzeGender(Connection connection, String gender) throws SQLException 
       {
           String query = "SELECT " +
                   "SUM(salary) AS total_salary, " +
                   "AVG(salary) AS avg_salary, " +
                   "MIN(salary) AS min_salary, " +
                   "MAX(salary) AS max_salary, " +
                   "COUNT(*) AS employee_count " +
                   "FROM employee_payroll " +
                   "WHERE gender = ? " +
                   "GROUP BY gender";

           try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
               preparedStatement.setString(1, gender);

               ResultSet resultSet = preparedStatement.executeQuery();

               if (resultSet.next()) {
                   double totalSalary = resultSet.getDouble("total_salary");
                   double avgSalary = resultSet.getDouble("avg_salary");
                   double minSalary = resultSet.getDouble("min_salary");
                   double maxSalary = resultSet.getDouble("max_salary");
                   int employeeCount = resultSet.getInt("employee_count");

                   System.out.println("\nAnalysis for Gender " + gender + ":");
                   System.out.println("Total Salary: " + totalSalary);
                   System.out.println("Average Salary: " + avgSalary);
                   System.out.println("Minimum Salary: " + minSalary);
                   System.out.println("Maximum Salary: " + maxSalary);
                   System.out.println("Employee Count: " + employeeCount);
                   System.out.println();
               }

           } catch (SQLException e) {
               throw new SQLException("Error in analyzing employee data by gender", e);
           }
       }
}
