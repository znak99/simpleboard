package com.simpleboard.post.service;

import com.simpleboard.post.dto.PostForm;
import com.simpleboard.post.repository.PostRepository;
import com.simpleboard.user.domain.User;
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
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("로그인 사용자는 게시글을 작성할 수 있다")
    void create_success() {
        User user = userRepository.save(new User("writer@example.com", "encoded-password", "writer"));

        PostForm form = new PostForm();
        form.setTitle("첫 글");
        form.setContent("내용입니다.");

        Long postId = postService.create(form, user.getId());

        assertThat(postId).isNotNull();
        assertThat(postRepository.findById(postId)).isPresent();
    }

    @Test
    @DisplayName("작성자만 게시글을 수정할 수 있다")
    void update_fail_notAuthor() {
        User author = userRepository.save(new User("author@example.com", "pw1", "author"));
        User other = userRepository.save(new User("other@example.com", "pw2", "other"));

        PostForm createForm = new PostForm();
        createForm.setTitle("원래 제목");
        createForm.setContent("원래 내용");
        Long postId = postService.create(createForm, author.getId());

        PostForm updateForm = new PostForm();
        updateForm.setTitle("수정 제목");
        updateForm.setContent("수정 내용");

        assertThatThrownBy(() -> postService.update(postId, updateForm, other.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("작성자만 수정 또는 삭제할 수 있습니다.");
    }

    @Test
    @DisplayName("작성자는 게시글을 수정할 수 있다")
    void update_success() {
        User author = userRepository.save(new User("author2@example.com", "pw1", "author2"));

        PostForm createForm = new PostForm();
        createForm.setTitle("원래 제목");
        createForm.setContent("원래 내용");
        Long postId = postService.create(createForm, author.getId());

        PostForm updateForm = new PostForm();
        updateForm.setTitle("수정 제목");
        updateForm.setContent("수정 내용");

        postService.update(postId, updateForm, author.getId());

        var post = postRepository.findById(postId).orElseThrow();
        assertThat(post.getTitle()).isEqualTo("수정 제목");
        assertThat(post.getContent()).isEqualTo("수정 내용");
    }
}