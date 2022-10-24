/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple JavaBean domain object representing an person.
 *
 * @author Ken Krebs
 */
@MappedSuperclass
@Getter
@Setter
public class Person extends BaseEntity {

	@Column(name = "name")
	@NotEmpty
	protected String name;

	@Column(name = "nickname")
	@NotEmpty
	protected String nickname;

	@NotEmpty
	protected String email;

	@NotEmpty
	protected String password;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	protected LocalDateTime creationDate;

	public Person(String name, String nickname, String email, String password){
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.creationDate = LocalDateTime.now();
	}

	public Person of (String name, String nickname, String email, String password){
		return new Person(name, nickname, email, password);
	}
	

}
