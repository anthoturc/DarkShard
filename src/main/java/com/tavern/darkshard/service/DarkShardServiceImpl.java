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
import com.tavern.darkshard.model.ImmutableCodeExecutionJobInput;
import com.tavern.darkshard.model.JobStatus;
import com.tavern.darkshard.util.JobIdGenerator;
import io.grpc.stub.StreamObserver;

import javax.inject.Inject;

public class DarkShardServiceImpl extends DarkShardGrpc.DarkShardImplBase {

    private final CodeExecutionJobAction codeExecJobAction;
    private final JobIdGenerator jobIdGenerator;

    @Inject
    public DarkShardServiceImpl(final CodeExecutionJobAction codeExecJobAction,
                                final JobIdGenerator jobIdGenerator) {
        this.codeExecJobAction = codeExecJobAction;
        this.jobIdGenerator = jobIdGenerator;
    }


    @Override
    public void submitCodeExecutionJob(SubmitCodeExecutionJobRequest request,
                                       StreamObserver<SubmitCodeExecutionJobResponse> responseObserver) {

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
    public void getCodeExecutionJobStatus(GetCodeExecutionJobStatusRequest request,
                                          StreamObserver<GetCodeExecutionJobStatusResponse> responseObserver) {

    }

    @Override
    public void getCodeExecutionJobOutput(GetCodeExecutionJobOutputRequest request,
                                          StreamObserver<GetCodeExecutionJobOutputResponse> responseObserver) {

    }

    @Override
    public void deleteCodeExecutionJob(DeleteCodeExecutionJobRequest request,
                                       StreamObserver<DeleteCodeExecutionJobResponse> responseObserver) {
        super.deleteCodeExecutionJob(request, responseObserver);
    }

}
