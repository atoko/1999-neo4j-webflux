package com.matrix9.domain.response;

import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public class PersonResponse {
    String id;
    String name;
    String email;
    Map<String, List<String>> relations = Collections.emptyMap();
}
