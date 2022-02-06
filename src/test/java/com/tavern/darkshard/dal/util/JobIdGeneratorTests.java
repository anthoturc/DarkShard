package com.tavern.darkshard.dal.util;

import com.tavern.darkshard.util.JobIdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JobIdGeneratorTests {

    private JobIdGenerator sut;

    @BeforeEach
    public void beforeEachTest() {
        sut = new JobIdGenerator();
    }

    @Test
    public void WHEN_generateRandomJobId_THEN_success() {
        String id = sut.generateRandomJobId();
        Assertions.assertNotNull(id);
        Assertions.assertEquals(24, id.length());
        Assertions.assertEquals(0, id.indexOf("job-"));
    }
}
