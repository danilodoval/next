package com.test.passenger.config;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
public class Config extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${spring.data.mongodb.host}")
    private String uri;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(new MongoClientURI(uri));
    }

    @Bean
    public Mongobee mongobee() throws Exception {
        Mongobee runner = new Mongobee(uri);
        runner.setDbName(getDatabaseName());
        runner.setChangeLogsScanPackage("com.test.passenger.mongo");
        runner.setMongoTemplate(mongoTemplate());
        return runner;
    }
}
