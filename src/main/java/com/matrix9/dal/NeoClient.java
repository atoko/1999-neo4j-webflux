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
public class NeoClient {
    @Autowired
    Driver driver;

    Mono<List<Record>> queryList(Statement statement) {
        Session session = driver.session();
        CompletionStage<StatementResultCursor> cursorStage =
                session.runAsync(statement);

        return Mono.fromCompletionStage(
                cursorStage
                        .thenCompose(StatementResultCursor::listAsync)
                        .whenComplete((records, error) -> {
                            if (records != null) System.out.println( records );
                            else error.printStackTrace();
                            session.closeAsync();
                        })
        )        .publishOn(Schedulers.elastic())
                .subscribeOn(Schedulers.elastic());
    }

    Mono<Record> queryOne(Statement statement) {
        Session session = driver.session();
        CompletionStage<StatementResultCursor> cursorStage =
                session.runAsync(statement, TransactionConfig.builder().withTimeout(Duration.ofSeconds(8)).build());

        return Mono.fromCompletionStage(
                cursorStage
                        .thenCompose(StatementResultCursor::singleAsync)
                        .whenComplete((records, error) -> {
                            if (records != null) System.out.println( records );
                            else error.printStackTrace();
                            session.closeAsync();
                        })
        )        .publishOn(Schedulers.elastic())
                .subscribeOn(Schedulers.elastic());
    }


    Mono<List<String>> execute(Statement statement) {
        Session session = driver.session();
        CompletionStage<StatementResultCursor> cursorStage =
                session.runAsync(statement);

        return Mono.fromCompletionStage(
                cursorStage
                        .handle((records, error) -> {
                            return records.keys();
                        })
                        .whenComplete((records, error) -> {
                            if (records != null) System.out.println( records );
                            else error.printStackTrace();
                            session.closeAsync();
                        })
        )
        .publishOn(Schedulers.elastic())
        .subscribeOn(Schedulers.elastic());
    }
}
