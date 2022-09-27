package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.samples.petclinic.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
	
	
	  @GetMapping({"/","/welcome"})
	  public String welcome(Map<String, Object> model) {	    
		
		List<Person>persons= new ArrayList<Person>();
		Person person=new Person();
		person.setFirstName("Alejandro");
		person.setLastName("Perez");
		persons.add(person);
		Person person2=new Person();
		person2.setFirstName("Sergio");
		person2.setLastName("Santiago");
		persons.add(person2);
		Person person3=new Person();
		person3.setFirstName("Maria");
		person3.setLastName("Vico");
		persons.add(person3);
		model.put("persons", persons);
		model.put("title", "Lab1");
		model.put("group", "Teachers");
	    return "welcome";
	  }
}
