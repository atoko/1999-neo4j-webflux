package com.matrix9.domain.model;

import com.matrix9.domain.request.PersonAddRelationRequest;
import lombok.Data;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.internal.value.PathValue;
import org.neo4j.driver.v1.types.Path;

@Data
public class RelationDTO {
    PersonDTO source = new PersonDTO();
    String relation;
    String type;
    String value;
    String metadata;

    private RelationDTO() {}

    public RelationDTO(String guid, String relation, PersonAddRelationRequest addRelationRequest) {
        this.source.setGuid(guid);
        this.relation = relation;
        this.type = addRelationRequest.getType();
        this.value = addRelationRequest.getValue();
    }

    public RelationDTO(Path path) {
        this.source.setGuid(path.start().get("guid").asString());
        this.source.setName(path.start().get("name").asString());
        this.source.setEmail(path.start().get("email").asString());
        this.relation = path.relationships().iterator().next().type();
        this.type = path.end().labels().iterator().next();
        this.value = path.end().get("name").asString();
    }

    public static RelationDTO reverseLookup(Path path) {
        RelationDTO dto = new RelationDTO();
        dto.source.setGuid(path.end().get("guid").asString());
        dto.source.setName(path.end().get("name").asString());
        dto.source.setEmail(path.end().get("email").asString());
        dto.relation = path.relationships().iterator().next().type();
        dto.type = path.start().labels().iterator().next();
        dto.value = path.start().get("name").asString();

        return dto;
    }
}
