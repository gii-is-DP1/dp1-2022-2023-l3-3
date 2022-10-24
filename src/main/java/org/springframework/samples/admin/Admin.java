package org.springframework.samples.admin;

import org.springframework.samples.petclinic.model.Person;

public class Admin extends Person{

    public Admin(String name, String nickname, String email, String password) {
        super(name, nickname, email, password);
        
    }
    
}
