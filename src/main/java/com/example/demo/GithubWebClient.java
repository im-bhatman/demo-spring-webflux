package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GithubWebClient {
    private final WebClient githubClient;

    public GithubWebClient(WebClient.Builder webClient) {
        this.githubClient = webClient.baseUrl("https://api.github.com").build();
    }

    public Mono<User> getUserDetails(final String username) {
        return githubClient.get()
            .uri("/users/{username}", username)
            .retrieve()
            .bodyToMono(User.class).log();
    }

    public Flux<Member> getOrgMembers(String orgName) {
        return githubClient.get()
            .uri("/orgs/{orgName}/members", orgName) // reactive-streams
            .retrieve()
            .bodyToFlux(Member.class).log();
    }

}
