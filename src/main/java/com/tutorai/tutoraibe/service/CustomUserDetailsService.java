package com.tutorai.tutoraibe.service;

import com.tutorai.tutoraibe.entity.User;
import com.tutorai.tutoraibe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var auth = new SimpleGrantedAuthority("ROLE_" + u.getRole().name());
        boolean enabled = u.getStatus() == User.Status.ACTIVE;
        return new org.springframework.security.core.userdetails.User(
                u.getEmail(), u.getPassword(), enabled, true, true, u.getStatus()!= User.Status.BANNED, List.of(auth)
        );
    }
}
