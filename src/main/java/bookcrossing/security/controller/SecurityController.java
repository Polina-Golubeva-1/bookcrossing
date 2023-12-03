package bookcrossing.security.controller;

import bookcrossing.security.domain.dto.AuthRequest;
import bookcrossing.security.domain.dto.AuthResponse;
import bookcrossing.security.domain.dto.RegistrationDTO;
import bookcrossing.security.service.SecurityService;
import bookcrossing.service.PersonService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security")
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody RegistrationDTO registrationDTO) {
        securityService.registration(registrationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<AuthResponse> generateToken(HttpSession session, @RequestBody AuthRequest authRequest) {
        Long userId = securityService.getUserIdByLogin(authRequest.getLogin());
        session.setAttribute("userId", userId);
        Optional<String> token = securityService.generateToken(authRequest);
        if (token.isPresent()) {
            return new ResponseEntity<>(new AuthResponse(token.get()), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}