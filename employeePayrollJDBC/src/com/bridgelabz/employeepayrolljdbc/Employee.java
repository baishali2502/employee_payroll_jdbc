package com.bridgelabz.employeepayrolljdbc;

public class Employee 
{
	 int id;
     String name;
     double salary;
     String startDate;
    
    public Employee(int id, String name, double salary, String startDate)
    {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.startDate = startDate;
    }
    
 // Getters and setters...

    @Override
    public String toString() 
    {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", startDate='" + startDate + '\'' +
                '}';
    }

}
