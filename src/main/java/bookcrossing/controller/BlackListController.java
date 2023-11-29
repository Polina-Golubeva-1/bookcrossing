package bookcrossing.controller;

import bookcrossing.domain.BlackList;
import bookcrossing.service.BlackListService;
import io.swagger.v3.oas.annotations.Parameter;
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

    @GetMapping("/findByPersonId/{personId}")
    public ResponseEntity<BlackList> findByPersonId(@PathVariable Long personId) {
        Optional<BlackList> blackList = blackListService.findByPersonId(personId);
        return blackList.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BlackList> addToBlackList(@RequestParam Long personId) {
        Optional<BlackList> blackListEntry = blackListService.addToBlackList(personId);

        return blackListEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "ID of the person to be deleted") @PathVariable("id") Long id) {
        blackListService.deleteBlackListById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}