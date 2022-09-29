package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.samples.petclinic.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
<<<<<<< HEAD
	
	
	  @GetMapping({"/","/welcome"})
	  public String welcome(Map<String, Object> model) {	
		List<Person> persons = new ArrayList<Person>();
		Person sergio = new Person();
		sergio.setFirstName("Sergio"); 
		sergio.setLastName("Santiago");
		persons.add(sergio);
		model.put("persons", persons);
		model.put("title", "L3-3");
		model.put("group", "G2 L3-3");
=======
>>>>>>> 4f9fa771fce97aa2bf2ea553cd7eae6f98daf1db

	@GetMapping({ "/", "/welcome" })
	public String welcome(Map<String, Object> model) {

		List<Person> persons = new ArrayList<Person>();
		Person person = new Person();
		person.setFirstName("Alejandro");
		person.setLastName("Perez");
		persons.add(person);
		Person person2 = new Person();
		person2.setFirstName("Sergio");
		person2.setLastName("Santiago");
		persons.add(person2);
		Person person3 = new Person();
		person3.setFirstName("Maria");
		person3.setLastName("Vico");
		persons.add(person3);
		Person person4 = new Person();
		person4.setFirstName("Juan Carlos");
		person4.setLastName("Ramírez");
		persons.add(person4);
		Person person5 = new Person();
		person5.setFirstName("Alberto");
		person5.setLastName("Perea");
		persons.add(person5);
		Person person6 = new Person();
		person6.setFirstName("Javier");
		person6.setLastName("Nunez");
		persons.add(person6);
		model.put("persons", persons);
		model.put("title", "Lab1");
		model.put("group", "Teachers");
		return "welcome";
	}
}
