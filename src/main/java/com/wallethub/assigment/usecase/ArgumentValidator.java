package com.wallethub.assigment.usecase;

import com.wallethub.assigment.domain.Duration;
import com.wallethub.assigment.domain.QueryArguments;
import com.wallethub.assigment.domain.exceptions.ArgumentValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class ArgumentValidator {

    public QueryArguments execute(String... strings) {

        QueryArguments build;

        Options options = new Options();
        options.addOption(Option.builder().longOpt("accesslog").hasArg(true).required(true).build());
        options.addOption(Option.builder().longOpt("startDate").hasArg(true).required(true).build());
        options.addOption(Option.builder().longOpt("duration").hasArg(true).required(true).build());
        options.addOption(Option.builder().longOpt("threshold").hasArg(true).required(true).build());

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd;
        try {
            cmd = parser.parse(options, strings);
        } catch (ParseException e) {
            log.error(e.getMessage());
            throw new ArgumentValidationException(e.getMessage());
        }

        build = QueryArguments.builder()
                .filename(cmd.getOptionValue("accesslog"))
                .dateTime(validateStartDate(cmd.getOptionValue("startDate")))
                .duration(validateDuration(cmd.getOptionValue("duration")))
                .threshold(validateThreshold(cmd.getOptionValue("threshold")))
                .build();

        return build;
    }

    private LocalDateTime validateStartDate(String startDateString) {
        LocalDateTime startDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
        try {
            startDate = LocalDateTime.parse(startDateString, formatter);
        } catch (Exception e) {
            log.error("StartDate should be in format yyyy-MM-dd.HH:mm:ss");
            throw new ArgumentValidationException("StartDate should be in format yyyy-MM-dd.HH:mm:ss");
        }
        return startDate;
    }

    private Duration validateDuration(String durationString) {
        Duration duration;
        try {
            duration = Duration.valueOf(durationString.toUpperCase());
        } catch (Exception e) {
            log.error("Duration {} is not an acceptable value", durationString);
            throw new ArgumentValidationException("Duration must be hourly or daily");
        }
        return duration;
    }

    private Long validateThreshold(String thresholdString) {
        Long threshold;
        try {
            threshold = Long.valueOf(thresholdString);
        } catch (Exception e) {
            log.error("Threshold {} is not an acceptable value", thresholdString);
            throw new ArgumentValidationException("Threshold should be a number");
        }
        return threshold;
    }
}
