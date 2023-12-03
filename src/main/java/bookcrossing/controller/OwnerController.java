package bookcrossing.controller;


import bookcrossing.domain.Owner;
import bookcrossing.service.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public ResponseEntity<List<Owner>> getAllOwners() {
        List<Owner> owners = ownerService.findAll();
        return ResponseEntity.ok(owners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Owner> getOwnerById(@PathVariable Long id) {
        Owner owner = ownerService.findById(id);
        return ResponseEntity.ok(owner);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerOwner(@RequestParam Long personId, @RequestParam Long bookId) {
        ownerService.registerOwner(personId, bookId);
        return ResponseEntity.ok("Owner registered successfully.");
    }
}
