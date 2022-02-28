package com.tavern.darkshard.dal;

import com.tavern.darkshard.model.CodeExecutionJobInput;

public interface CodeExecutionJobQueueDao {

    void pushCodeExecutionJobToQueue(CodeExecutionJobInput input);

}
