package com.tavern.darkshard.bl;

import com.tavern.darkshard.dal.CodeExecutionJobDao;
import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.CodeExecutionJobOutput;
import com.tavern.darkshard.model.JobStatus;

import javax.inject.Inject;
import java.util.Optional;

public class CodeExecutionJobAction {

    private final CodeExecutionJobDao codeExecJobDao;

    @Inject
    public CodeExecutionJobAction(final CodeExecutionJobDao codeExecJobDao) {
        this.codeExecJobDao = codeExecJobDao;
    }

    public void submitCodeExecutionJob(final CodeExecutionJobInput codeExecutionJob) {
        this.codeExecJobDao.putCodeExecutionJob(codeExecutionJob);
        // TODO: implement task queue dao to use here
    }

    public Optional<JobStatus> getCodeExecutionJobStatus(final String jobId) {
        return Optional.empty();
    }

    public Optional<CodeExecutionJobOutput> getCodeExecutionJobOutput(final String jobId) {
        return Optional.empty();
    }

    public void deleteCodeExecutionJob(final String jobId) {
    }
}
