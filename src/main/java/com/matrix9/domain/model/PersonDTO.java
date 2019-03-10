package com.matrix9.domain.model;

import com.matrix9.domain.request.PersonPostRequest;
import lombok.Data;

@Data
public class PersonDTO {

    String guid;
    String name;
    String email;

    public PersonDTO() {}

    public PersonDTO(PersonPostRequest post) {
        this.guid = post.getGuid();
        this.name = post.getName();
        this.email = post.getEmail();
    }
}
