package com.matrix9;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableWebFlux
public class DefaultConfiguration {
    @Bean
    Driver driver() {
        try {
            return GraphDatabase.driver("bolt://barry-sky-blue-hane-rapids.graphstory.cloud",
                    AuthTokens.basic("barry_sky_blue_hane_rapids", "KqtZkjbFih5hHargY7zsl"),
                    Config
                        .build()
                        .withMaxConnectionPoolSize(100)
                        .withLoadBalancingStrategy(Config.LoadBalancingStrategy.ROUND_ROBIN)
                        .build()
            );
        } catch (Exception e) {
            System.out.println("Database failed to connect!");
            throw e;
        }
    }
}
