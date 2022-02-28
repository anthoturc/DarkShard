package com.tavern.darkshard.dal;

import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.CodeExecutionJobMetadata;
import com.tavern.darkshard.model.CodeExecutionJobOutput;

import java.util.Optional;

public interface CodeExecutionJobDao {

    void putCodeExecutionJob(CodeExecutionJobInput codeExecJobInput);

    Optional<CodeExecutionJobMetadata> getCodeExecutionJobMetadata(String jobId);

    Optional<CodeExecutionJobOutput> getCodeExecutionJobOutput(String jobId);

    void deleteCodeExecutionJob(String jobId);

}
