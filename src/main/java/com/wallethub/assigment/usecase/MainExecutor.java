package com.wallethub.assigment.usecase;

import com.wallethub.assigment.domain.QueryArguments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@Component
public class MainExecutor {

    private final FileParser fileParser;
    private final FindIpByQuery findIpByQuery;
    private final SaveQueryHistory saveQueryHistory;

    public MainExecutor(FileParser fileParser, FindIpByQuery findIpByQuery, SaveQueryHistory saveQueryHistory) {
        this.fileParser = fileParser;
        this.findIpByQuery = findIpByQuery;
        this.saveQueryHistory = saveQueryHistory;
    }


    public List<String> execute(QueryArguments arguments) throws FileNotFoundException {

        File file = new File(arguments.getFilename());

        if (!file.exists()) {
            log.error("File {} does not exists", arguments.getFilename());
            throw new FileNotFoundException();
        } else {

            log.info("Parsing file and saving in database");
            fileParser.execute(file);
            log.info("Parsing file ended");

            log.info("Executing query");
            List<String> ipList = findIpByQuery.execute(arguments.getDateTime(), arguments.getDuration(), arguments.getThreshold());
            log.info("Query ended");

            log.info("Saving query results");
            saveQueryHistory.execute(ipList, arguments.getDuration(), arguments.getThreshold());

            return ipList;
        }

    }
}
