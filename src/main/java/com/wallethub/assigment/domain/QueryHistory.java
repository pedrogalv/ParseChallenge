package com.wallethub.assigment.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class QueryHistory {

    @Id
    @GeneratedValue
    Long id;

    String ip;

    String blockDescription;

}
