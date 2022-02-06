package com.tavern.darkshard.util;

import javax.inject.Inject;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class JobIdGenerator {

    private static final int NUMBER_OF_RANDOM_DIGITS = 20;
    private static final int RANDOM_NUMBER_ORIGIN_INCLUSIVE = 0;
    private static final int RANDOM_NUMBER_BOUND_EXCLUSIVE = 16;
    private static final String JOB_ID_PREFIX = "job";

    @Inject
    public JobIdGenerator() {
    }

    public String generateRandomJobId() {
        final String hexString = ThreadLocalRandom.current()
                .ints(NUMBER_OF_RANDOM_DIGITS, RANDOM_NUMBER_ORIGIN_INCLUSIVE, RANDOM_NUMBER_BOUND_EXCLUSIVE)
                .boxed()
                .map(Integer::toHexString)
                .collect(Collectors.joining());

        return String.format("%s-%s", JOB_ID_PREFIX, hexString);
    }

}
