package com.dabaicong.jpa.entry;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="employee")
public class Employee {
	
	@Id
	private int id ;
	private String name ; 
	private long salary;
	
	public Employee() {
		
	}
	
	public Employee(int id, String name, long salary) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
	}
	public Employee(int id){
		super();
		this.id= id ;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSalary() {
		return salary;
	}
	public void setSalary(long salary) {
		this.salary = salary;
	} 
	
	
}
