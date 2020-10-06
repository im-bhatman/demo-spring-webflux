package com.example.demo;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/user")
public class GithubController {
    private final GithubWebClient githubWebClient;

    public GithubController(GithubWebClient githubWebClient) {
        this.githubWebClient = githubWebClient;
    }

    @GetMapping("/{orgName}")
    public Flux<User> getOrgUserDetails(@PathVariable String orgName) {
        Flux<Member> members = githubWebClient.getOrgMembers(orgName);
        Flux<User> users = members.flatMap(member -> githubWebClient.getUserDetails(member.getLogin()));
        return users;
    }
}
