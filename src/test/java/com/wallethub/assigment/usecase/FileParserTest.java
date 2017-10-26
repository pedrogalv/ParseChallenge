package com.wallethub.assigment.usecase;

import com.wallethub.assigment.AssigmentApplication;
import com.wallethub.assigment.gateway.repository.AccessLogRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeParseException;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AssigmentApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)
public class FileParserTest {

    @Autowired
    FileParser fileParser;

    @MockBean
    AccessLogRepository accessLogRepository;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @Test
    public void parseFileWithSuccess() throws IOException {
        File file = File.createTempFile("accessLog", "log");
        file.deleteOnExit();

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            writer.write("2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"");
            writer.newLine();
            writer.write("2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"");
        }

        fileParser.execute(file);

        verify(accessLogRepository, Mockito.times(1)).save(anyList());
    }

    @Test
    public void throwErrorForDateInUnknownFormat() throws IOException {
        expectedEx.expect(DateTimeParseException.class);

        File file = File.createTempFile("accessLog", "log");
        file.deleteOnExit();

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            writer.write("2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"");
            writer.newLine();
            writer.write("2017/01/01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"");
        }

        fileParser.execute(file);
        verify(accessLogRepository, Mockito.times(0)).save(anyList());
    }

    @Test
    public void throwErrorForLogHavingLessThan5Attributes() throws IOException {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Log format should have the following information: Date, IP, Request, Status and User Agent, separated by the pipe(|) character");

        File file = File.createTempFile("accessLog", "log");
        file.deleteOnExit();

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            writer.write("2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"");
        }

        fileParser.execute(file);
        verify(accessLogRepository, Mockito.times(0)).save(anyList());
    }

    @Test
    public void throwErrorForLogHavingLMoreThan5Attributes() throws IOException {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Log format should have the following information: Date, IP, Request, Status and User Agent, separated by the pipe(|) character");

        File file = File.createTempFile("accessLog", "log");
        file.deleteOnExit();

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            writer.write("2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"");
        }

        fileParser.execute(file);
        verify(accessLogRepository, Mockito.times(0)).save(anyList());
    }


}