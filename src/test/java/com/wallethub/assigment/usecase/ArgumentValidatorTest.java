package com.wallethub.assigment.usecase;

import com.wallethub.assigment.domain.Duration;
import com.wallethub.assigment.domain.QueryArguments;
import com.wallethub.assigment.domain.exceptions.ArgumentValidationException;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ArgumentValidatorTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @InjectMocks
    private
    ArgumentValidator argumentValidator;

    @Test
    public void throwErrorWhenAccessLogIsMissing() {
        expectedEx.expect(ArgumentValidationException.class);
        expectedEx.expectMessage("Missing required option: accesslog");

        argumentValidator.execute("--startDate=2017-01-01.13:00:00", "--duration=hourly", "--threshold=100");
    }

    @Test
    public void throwErrorWhenStartDateIsMissing() {
        expectedEx.expect(ArgumentValidationException.class);
        expectedEx.expectMessage("Missing required option: startDate");

        argumentValidator.execute("--accesslog=access.log", "--duration=hourly", "--threshold=100");
    }

    @Test
    public void throwErrorWhenDurationIsMissing() {
        expectedEx.expect(ArgumentValidationException.class);
        expectedEx.expectMessage("Missing required option: duration");

        argumentValidator.execute("--accesslog=access.log", "--startDate=2017-01-01.13:00:00", "--threshold=100");
    }

    @Test
    public void throwErrorWhenThresholdIsMissing() {
        expectedEx.expect(ArgumentValidationException.class);
        expectedEx.expectMessage("Missing required option: threshold");

        argumentValidator.execute("--accesslog=access.log", "--startDate=2017-01-01.13:00:00", "--duration=hourly");
    }

    @Test
    public void throwErrorWhenStartDateIsInWrongFormat() {
        expectedEx.expect(ArgumentValidationException.class);
        expectedEx.expectMessage("StartDate should be in format yyyy-MM-dd.HH:mm:ss");

        argumentValidator.execute("--accesslog=access.log", "--startDate=123", "--duration=hourly", "--threshold=100");
    }

    @Test
    public void throwErrorWhenDurationIsInWrongFormat() {
        expectedEx.expect(ArgumentValidationException.class);
        expectedEx.expectMessage("Duration must be hourly or daily");

        argumentValidator.execute("--accesslog=access.log", "--startDate=2017-01-01.13:00:00", "--duration=123", "--threshold=100");
    }

    @Test
    public void throwErrorWhenThresholdIsInWrongFormat() {
        expectedEx.expect(ArgumentValidationException.class);
        expectedEx.expectMessage("Threshold should be a number");

        argumentValidator.execute("--accesslog=access.log", "--startDate=2017-01-01.13:00:00", "--duration=hourly", "--threshold=aaa");
    }

    @Test
    public void returnObjectWhenArgumentsAreValid() {
        QueryArguments arguments = argumentValidator.execute("--accesslog=access.log", "--startDate=2017-01-01.13:00:00", "--duration=hourly", "--threshold=100");
        assertThat(arguments.getFilename(), Matchers.equalTo("access.log"));
        assertThat(arguments.getDateTime(), Matchers.equalTo(LocalDateTime.of(2017, 1, 1, 13, 0, 0)));
        assertThat(arguments.getDuration(), Matchers.equalTo(Duration.HOURLY));
        assertThat(arguments.getThreshold(), Matchers.equalTo(100L));
    }
}