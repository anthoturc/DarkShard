package com.tavern.darkshard.model;

import org.immutables.value.Value;

@Value.Immutable
public interface CodeExecutionJobInput {
    String jobId();
    String rawProgramCode();
    JobStatus jobStatus();
}
