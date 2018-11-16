package com.test.core.scheduler;

public enum SchedulerCronType {

    ONE_SECOND("0/1 * * * * ?"),

    TEN_SECONDS("0/10 * * * * ?"),

    ONE_MINUTE("0 */1 * ? * *"),

    TEN_MINUTE("0 */10 * ? * *"),

    ONE_HOUR("0 0 * ? * *");

    //ToDo implement other counters

    private String value;

    SchedulerCronType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
