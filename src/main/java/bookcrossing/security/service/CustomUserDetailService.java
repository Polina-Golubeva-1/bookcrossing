package bookcrossing.security.service;


import bookcrossing.exeption_resolver.UserFromDatabaseNotFound;
import bookcrossing.security.domain.SecurityCredentials;
import bookcrossing.security.repository.SecurityCredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final SecurityCredentialsRepository credentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SecurityCredentials> userFromDatabase = credentialsRepository.getByPersonLogin(username);
        if (userFromDatabase.isEmpty()) {
            throw new UserFromDatabaseNotFound();
        }
        SecurityCredentials user = userFromDatabase.get();
        return User
                .withUsername(user.getUserName())
                .password(user.getPersonPassword())
                .roles(user.getRole().toString())
                .build();
    }
}