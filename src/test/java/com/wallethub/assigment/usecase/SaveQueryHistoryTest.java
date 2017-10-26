package com.wallethub.assigment.usecase;

import com.wallethub.assigment.AssigmentApplication;
import com.wallethub.assigment.domain.Duration;
import com.wallethub.assigment.domain.QueryHistory;
import com.wallethub.assigment.gateway.repository.QueryHistoryRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AssigmentApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@AutoConfigureTestDatabase
public class SaveQueryHistoryTest {

    @Autowired
    SaveQueryHistory saveQueryHistory;

    @Autowired
    QueryHistoryRepository queryHistoryRepository;

    @Before
    public void setup() {
        queryHistoryRepository.deleteAll();
    }

    @Test
    public void saveAllIpsForDailyDuration() {
        List<String> ipList = Arrays.asList("192.168.0.1", "192.168.0.2");

        saveQueryHistory.execute(ipList, Duration.DAILY, 100L);

        List<QueryHistory> all = queryHistoryRepository.findAll();
        assertThat(all, Matchers.hasSize(2));
        assertThat(all, Matchers.hasItem(QueryHistory.builder().ip("192.168.0.1").blockDescription("daily limit of 100 calls crossed.").build()));
        assertThat(all, Matchers.hasItem(QueryHistory.builder().ip("192.168.0.2").blockDescription("daily limit of 100 calls crossed.").build()));
    }

//    @Test
//    public void saveAllIpsForHourlyDuration() {
//        List<String> ipList = Arrays.asList("192.168.0.3", "192.168.0.4");
//
//        saveQueryHistory.execute(ipList, Duration.HOURLY, 10L);
//
//
//        List<QueryHistory> all = queryHistoryRepository.findAll();
//        assertThat(all, Matchers.hasSize(2));
//        assertThat(all, Matchers.hasItem(QueryHistory.builder().ip("192.168.0.3").blockDescription("hourly limit of 10 calls crossed.").build()));
//        assertThat(all, Matchers.hasItem(QueryHistory.builder().ip("192.168.0.4").blockDescription("hourly limit of 10 calls crossed.").build()));
//    }
//
//    @Test
//    public void saveWhenIpListIsEmpty() {
//        List<String> ipList = Collections.emptyList();
//
//        saveQueryHistory.execute(ipList, Duration.HOURLY, 10L);
//
//        List<QueryHistory> all = queryHistoryRepository.findAll();
//        assertThat(all, Matchers.hasSize(0));
//    }

}