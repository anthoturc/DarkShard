package com.tavern.darkshard.dal;

import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.CodeExecutionJobOutput;
import com.tavern.darkshard.model.JobStatus;

public interface CodeExecutionJobDao {

    void putCodeExecutionJob(CodeExecutionJobInput codeExecJobInput);

    void deleteCodeExecutionJob();

    JobStatus getCodeExecutionJobStatus();

    CodeExecutionJobOutput getCodeExecutionJobOutput();

}
