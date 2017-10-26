package com.wallethub.assigment.gateway.repository;

import com.wallethub.assigment.domain.AccessLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AccessLogRepository extends CrudRepository<AccessLog, Long> {

    //    @Query("SELECT AL.ip FROM AccessLog AS AL WHERE AL.date BETWEEN ?1 AND ?2 GROUP BY AL.ip HAVING (SELECT COUNT(al.id) FROM AccessLog al WHERE al.date BETWEEN ?1 AND ?2 AND al.ip = AL.ip) >= ?3")
    @Query(value = "SELECT AL.ip as ip FROM AccessLog AS AL WHERE AL.date BETWEEN ?1 AND ?2 GROUP BY AL.ip HAVING count(AL.ip) >= ?3")
    List<String> findIpByPeriodAndThresholdGEThan(LocalDateTime firstDate, LocalDateTime lastDate, Long threshold);
}
