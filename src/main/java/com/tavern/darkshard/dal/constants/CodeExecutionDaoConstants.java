package com.tavern.darkshard.dal.constants;

public final class CodeExecutionDaoConstants {

    // DB Name
    public static final String CODE_EXECUTION_COLLECTION_NAME = "CodeExecutionJobs";

    // Fields for Jobs
    public static final String JOB_ID = "_id";
    public static final String FILE_DATA = "file_data";
    public static final String JOB_STATUS = "status";

    // Queue Name
    public static final String CODE_EXECUTION_JOB_QUEUE_NAME = "CodeExecutionQueue";

    private CodeExecutionDaoConstants() {
    }
}
