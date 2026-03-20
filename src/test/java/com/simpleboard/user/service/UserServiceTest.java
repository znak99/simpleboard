package com.simpleboard.user.service;

import com.simpleboard.user.domain.User;
import com.simpleboard.user.dto.UserSignUpForm;
import com.simpleboard.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입에 성공한다")
    void signUp_success() {
        UserSignUpForm form = new UserSignUpForm();
        form.setEmail("test@example.com");
        form.setPassword("12345678");
        form.setName("tester");

        User savedUser = userService.signUp(form);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getName()).isEqualTo("tester");
        assertThat(savedUser.getPassword()).isNotEqualTo("12345678");
    }

    @Test
    @DisplayName("중복 이메일이면 회원가입에 실패한다")
    void signUp_fail_duplicateEmail() {
        UserSignUpForm form1 = new UserSignUpForm();
        form1.setEmail("dup@example.com");
        form1.setPassword("12345678");
        form1.setName("user1");
        userService.signUp(form1);

        UserSignUpForm form2 = new UserSignUpForm();
        form2.setEmail("dup@example.com");
        form2.setPassword("87654321");
        form2.setName("user2");

        assertThatThrownBy(() -> userService.signUp(form2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용 중인 이메일입니다.");
    }
}