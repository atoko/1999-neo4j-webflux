package com.matrix9.dal;

import com.matrix9.domain.model.PersonDTO;
import com.matrix9.domain.model.RelationDTO;
import com.matrix9.domain.query.RelationQuery;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.driver.v1.Statement;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QueryClient extends NeoClient{
    public Mono<List<RelationDTO>> get(RelationQuery relation) {
        String query = String.format("MATCH p=(n:%s {name: '%s'})<--(any) RETURN p, any",
                relation.getType(),
                relation.getValue()
        );
        Statement statement = new Statement(query);

        return queryList(statement).map( records -> {
            return records.stream().map(record -> RelationDTO.reverseLookup(record.get("p").asPath()))
                    .collect(Collectors.toList());
        });
    }
}
