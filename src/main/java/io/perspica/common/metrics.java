package io.perspica.common;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class metrics {
    public static final MetricRegistry registry = new MetricRegistry();
    private static final String PREFIX = System.getProperty("instrumentation.schema", "localhost.0");
    private static final int INTERVAL = Integer.parseInt(System.getProperty("instrumentation.interval", "15"));
    static final boolean GRAPHITE = System.getProperty("instrumentation.graphite.enable") != null;

    public static void startReport() {
        if (GRAPHITE) {
            final Graphite graphite = new Graphite(new InetSocketAddress("graphite.example.com", 2003));
            final GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(registry)
                    .prefixedWith("perspica.streamingest." + PREFIX)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .filter(MetricFilter.ALL)
                    .build(graphite);
            graphiteReporter.start(INTERVAL, TimeUnit.SECONDS);
        }

        final Slf4jReporter logReporter = Slf4jReporter.forRegistry(registry)
                .outputTo(LoggerFactory.getLogger("perspica.streamingest." + PREFIX))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        logReporter.start(INTERVAL, TimeUnit.SECONDS);
    }
}