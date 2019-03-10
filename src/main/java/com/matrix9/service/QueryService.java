package com.matrix9.service;

import com.matrix9.dal.PersonClient;
import com.matrix9.dal.QueryClient;
import com.matrix9.domain.model.RelationDTO;
import com.matrix9.domain.query.RelationQuery;
import com.matrix9.domain.response.PersonGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QueryService {

    @Autowired
    private QueryClient neoClient;

    public Mono<Map<String, List<String>>> getEmails(RelationQuery request) {
        return neoClient.get(request).map( relationDTOS -> {
            Map<String, List<String>> relations = new HashMap<>();
            for (RelationDTO relation: relationDTOS) {
                String rel = relation.getRelation();

                if (!relations.containsKey(rel)) {
                    relations.put(rel, new ArrayList<>());
                }

                relations.get(rel).add(relation.getSource().getEmail());
            }

            return relations;
        });
    }
}
