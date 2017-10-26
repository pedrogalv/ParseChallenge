package com.wallethub.assigment;

import com.wallethub.assigment.domain.QueryArguments;
import com.wallethub.assigment.usecase.ArgumentValidator;
import com.wallethub.assigment.usecase.MainExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@Slf4j
@SpringBootApplication
public class AssigmentApplication implements CommandLineRunner {

    private final MainExecutor mainExecutor;
    private final ArgumentValidator argumentValidator;

    @Autowired
    public AssigmentApplication(MainExecutor mainExecutor, ArgumentValidator argumentValidator) {
        this.mainExecutor = mainExecutor;
        this.argumentValidator = argumentValidator;
    }


    public static void main(String[] args) {
        SpringApplication.run(AssigmentApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        log.debug("Starting Assigment Application");

        QueryArguments arguments = null;
        try {
            arguments = argumentValidator.execute(strings);
        } catch (Exception e) {
            log.error(e.getMessage());
            System.exit(1);
        }

        List<String> ipList = mainExecutor.execute(arguments);

        log.info("Found {} IP(s) that satisfy the query conditions:", ipList.size());

        ipList.forEach(log::info);
        System.exit(0);
    }
}
