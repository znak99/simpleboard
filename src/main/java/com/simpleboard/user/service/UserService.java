package com.simpleboard.user.service;

import com.simpleboard.user.domain.User;
import com.simpleboard.user.dto.UserSignUpForm;
import com.simpleboard.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(UserSignUpForm form) {
        validateDuplicateEmail(form.getEmail());

        String encodedPassword = passwordEncoder.encode(form.getPassword());

        User user = new User(
                form.getEmail(),
                encodedPassword,
                form.getName()
        );

        return userRepository.save(user);
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }
}