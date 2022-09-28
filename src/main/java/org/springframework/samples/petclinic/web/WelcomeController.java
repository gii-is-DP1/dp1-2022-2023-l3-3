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
		List<Person> persons = new ArrayList<Person>();
		Person sergio = new Person();
		sergio.setFirstName("Sergio"); 
		sergio.setLastName("Santiago");
		persons.add(sergio);
		model.put("persons", persons);
		model.put("title", "L3-3");
		model.put("group", "G2 L3-3");

	    return "welcome";
	  }
}
