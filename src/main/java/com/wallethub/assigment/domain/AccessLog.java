package com.wallethub.assigment.domain;

import com.wallethub.assigment.conf.LocalDateTimeConverter;
import lombok.*;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class AccessLog {

    @GeneratedValue
    @Id
    Long id;

    @Convert(converter = LocalDateTimeConverter.class)
    LocalDateTime date;

    String ip;

    String requestMethod;

    Integer httpStatus;

    String browser;
}
