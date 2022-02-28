package com.tavern.darkshard.interceptor;

import com.tavern.darkshard.exception.ResourceNotFoundException;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * ExceptionInterceptor
 *
 * Adapted from https://stackoverflow.com/a/50601574
 */
public class ExceptionInterceptor implements ServerInterceptor {

    private static final Logger LOG = LogManager.getLogger(ExceptionInterceptor.class);

    private static final String INTERNAL_SERVER_EXCEPTION_MESSAGE = "There was an internal error. Try again later";

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler) {
        final ServerCall.Listener<ReqT> delegate = serverCallHandler.startCall(serverCall, metadata);
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (final Exception ex) {
                    serverCall.close(translateException(ex), new Metadata());
                }
            }
        };
    }

    private Status translateException(final Exception ex) {
        final String stackTraceString = Arrays.toString(ex.getStackTrace()).replace(',', '\n');
        final String logMsg = String.join("\n",
                ex.toString(),
                stackTraceString.substring(1, stackTraceString.length()-1)
        );

        if (ex instanceof ResourceNotFoundException) {
            LOG.info(logMsg);
            return Status.NOT_FOUND
                    .withCause(ex)
                    .withDescription(ex.getMessage());
        }

        LOG.error(logMsg);
        return Status.INTERNAL // Catch all other exceptions as Internal Errors
                .withDescription(INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
}
