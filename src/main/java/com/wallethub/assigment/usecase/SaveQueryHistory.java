package com.wallethub.assigment.usecase;

import com.wallethub.assigment.domain.Duration;
import com.wallethub.assigment.domain.QueryHistory;
import com.wallethub.assigment.gateway.repository.QueryHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SaveQueryHistory {

    private QueryHistoryRepository queryHistoryRepository;

    @Autowired
    public SaveQueryHistory(QueryHistoryRepository queryHistoryRepository) {
        this.queryHistoryRepository = queryHistoryRepository;
    }

    public void execute(List<String> ipList, Duration duration, Long threshold) {
        List<QueryHistory> queryHistoryList = new ArrayList<>();

        ipList.forEach(ip -> queryHistoryList.add(QueryHistory.builder().ip(ip).blockDescription(String.format("%s limit of %s calls crossed.",
                duration.toString(), threshold)).build()));

        queryHistoryRepository.save(queryHistoryList);
    }
}
