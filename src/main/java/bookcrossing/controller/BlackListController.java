package bookcrossing.controller;

import bookcrossing.domain.BlackList;
import bookcrossing.service.BlackListService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/black_list")
public class BlackListController {
    private final BlackListService blackListService;

    public BlackListController(BlackListService blackListService) {
        this.blackListService = blackListService;
    }

    @GetMapping
    public ResponseEntity<List<BlackList>> getAll() {
        Optional<List<BlackList>> blackList = Optional.ofNullable(blackListService.getAll());
        return blackList.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlackList> getBlackListById(@PathVariable("id") Long id) {
        Optional<BlackList> blackList = blackListService.getBlackListById(id);
        return blackList.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BlackList> addToBlackList(@RequestBody BlackList blackList) {
        Optional<BlackList> addedBlackList = Optional.ofNullable(blackListService.addToBlackList(blackList));
        return addedBlackList.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/byRating")
    public ResponseEntity<List<BlackList>> getBlackListByRating(@RequestParam("rating") Integer rating) {
        Optional<List<BlackList>> blackList = Optional.ofNullable(blackListService.getBlackListByRating(rating));
        return blackList.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/byPersonId")
    public ResponseEntity<List<BlackList>> getBlackListByPersonId(@RequestParam("personId") Long personId) {
        Optional<List<BlackList>> blackList = Optional.ofNullable(blackListService.getBlackListByPersonId(personId));
        return blackList.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "ID of the person to be deleted") @PathVariable("id") Long id) {
        blackListService.deleteBlackListById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}