package io.perspica.worker;

import com.codahale.metrics.Counter;
import io.perspica.common.metrics;

public class worker {
    private final static Counter parsedLines = metrics.registry.counter("lines.parsed");
    public final static void parseLine(String msg) {
        parsedLines.inc();
        // Here is where we would do the business logic, each "msg" here represents a datapoint
        System.out.println("Incoming: " + msg);
    }
}
