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
	

}
