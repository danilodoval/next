package com.next.challenge.driver.driver;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
public class Driver {

    @Id
    String id;

    String name;

    Date createdAt;

    Date updatedAt;

}
