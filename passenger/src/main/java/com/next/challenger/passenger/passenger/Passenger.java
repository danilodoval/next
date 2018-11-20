package com.next.challenger.passenger.passenger;

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
public class Passenger {

    @Id
    String id;

    String name;

    Date createdAt;

    Date updatedAt;

}
