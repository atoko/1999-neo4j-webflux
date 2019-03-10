package com.matrix9.dal;

import com.matrix9.domain.model.PersonDTO;
import com.matrix9.domain.model.RelationDTO;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class PersonClient extends NeoClient{
    public Mono<List<RelationDTO>> get(String id) {
        String query = String.format("MATCH p=(n:Person {guid: '%s'})-->(any) RETURN p, any", id);
        Statement statement = new Statement(query);

        return queryList(statement).map( records -> {
            return records.stream().map(record -> new RelationDTO(record.get("p").asPath()))
                    .collect(Collectors.toList());
        });
    }

    public Mono<String> set(PersonDTO person) {
      String query = String.format("CREATE (n:Person {guid:'%s', name: '%s', email: '%s'}) RETURN n",
              person.getGuid(),
              person.getName(),
              person.getEmail()
      );
      Statement statement = new Statement(query);

      return execute(statement).map(result -> {
        if (!result.isEmpty()) { return person.getGuid(); } else return "";
      });
    };

    public Mono<String> remove(PersonDTO person) {
        String query = String.format("" +
                "MATCH (current:Person { guid: '%s' }) " +
                "DELETE current", person.getGuid());
        Statement statement = new Statement(query);

        return execute(statement).map(result -> {
            if (!result.isEmpty()) { return person.getGuid(); } else return "";
        });
    };

    public Mono<Boolean> addRelation(RelationDTO dto) {
        String query = String.format("" +
                "MATCH (current:Person { guid: '%s' }) \n" +
                "MERGE (current)-[r:%s]->(value:%s { name: '%s' }) \n" +
                "RETURN type(r)",
                dto.getSource().getGuid(),
                dto.getRelation().toUpperCase(),
                StringUtils.capitalize(dto.getType().toLowerCase()),
                dto.getValue().toUpperCase()
        );
        Statement statement = new Statement(query);

        return execute(statement).map(result -> {
            if (!result.isEmpty()) { return true; } else return false;
        });
    }

    public Mono<Boolean> removeRelation(RelationDTO dto) {
        String query = String.format("" +
                        "MATCH (n { guid: '%s' })-[r:%s]->(value:%s { name: '%s' })" +
                        "DELETE r",
                dto.getSource().getGuid(),
                dto.getRelation().toUpperCase(),
                StringUtils.capitalize(dto.getType().toLowerCase()),
                dto.getValue().toUpperCase()
        );
        Statement statement = new Statement(query);

        return execute(statement).map(result -> {
            if (!result.isEmpty()) { return true; } else return false;
        });
    }
}
