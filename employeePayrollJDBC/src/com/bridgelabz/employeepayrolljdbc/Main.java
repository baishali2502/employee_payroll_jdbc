package com.bridgelabz.employeepayrolljdbc;
import java.sql.*;
import java.util.*;

public class Main 
{

	public static void main(String[] args)
	{
		EmployeePayroll e = new EmployeePayroll();
		
//		 ----------------------- UC-1 ---------------------------
		
		
//      Check if MySQL JDBC driver class is loaded
		e.checkMySQLJDBCDriver();

//      List the MySQL JDBC Drivers Registered
		e.listRegisteredDrivers();

//      Get the SQL Connection from the JDBC Driver 
//      passing the DB URL, User name and Password
		Connection connection = e.establishConnection();

		// Output of the Java Program is Connection Established
		if (connection != null) {
			System.out.println("Connection Established!\n");
		} else {
			System.out.println("Connection Failed!\n");
		}

		
//	     ----------------------- UC-2 ---------------------------

		
		List<Employee> employees = e.readEmployeePayrollData();

		for(Employee employee:employees)
		{
			System.out.println(employee.toString());
		}
		
		
//	     ----------------------- UC-3/4 ---------------------------	

		
//      Update the salary for Employee Terisa to 3000000.00
		e.updateEmployeeSalary("Terissa", 3000000.0);
		
		
//	     ----------------------- UC-5 ---------------------------

		
//      Retrieve all employees who have joined in a particular data range		
		e.retrieveEmployeesInDateRange("2023-01-01","2023-06-06");
		
		
//	     ----------------------- UC-6 ---------------------------
		
		
//		find sum, average, min, max and count of male and female employees
		e.analyzeEmployeeDataByGender();
		
		
	}

	
}
