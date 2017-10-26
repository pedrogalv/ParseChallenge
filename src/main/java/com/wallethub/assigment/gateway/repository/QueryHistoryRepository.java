package com.wallethub.assigment.gateway.repository;

import com.wallethub.assigment.domain.QueryHistory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QueryHistoryRepository extends CrudRepository<QueryHistory, Long> {

    List<QueryHistory> findAll();

}
