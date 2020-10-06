package com.example.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxAndMono {
    static class Employee {
        final String name;
        Employee(String name) {
            this.name = name;
        }
    }

    /**
     * Subscribe to the publisher.
     */
    @Test
    void fluxSubscriber() {
        Flux<String> f1 = getDataFromExternalSource().log();

        f1.subscribe(data ->System.out.println(data));
    }

    Flux<String> getDataFromExternalSource() {
        return Flux.just("one","two","three");
    }

    /**
     * Subscribe to the publisher and handle onNext and onComplete(Terminate)
     */
    @Test
    void fluxSubscriberEvents() {
        Flux<String> f1 = Flux.just("one","two","three").log();

        f1.subscribe(System.out::println,
            (e) -> System.out.print(e),
            () -> System.out.print("Completed")); // you will not see this subscribe fxn calling in spring webflux
    }

    /**
     * Subscribe to the publisher and handle onNext and onError(Terminate)
     */
    @Test
    void fluxSubscriberWithError() {
        Flux<String> f1 = Flux.just("one","two","three")
            .concatWith(Flux.error(new RuntimeException("Error"))).log();

        f1.subscribe(System.out::println,
            (e) -> System.out.print(e),
            () -> System.out.print("Completed"));

    }

    /**
     * Transform the stream using operator, sync (map)
     */
    @Test
    void fluxSubscriberWithTransformation() {
        Flux<String> f1 = Flux.just("one","two","three");

        f1.
            map(item -> item.toUpperCase()) // map is synchronous, should only be used when we have operation which have predictable time.
            .subscribe(System.out::println);
    }

    /**
     * Transform the stream suing operator, async (flatMap)
     */
    @Test
    void fluxSubscriberWithTransformationFlatMap() {
        Flux<String> f1 = Flux.just("one","two","three").log();

        f1.
            map(item -> someFixedTransformation(item)) // map is synchronous, should only be used when we have operation which have predictable time.
            .flatMap(name -> someExternalService(name)) // using map returns Flux<Employee>
            .subscribe(employee -> System.out.println(employee.name));
    }

    /**
     * Code proof for Non-Blocking.
     * @throws InterruptedException
     */
    @Test
    void fluxNonBlocking() throws InterruptedException { // What happened here ??
        Flux<Long> f2 = Flux.interval(Duration.ofMillis(200)).log();

        f2
            .map(s -> s*20)
            .subscribe(res -> System.out.println(res + " -- " + Thread.currentThread().getName()));

        System.out.println(" Thread -- " + Thread.currentThread().getName());

        Thread.sleep(4000);
    }

    private String someFixedTransformation(String item) {
        return item.toUpperCase();
    }

    Flux<Employee> someExternalService(String name) {
        return Flux.just(new Employee(name));
    }
}
