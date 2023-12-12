package bookcrossing.security.service;

import bookcrossing.domain.Person;
import bookcrossing.domain.Role;
import bookcrossing.exeption_resolver.SameUserInDatabaseException;
import bookcrossing.repository.PersonRepository;
import bookcrossing.security.domain.SecurityCredentials;
import bookcrossing.security.domain.dto.AuthRequest;
import bookcrossing.security.domain.dto.RegistrationDTO;
import bookcrossing.security.repository.SecurityCredentialsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SecurityService {

    private final SecurityCredentialsRepository securityCredentialsRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public Optional<String> generateToken(AuthRequest authRequest) {
        Optional<SecurityCredentials> personCredentials =
                securityCredentialsRepository.getByUserName(authRequest.getLogin());
        if (personCredentials.isPresent() &&
                passwordEncoder.matches(authRequest.getPassword(), personCredentials.get().getPersonPassword())) {
            return Optional.of(jwtUtils.generateJwtToken(authRequest.getLogin()));
        }
        return Optional.empty();
    }

    public Long getUserIdByLogin(String login) {
        return securityCredentialsRepository.findUserIdByLogin(login);
    }

    @Transactional(rollbackOn = Exception.class)
    public void registration(RegistrationDTO registrationDTO) {
        Optional<SecurityCredentials> result = securityCredentialsRepository.getByUserName(registrationDTO.getUserName());
        if (result.isPresent()) {
            throw new SameUserInDatabaseException();
        }
        Person person = new Person();
        person.setFirstName(registrationDTO.getFirstName());
        person.setSecondName(registrationDTO.getSecondName());
        person.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        person.setAge(registrationDTO.getAge());
        person.setPhone(registrationDTO.getPhone());
        person.setEmail(registrationDTO.getEmail());
        person.setAddress(registrationDTO.getAddress());
        person.setRating(10);
        Person userInfoResult = personRepository.save(person);

        SecurityCredentials securityCredentials = new SecurityCredentials();
        securityCredentials.setUserName(registrationDTO.getUserName());
        securityCredentials.setPersonPassword(passwordEncoder.encode(registrationDTO.getPersonPassword()));
        securityCredentials.setRole(Role.USER);
        securityCredentials.setPersonId(userInfoResult.getId());
        securityCredentialsRepository.save(securityCredentials);
    }

    public boolean checkAccessById(Long id) {
        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        String userRole = String.valueOf(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .findFirst()
                .get());
        Long userId = securityCredentialsRepository.findUserIdByLogin(userLogin);

        return (userId.equals(id) || userRole.equals("ROLE_ADMIN"));
    }
}
