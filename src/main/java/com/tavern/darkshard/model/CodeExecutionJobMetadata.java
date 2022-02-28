package com.tavern.darkshard.model;

import org.immutables.value.Value;

@Value.Immutable
public interface CodeExecutionJobMetadata {

    String jobId();

    JobStatus status();
}
