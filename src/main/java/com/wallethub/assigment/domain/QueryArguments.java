package com.wallethub.assigment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class QueryArguments {

    String filename;

    LocalDateTime dateTime;

    Duration duration;

    Long threshold;
}
