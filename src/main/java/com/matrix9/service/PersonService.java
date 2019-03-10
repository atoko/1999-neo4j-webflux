package com.matrix9.service;

import com.matrix9.dal.NeoClient;
import com.matrix9.dal.PersonClient;
import com.matrix9.domain.model.PersonDTO;
import com.matrix9.domain.model.RelationDTO;
import com.matrix9.domain.request.PersonAddRelationRequest;
import com.matrix9.domain.request.PersonPostRequest;
import com.matrix9.domain.response.PersonGetResponse;
import com.matrix9.domain.response.PersonPostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

import static java.util.stream.Collectors.toMap;

@Component
public class PersonService {

    @Autowired
    private PersonClient neoClient;

    public Mono<PersonGetResponse> getById(String id) {

        return neoClient.get(id).map( relationDTOS -> {
            PersonGetResponse response = new PersonGetResponse();
            response.getData().setId(id);
            response.getData().setName(relationDTOS.iterator().next().getSource().getName());
            response.getData().setEmail(relationDTOS.iterator().next().getSource().getEmail());

            Map<String, List<String>> relations = new HashMap<>();
            for (RelationDTO relation: relationDTOS) {
                String rel = relation.getRelation();
                String value = relation.getValue();

                if (!relations.containsKey(rel)) {
                    relations.put(rel, new ArrayList<>());
                }

                relations.get(rel).add(value);
            }
            response.getData().setRelations(relations);

            return response;
        });
    }

    public Mono<PersonPostResponse> createPerson(PersonPostRequest request) {
        PersonDTO dto = new PersonDTO(request);

        return neoClient.set(dto).map(id -> {
                PersonPostResponse response = new PersonPostResponse();
                response.setId(id);
                return response;
            }
        );
    }

    public Mono<PersonPostResponse> deletePerson(PersonPostRequest request) {
        PersonDTO dto = new PersonDTO(request);

        return neoClient.remove(dto).map(id -> {
                    PersonPostResponse response = new PersonPostResponse();
                    response.setId(id);
                    return response;
                }
        );
    }

    public Mono<Boolean> addRelation(String id, String relation, PersonAddRelationRequest request) {
        RelationDTO dto = new RelationDTO(id, relation, request);

        return neoClient.addRelation(dto).map(success -> {
            return success;
        });
    }

    public Mono<Boolean> removeRelation(String id, String relation, PersonAddRelationRequest request) {
        RelationDTO dto = new RelationDTO(id, relation, request);

        return neoClient.removeRelation(dto).map(success -> {
            return success;
        });
    }
}
