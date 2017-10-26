package com.wallethub.assigment.usecase;

import com.wallethub.assigment.AssigmentApplication;
import com.wallethub.assigment.domain.AccessLog;
import com.wallethub.assigment.domain.Duration;
import com.wallethub.assigment.gateway.repository.AccessLogRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AssigmentApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@AutoConfigureTestDatabase
public class FindIpByQueryTest {

    @Autowired
    FindIpByQuery findIpByQuery;

    @Autowired
    AccessLogRepository accessLogRepository;

    @Before
    public void setUp() {
        accessLogRepository.deleteAll();
    }

    @Test
    public void fetchOnlyRegistersInCorrectHourlyPeriod() {
        accessLogRepository.save(Arrays.asList(
                AccessLog.builder()
                        .ip("192.168.0.0")
                        .date(LocalDateTime.of(2017, 1, 1, 9, 0, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build(),
                AccessLog.builder()
                        .ip("192.168.0.0")
                        .date(LocalDateTime.of(2017, 1, 1, 10, 0, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build(),
                AccessLog.builder()
                        .ip("192.168.0.0")
                        .date(LocalDateTime.of(2017, 1, 1, 10, 30, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build(),
                AccessLog.builder()
                        .ip("192.168.0.0")
                        .date(LocalDateTime.of(2017, 1, 1, 10, 30, 30))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build(),
                AccessLog.builder()
                        .ip("192.168.0.0")
                        .date(LocalDateTime.of(2017, 1, 1, 11, 0, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build()
        ));

        List<String> ipList = findIpByQuery.execute(LocalDateTime.of(2017, 1, 1, 10, 30, 0), Duration.HOURLY, 3L);
        assertThat(ipList, Matchers.hasSize(1));
        assertThat(ipList, Matchers.hasItem("192.168.0.0"));
    }

    @Test
    public void fetchOnlyRegistersInCorrectDailyPeriod() {
        accessLogRepository.save(Arrays.asList(
                AccessLog.builder()
                        .ip("192.168.0.0")
                        .date(LocalDateTime.of(2017, 1, 1, 9, 0, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build(),
                AccessLog.builder()
                        .ip("192.168.0.0")
                        .date(LocalDateTime.of(2017, 1, 2, 10, 0, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build(),
                AccessLog.builder()
                        .ip("192.168.0.0")
                        .date(LocalDateTime.of(2017, 1, 2, 10, 30, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build(),
                AccessLog.builder()
                        .ip("192.168.0.0")
                        .date(LocalDateTime.of(2017, 1, 2, 10, 30, 30))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build(),
                AccessLog.builder()
                        .ip("192.168.0.0")
                        .date(LocalDateTime.of(2017, 1, 3, 11, 0, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build()
        ));

        List<String> ipList = findIpByQuery.execute(LocalDateTime.of(2017, 1, 2, 10, 0, 0), Duration.DAILY, 3L);
        assertThat(ipList, Matchers.hasSize(1));
        assertThat(ipList, Matchers.hasItem("192.168.0.0"));
    }

    @Test
    public void fetchOnlyRegistersHavingThresholdEqualsOrBiggerThen() {
        accessLogRepository.save(Arrays.asList(
                AccessLog.builder()
                        .ip("192.168.0.0")
                        .date(LocalDateTime.of(2017, 1, 1, 9, 0, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build(),
                AccessLog.builder()
                        .ip("192.168.0.0")
                        .date(LocalDateTime.of(2017, 1, 1, 9, 0, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build(),
                AccessLog.builder()
                        .ip("192.168.0.0")
                        .date(LocalDateTime.of(2017, 1, 1, 9, 0, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build(),
                AccessLog.builder()
                        .ip("192.168.0.1")
                        .date(LocalDateTime.of(2017, 1, 1, 9, 0, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build(),
                AccessLog.builder()
                        .ip("192.168.0.1")
                        .date(LocalDateTime.of(2017, 1, 1, 9, 0, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build(),
                AccessLog.builder()
                        .ip("192.168.0.2")
                        .date(LocalDateTime.of(2017, 1, 1, 9, 0, 0))
                        .httpStatus(200)
                        .requestMethod("GET")
                        .browser("Mozilla Firefox")
                        .build()
        ));

        List<String> ipList = findIpByQuery.execute(LocalDateTime.of(2017, 1, 1, 9, 0, 0), Duration.DAILY, 2L);
        assertThat(ipList, Matchers.hasSize(2));
        assertThat(ipList, Matchers.hasItem("192.168.0.0"));
        assertThat(ipList, Matchers.hasItem("192.168.0.1"));
    }
}