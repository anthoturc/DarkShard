package com.tavern.darkshard.service;

import com.tavern.darkshard.DarkShardGrpc;
import com.tavern.darkshard.DeleteCodeExecutionJobRequest;
import com.tavern.darkshard.DeleteCodeExecutionJobResponse;
import com.tavern.darkshard.GetCodeExecutionJobOutputRequest;
import com.tavern.darkshard.GetCodeExecutionJobOutputResponse;
import com.tavern.darkshard.GetCodeExecutionJobStatusRequest;
import com.tavern.darkshard.GetCodeExecutionJobStatusResponse;
import com.tavern.darkshard.SubmitCodeExecutionJobRequest;
import com.tavern.darkshard.SubmitCodeExecutionJobResponse;
import com.tavern.darkshard.bl.CodeExecutionJobAction;
import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.CodeExecutionJobOutput;
import com.tavern.darkshard.model.ImmutableCodeExecutionJobInput;
import com.tavern.darkshard.model.JobStatus;
import com.tavern.darkshard.util.JobIdGenerator;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class DarkShardServiceImpl extends DarkShardGrpc.DarkShardImplBase {

    private static final Logger LOG = LogManager.getLogger(DarkShardServiceImpl.class);

    private final CodeExecutionJobAction codeExecJobAction;
    private final JobIdGenerator jobIdGenerator;

    @Inject
    public DarkShardServiceImpl(final CodeExecutionJobAction codeExecJobAction,
                                final JobIdGenerator jobIdGenerator) {
        this.codeExecJobAction = codeExecJobAction;
        this.jobIdGenerator = jobIdGenerator;
    }

    @Override
    public void submitCodeExecutionJob(final SubmitCodeExecutionJobRequest request,
                                       final StreamObserver<SubmitCodeExecutionJobResponse> responseObserver) {

        LOG.info("In submit handler");

        final String jobId = jobIdGenerator.generateRandomJobId();
        final CodeExecutionJobInput codeExecutionJobInput = ImmutableCodeExecutionJobInput.builder()
                .jobId(jobId)
                .rawProgramCode(request.getRawProgramCode())
                .jobStatus(JobStatus.NOT_STARTED)
                .build();

        this.codeExecJobAction.submitCodeExecutionJob(codeExecutionJobInput);

        responseObserver.onNext(SubmitCodeExecutionJobResponse.newBuilder().setJobId(jobId).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCodeExecutionJobStatus(final GetCodeExecutionJobStatusRequest request,
                                          final StreamObserver<GetCodeExecutionJobStatusResponse> responseObserver) {

        JobStatus jobStatus = this.codeExecJobAction.getCodeExecutionJobStatus(request.getJobId());

        final GetCodeExecutionJobStatusResponse response = GetCodeExecutionJobStatusResponse.newBuilder()
                .setJobStatus(jobStatus.toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCodeExecutionJobOutput(final GetCodeExecutionJobOutputRequest request,
                                          final StreamObserver<GetCodeExecutionJobOutputResponse> responseObserver) {

        final CodeExecutionJobOutput codeJobOutput =
                this.codeExecJobAction.getCodeExecutionJobOutput(request.getJobId());

        final GetCodeExecutionJobOutputResponse response = GetCodeExecutionJobOutputResponse.newBuilder()
                .setResultStatus(codeJobOutput.status().toString())
                .setJobOutput(codeJobOutput.output())
                .setTime(codeJobOutput.time())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCodeExecutionJob(final DeleteCodeExecutionJobRequest request,
                                       final StreamObserver<DeleteCodeExecutionJobResponse> responseObserver) {

        this.codeExecJobAction.deleteCodeExecutionJob(request.getJobId());

        final DeleteCodeExecutionJobResponse response = DeleteCodeExecutionJobResponse.newBuilder()
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
