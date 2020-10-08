package com.example.demo;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class FluxBackPressure {
    @Test
    void fluxSubscriberWithBackPressure() {
        Flux<String> f1 = getDataFromExternalSource().log();

        f1.subscribe(new BaseSubscriber<String>() {
            /**
             * Hook for processing of onNext values. You can call {@link #request(long)} here
             * to further request data from the source {@link Publisher} if
             * the {@link #hookOnSubscribe(Subscription) initial request} wasn't unbounded.
             * <p>Defaults to doing nothing.
             *
             * @param value the emitted value to process
             */
            @Override
            protected void hookOnNext(String value) {
                request(1);
            }

            /**
             * Hook for further processing of onSubscribe's Subscription. Implement this method
             * to call {@link #request(long)} as an initial request. Values other than the
             * unbounded {@code Long.MAX_VALUE} imply that you'll also call request in
             * {@link #hookOnNext(Object)}.
             * <p> Defaults to request unbounded Long.MAX_VALUE as in {@link #requestUnbounded()}
             *
             * @param subscription the subscription to optionally process
             */
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(1);
            }
        });
    }

    Flux<String> getDataFromExternalSource() {
        return Flux.just("one","two","three");
    }
}
