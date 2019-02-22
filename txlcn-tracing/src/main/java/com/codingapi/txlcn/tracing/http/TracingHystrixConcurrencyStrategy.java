package com.codingapi.txlcn.tracing.http;

import com.codingapi.txlcn.tracing.TracingContext;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Date: 19-2-15 下午1:28
 *
 * @author ujued
 */
@Slf4j
@Component
@ConditionalOnClass(HystrixConcurrencyStrategy.class)
public class TracingHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    private HystrixConcurrencyStrategy delegate;

    public TracingHystrixConcurrencyStrategy() {
        try {
            this.delegate = HystrixPlugins.getInstance().getConcurrencyStrategy();
            if (this.delegate instanceof TracingHystrixConcurrencyStrategy) {
                log.debug("Non another HystrixConcurrencyStrategy.");
                return;
            }
            HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins
                    .getInstance().getCommandExecutionHook();
            HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance()
                    .getEventNotifier();
            HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance()
                    .getMetricsPublisher();
            HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance()
                    .getPropertiesStrategy();
            log.debug("HystrixEventNotifier:{}, HystrixMetricsPublisher:{}, HystrixPropertiesStrategy:{}",
                    eventNotifier, metricsPublisher, propertiesStrategy);
            HystrixPlugins.reset();
            HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
            HystrixPlugins.getInstance()
                    .registerCommandExecutionHook(commandExecutionHook);
            HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
            HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
            HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
        } catch (Exception e) {
            log.error("Failed to register Tracing Hystrix Concurrency Strategy", e);
        }
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        Map<String, String> fields = TracingContext.tracing().fields();
        return () -> {
            boolean isReInitTracingContext = true;
            try {
                if (TracingContext.tracing().hasGroup()) {
                    isReInitTracingContext = false;
                    return delegate.wrapCallable(callable).call();
                }
                log.debug("Hystrix transfer tracing.");
                TracingContext.init(fields);
                return delegate.wrapCallable(callable).call();
            } finally {
                if (isReInitTracingContext) {
                    TracingContext.tracing().destroy();
                }
            }
        };
    }

    @Override
    public ThreadPoolExecutor getThreadPool(final HystrixThreadPoolKey threadPoolKey, HystrixProperty<Integer> corePoolSize, HystrixProperty<Integer> maximumPoolSize, HystrixProperty<Integer> keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        return this.delegate.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolProperties threadPoolProperties) {
        return this.delegate.getThreadPool(threadPoolKey, threadPoolProperties);
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return this.delegate.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
        return this.delegate.getRequestVariable(rv);
    }
}
