package io.perspica.worker;

import com.codahale.metrics.Counter;
import io.perspica.common.metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class worker {
    private final static Counter parsedLines = metrics.registry.counter("lines.parsed");

    private static final Logger LOG = LoggerFactory.getLogger(worker.class);
    public final static void parseLine(String msg) {
        parsedLines.inc();

        // Here is where we would do the business logic, each "msg" here represents a datapoint
        LOG.debug("Incoming Datapoint: {}", msg);
    }
}
