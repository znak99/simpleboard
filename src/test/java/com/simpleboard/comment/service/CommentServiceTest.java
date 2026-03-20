package com.simpleboard.comment.service;

import com.simpleboard.comment.dto.CommentForm;
import com.simpleboard.comment.repository.CommentRepository;
import com.simpleboard.post.domain.Post;
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
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("로그인 사용자는 댓글을 작성할 수 있다")
    void create_success() {
        User author = userRepository.save(new User("commenter@example.com", "pw", "commenter"));
        User postAuthor = userRepository.save(new User("poster@example.com", "pw", "poster"));
        Post post = postRepository.save(new Post("제목", "내용", postAuthor));

        CommentForm form = new CommentForm();
        form.setContent("댓글 내용");

        commentService.create(post.getId(), author.getId(), form);

        assertThat(commentRepository.findByPostIdOrderByCreatedAtAsc(post.getId())).hasSize(1);
    }

    @Test
    @DisplayName("댓글 작성자만 댓글을 삭제할 수 있다")
    void delete_fail_notAuthor() {
        User commentAuthor = userRepository.save(new User("author@example.com", "pw", "author"));
        User other = userRepository.save(new User("other@example.com", "pw", "other"));
        User postAuthor = userRepository.save(new User("postauthor@example.com", "pw", "postauthor"));

        Post post = postRepository.save(new Post("제목", "내용", postAuthor));

        CommentForm form = new CommentForm();
        form.setContent("댓글 내용");
        commentService.create(post.getId(), commentAuthor.getId(), form);

        Long commentId = commentRepository.findByPostIdOrderByCreatedAtAsc(post.getId()).get(0).getId();

        assertThatThrownBy(() -> commentService.delete(commentId, other.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글 작성자만 삭제할 수 있습니다.");
    }
}