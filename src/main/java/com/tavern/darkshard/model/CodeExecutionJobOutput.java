package com.tavern.darkshard.model;

import org.immutables.value.Value;

@Value.Immutable
public interface CodeExecutionJobOutput {

    String time();

    ResultStatus status();

}
