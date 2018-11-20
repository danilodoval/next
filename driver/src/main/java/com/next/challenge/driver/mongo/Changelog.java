package com.next.challenge.driver.mongo;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;

@ChangeLog
public class Changelog {

    @ChangeSet(order = "001", id = "driver001", author = "ddval")
    public void addStarts(MongoTemplate mongoTemplate) {

    }

}
