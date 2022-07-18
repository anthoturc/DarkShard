package com.tavern.darkshard.bl;

import com.tavern.darkshard.dal.CodeExecutionJobDao;
import com.tavern.darkshard.dal.CodeExecutionJobQueueDao;
import com.tavern.darkshard.exception.ResourceNotFoundException;
import com.tavern.darkshard.exception.UnsupportedOperationException;
import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.CodeExecutionJobMetadata;
import com.tavern.darkshard.model.CodeExecutionJobOutput;
import com.tavern.darkshard.model.JobStatus;

import javax.inject.Inject;

public class CodeExecutionJobAction {

    private final CodeExecutionJobDao codeExecJobDao;
    private final CodeExecutionJobQueueDao queueDao;

    @Inject
    public CodeExecutionJobAction(final CodeExecutionJobDao codeExecJobDao,
                                  final CodeExecutionJobQueueDao queueDao) {
        this.codeExecJobDao = codeExecJobDao;
        this.queueDao = queueDao;
    }

    public void submitCodeExecutionJob(final CodeExecutionJobInput codeExecutionJob) {
        // TODO: add this back when the dao is fully implemented
        this.codeExecJobDao.putCodeExecutionJob(codeExecutionJob);
        // TODO: implement task queue dao to use here
        this.queueDao.pushCodeExecutionJobToQueue(codeExecutionJob);
    }

    public JobStatus getCodeExecutionJobStatus(final String jobId) {

        return this.codeExecJobDao.getCodeExecutionJobMetadata(jobId)
                .map(CodeExecutionJobMetadata::status)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Code execution job (%s) does not exist.", jobId))
                );
    }

    public CodeExecutionJobOutput getCodeExecutionJobOutput(final String jobId) {
        throw new UnsupportedOperationException("This will be implemented later");
    }

    public void deleteCodeExecutionJob(final String jobId) {
        this.codeExecJobDao.deleteCodeExecutionJob(jobId);
    }
}
