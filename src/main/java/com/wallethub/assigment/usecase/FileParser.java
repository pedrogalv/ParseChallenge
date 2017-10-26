package com.wallethub.assigment.usecase;

import com.wallethub.assigment.domain.AccessLog;
import com.wallethub.assigment.gateway.repository.AccessLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Component
public class FileParser {

    @Value("${fileLoader.buffer.size:2000}")
    public Integer BUFFER_SIZE;

    private final AccessLogRepository accessLogRepository;

    @Autowired
    public FileParser(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    public void execute(File file) {
        List<AccessLog> buffer = new ArrayList<>();
        int iterations = 0;

        FileInputStream inputStream = null;
        Scanner sc = null;

        log.info("Parsing each line and saving in database. For optimization, it saves a batch of {} lines per time", BUFFER_SIZE);
        log.debug("Free Memory before process:{} ", Runtime.getRuntime().freeMemory());
        try {
            inputStream = new FileInputStream(file);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {

                String line = sc.nextLine();
                AccessLog accessLog = assembleLogFromLine(line);
                buffer.add(accessLog);

                if (buffer.size() > BUFFER_SIZE) {
                    accessLogRepository.save(buffer);
                    buffer = new ArrayList<>();
                }
                iterations++;
            }
            accessLogRepository.save(buffer);

            if (sc.ioException() != null) {
                log.error("Exception captured while reading file: {}", sc.ioException().getMessage());
            }
        } catch (IOException e) {
            log.error("Exception captured while reading file: {}", e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Exception captured while closing file scanner: {}", e.getMessage());
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
        log.debug("Free Memory after process:{} ", Runtime.getRuntime().freeMemory());
        log.info("{} lines parsed and saved in database.", iterations);
    }

    private AccessLog assembleLogFromLine(String line) {

        String[] split = line.split("\\|");

        if (split.length < 5 || split.length > 5) {
            throw new RuntimeException("Log format should have the following information: Date, IP, Request, Status and User Agent, separated by the pipe(|) character");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        return AccessLog.builder()
                .date(LocalDateTime.parse(split[0], formatter))
                .ip(split[1])
                .requestMethod(split[2])
                .httpStatus(Integer.parseInt(split[3]))
                .browser(split[4]).build();
    }

}
