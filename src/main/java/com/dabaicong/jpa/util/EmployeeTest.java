package com.dabaicong.jpa.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.dabaicong.jpa.entry.Employee;
import com.dabaicong.jpa.service.EmployeeService;

public class EmployeeTest {
	
	
	
	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EmployeeService");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EmployeeService serivce = new EmployeeService(entityManager);
		
		
		
		entityManager.getTransaction().begin();
		Employee employee = serivce.create(100, "dale", 10000);
		entityManager.getTransaction().commit();
		System.out.println(employee.getName());
		
		entityManager.close();
		entityManagerFactory.close();
				
	}
}
