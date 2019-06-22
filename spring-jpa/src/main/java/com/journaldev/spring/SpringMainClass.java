package com.journaldev.spring;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

//import com.in28minutes.springboot.jdbc.h2.example.student.StudentJdbcRepository;
import com.journaldev.spring.model.Employee;
import com.journaldev.spring.repository.EmployeeRepository;

@SpringBootConfiguration
@ComponentScan(basePackages = "com.journaldev.spring")
public class SpringMainClass  implements CommandLineRunner{
	
	@Autowired
	EmployeeRepository repository;

	public static void main(String[] args) {
		ConfigurableApplicationContext context =  SpringApplication.run(SpringMainClass.class, args);
		for (String bean : context.getBeanDefinitionNames()) {
			System.out.println(bean);
		}
	}
	
	public void run(String... args) {
		System.out.println(repository);

		// store
		repository.store(new Employee(1, "Pankaj", "CEO"));
		repository.store(new Employee(2, "Anupam", "Editor"));
		repository.store(new Employee(3, "Meghna", "CFO"));

		// retrieve
		Employee emp = repository.retrieve(1);
		System.out.println(emp);

		// search
		Employee cfo = repository.search("Meghna");
		System.out.println(cfo);

		// delete
		Employee editor = repository.delete(2);
		System.out.println(editor);

		// close the spring context
		//context.close();
	}

}