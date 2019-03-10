package com.matrix9.controller;

import com.matrix9.domain.request.PersonAddRelationRequest;
import com.matrix9.domain.request.PersonPostRequest;
import com.matrix9.domain.response.PersonGetResponse;
import com.matrix9.domain.response.PersonPostResponse;
import com.matrix9.domain.response.PersonPutRelationResponse;
import com.matrix9.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/persons")
class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(value = "/{guid}", consumes = "application/json", produces = "application/json")
    Mono<ResponseEntity<PersonGetResponse>> getById(@PathVariable String guid) {
        return personService.getById(guid).map(response ->
                ResponseEntity.ok(response)
        );
    }

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    Mono<ResponseEntity<PersonPostResponse>> postUser(@RequestBody PersonPostRequest request) {
        return personService.createPerson(request).map(response ->
            ResponseEntity.ok(response)
        );
    }

    @DeleteMapping(value = "/", consumes = "application/json", produces = "application/json")
    Mono<ResponseEntity<PersonPostResponse>> deleteUser(@RequestBody PersonPostRequest request) {
        return personService.deletePerson(request).map(response ->
                ResponseEntity.ok(response)
        );
    }

    @PutMapping(value = "/{guid}/{relation}", consumes = "application/json", produces = "application/json")
    Mono<ResponseEntity<PersonPutRelationResponse>> putRelation(@PathVariable String guid,
                                                            @PathVariable String relation,
                                                            @RequestBody PersonAddRelationRequest request) {
        return personService.addRelation(guid, relation, request).map(success -> {
            PersonPutRelationResponse response = new PersonPutRelationResponse();
            response.setOk(success);
            return ResponseEntity.ok(response);
        });
    }

    @DeleteMapping(value = "/{guid}/{relation}", consumes = "application/json", produces = "application/json")
    Mono<ResponseEntity<PersonPutRelationResponse>> deleteRelation(@PathVariable String guid,
                                                            @PathVariable String relation,
                                                            @RequestBody PersonAddRelationRequest request) {
        return personService.removeRelation(guid, relation, request).map(success -> {
            PersonPutRelationResponse response = new PersonPutRelationResponse();
            response.setOk(success);
            return ResponseEntity.ok(response);
        });
    }

}
