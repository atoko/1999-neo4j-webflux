package com.matrix9.controller;

import com.matrix9.domain.query.RelationQuery;
import com.matrix9.domain.response.PersonGetResponse;
import com.matrix9.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/query")
class QueryController {

    @Autowired
    private QueryService queryService;

    @GetMapping(value = "/{type}/{name}", consumes = "application/json", produces = "application/json")
    Mono<ResponseEntity<Map<String, List<String>>>> getById(@PathVariable String type, @PathVariable String name) {
        RelationQuery query = new RelationQuery();
        query.setType(type);
        query.setValue(name);

        return queryService.getEmails(query).map(response ->
                ResponseEntity.ok(response)
        );
    }
}

//5982546820
//8986252410