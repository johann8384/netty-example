package io.perspica.common;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class metrics {
    public static final MetricRegistry registry = new MetricRegistry();
    private static final int PREFIX = Integer.parseInt(System.getProperty("metric-schema", "localhost.0"));

    public static void startReport() {
        final Graphite graphite = new Graphite(new InetSocketAddress("graphite.example.com", 2003));
        final GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
                .prefixedWith("perspica.streamingest." + PREFIX)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .build(graphite);
        reporter.start(1, TimeUnit.MINUTES);
    }
}