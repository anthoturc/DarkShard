package com.tavern.darkshard.service;

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
import com.tavern.darkshard.model.ImmutableCodeExecutionJobOutput;
import com.tavern.darkshard.model.JobStatus;
import com.tavern.darkshard.model.ResultStatus;
import com.tavern.darkshard.util.JobIdGenerator;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class DarkShardServiceImplTests {

    private static final String TEST_JOB_ID = "job-1234";
    private static final String TEST_RAW_PROGRAM_CODE = "print('hello')";
    private static final String TEST_JOB_OUTPUT = "EXIT 0";

    private CodeExecutionJobAction codeExecAction;
    private DarkShardServiceImpl sut;

    @BeforeEach
    public void beforeEachTest() {
        codeExecAction = Mockito.mock(CodeExecutionJobAction.class);
        sut = new DarkShardServiceImpl(codeExecAction, new JobIdGenerator());
    }

    @Test
    public void WHEN_submitCodeExecutionJob_THEN_success() {
        final SubmitCodeExecutionJobRequest request = SubmitCodeExecutionJobRequest.newBuilder()
                .setRawProgramCode(TEST_RAW_PROGRAM_CODE)
                .build();

        final StreamObserver<SubmitCodeExecutionJobResponse> mockStreamObserver = Mockito.mock(StreamObserver.class);

        final ArgumentCaptor<CodeExecutionJobInput> blInput = ArgumentCaptor.forClass(CodeExecutionJobInput.class);
        Mockito.doNothing().when(codeExecAction)
                .submitCodeExecutionJob(blInput.capture());

        sut.submitCodeExecutionJob(request, mockStreamObserver);

        CodeExecutionJobInput actualBlInput = blInput.getAllValues().get(0);
        Assertions.assertTrue(actualBlInput.jobId().contains("job"));
        Assertions.assertEquals(JobStatus.NOT_STARTED, actualBlInput.jobStatus());
        Assertions.assertEquals(TEST_RAW_PROGRAM_CODE, actualBlInput.rawProgramCode());

        Mockito.verify(mockStreamObserver, Mockito.times(1))
                .onNext(SubmitCodeExecutionJobResponse.newBuilder().setJobId(actualBlInput.jobId()).build());
        Mockito.verify(mockStreamObserver, Mockito.times(1))
                .onCompleted();
    }

    @Test
    public void WHEN_getCodeExecutionJobStatus_THEN_success() {
        final GetCodeExecutionJobStatusRequest request = GetCodeExecutionJobStatusRequest.newBuilder()
                .setJobId(TEST_JOB_ID)
                .build();

        Mockito.when(codeExecAction.getCodeExecutionJobStatus(TEST_JOB_ID))
                .thenReturn(JobStatus.DONE);

        final StreamObserver<GetCodeExecutionJobStatusResponse> mockStreamObserver = Mockito.mock(StreamObserver.class);

        sut.getCodeExecutionJobStatus(request, mockStreamObserver);

        final GetCodeExecutionJobStatusResponse expectedResponse = GetCodeExecutionJobStatusResponse.newBuilder()
                .setJobStatus(JobStatus.DONE.toString())
                .build();

        Mockito.verify(mockStreamObserver, Mockito.times(1))
                .onNext(expectedResponse);

        Mockito.verify(mockStreamObserver, Mockito.times(1))
                .onCompleted();
        Mockito.verify(codeExecAction, Mockito.times(1))
                .getCodeExecutionJobStatus(TEST_JOB_ID);
    }

    @Test
    public void WHEN_getCodeExecutionJobOutput_THEN_success() {
        final GetCodeExecutionJobOutputRequest request = GetCodeExecutionJobOutputRequest.newBuilder()
                .setJobId(TEST_JOB_ID)
                .build();

        final StreamObserver<GetCodeExecutionJobOutputResponse> mockStreamObserver = Mockito.mock(StreamObserver.class);

        final CodeExecutionJobOutput output = ImmutableCodeExecutionJobOutput.builder()
                .output(TEST_JOB_OUTPUT)
                .time("1s")
                .status(ResultStatus.PASSED)
                .build();

        Mockito.when(codeExecAction.getCodeExecutionJobOutput(TEST_JOB_ID))
                .thenReturn(output);

        sut.getCodeExecutionJobOutput(request, mockStreamObserver);

        final GetCodeExecutionJobOutputResponse expectedResponse = GetCodeExecutionJobOutputResponse.newBuilder()
                .setJobOutput(TEST_JOB_OUTPUT)
                .setTime("1s")
                .setResultStatus(ResultStatus.PASSED.toString())
                .build();

        Mockito.verify(mockStreamObserver, Mockito.times(1))
                .onNext(expectedResponse);

        Mockito.verify(mockStreamObserver, Mockito.times(1))
                .onCompleted();
    }

    @Test
    public void WHEN_deleteCodeExecutionJob_THEN_success() {
        final DeleteCodeExecutionJobRequest request = DeleteCodeExecutionJobRequest.newBuilder()
                .setJobId(TEST_JOB_ID)
                .build();

        final StreamObserver<DeleteCodeExecutionJobResponse> mockStreamObserver = Mockito.mock(StreamObserver.class);

        sut.deleteCodeExecutionJob(request, mockStreamObserver);

        Mockito.verify(codeExecAction, Mockito.times(1))
                .deleteCodeExecutionJob(TEST_JOB_ID);
        Mockito.verify(mockStreamObserver, Mockito.times(1))
                .onNext(DeleteCodeExecutionJobResponse.newBuilder().build());
        Mockito.verify(mockStreamObserver, Mockito.times(1))
                .onCompleted();
    }


}
