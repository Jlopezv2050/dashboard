package com.jlopezv.dashboard.backend.controller;


import com.jlopezv.dashboard.backend.model.polling.Poll;
import com.jlopezv.dashboard.backend.payload.AppConstants;
import com.jlopezv.dashboard.backend.payload.PagedResponse;
import com.jlopezv.dashboard.backend.payload.polling.PollRequest;
import com.jlopezv.dashboard.backend.payload.polling.PollResponse;
import com.jlopezv.dashboard.backend.payload.polling.VoteRequest;
import com.jlopezv.dashboard.backend.payload.security.ApiResponse;
import com.jlopezv.dashboard.backend.security.CurrentUser;
import com.jlopezv.dashboard.backend.security.UserPrincipal;
import com.jlopezv.dashboard.backend.service.PollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping("/api/polls")
public class PollController {


    private final PollService pollService;

    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

    @Autowired
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping
    public PagedResponse<PollResponse> getPolls(@CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getAllPolls(currentUser, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest) {
        Poll poll = pollService.createPoll(pollRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(poll.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Poll Created Successfully"));
    }


    @GetMapping("/{pollId}")
    public PollResponse getPollById(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long pollId) {
        return pollService.getPollById(pollId, currentUser);
    }

    @PostMapping("/{pollId}/votes")
    @PreAuthorize("hasRole('USER')")
    public PollResponse castVote(@CurrentUser UserPrincipal currentUser,
                         @PathVariable Long pollId,
                         @Valid @RequestBody VoteRequest voteRequest) {
        return pollService.castVoteAndGetUpdatedPoll(pollId, voteRequest, currentUser);
    }

}
