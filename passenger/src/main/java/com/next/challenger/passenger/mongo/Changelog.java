package com.next.challenger.passenger.mongo;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeLog
public class Changelog {

    @ChangeSet(order = "001", id = "test001", author = "ddval")
    public void addStarts(MongoTemplate mongoTemplate) {

    }
}
