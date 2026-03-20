package com.simpleboard.comment.service;

import com.simpleboard.comment.domain.Comment;
import com.simpleboard.comment.dto.CommentForm;
import com.simpleboard.comment.dto.CommentView;
import com.simpleboard.comment.repository.CommentRepository;
import com.simpleboard.post.domain.Post;
import com.simpleboard.post.repository.PostRepository;
import com.simpleboard.user.domain.User;
import com.simpleboard.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<CommentView> findByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(comment -> new CommentView(
                        comment.getId(),
                        comment.getContent(),
                        comment.getAuthor().getId(),
                        comment.getAuthor().getName(),
                        comment.getCreatedAt()
                ))
                .toList();
    }

    public void create(Long postId, Long loginUserId, CommentForm form) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        User author = userRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Comment comment = new Comment(form.getContent(), post, author);
        commentRepository.save(comment);
    }

    public void delete(Long commentId, Long loginUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getAuthor().getId().equals(loginUserId)) {
            throw new IllegalArgumentException("댓글 작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}