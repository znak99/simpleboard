package com.simpleboard.post.service;

import com.simpleboard.post.domain.Post;
import com.simpleboard.post.dto.PostDetailView;
import com.simpleboard.post.dto.PostForm;
import com.simpleboard.post.dto.PostListItem;
import com.simpleboard.post.dto.PostSearchCondition;
import com.simpleboard.post.repository.PostRepository;
import com.simpleboard.user.domain.User;
import com.simpleboard.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<PostListItem> findPage(PostSearchCondition condition, Pageable pageable) {
        Page<Post> postPage;

        if (condition != null && condition.hasKeyword()) {
            postPage = postRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(
                    condition.getKeyword(),
                    pageable
            );
        } else {
            postPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        return postPage.map(post -> new PostListItem(
                post.getId(),
                post.getTitle(),
                post.getAuthor().getName(),
                post.getCreatedAt()
        ));
    }

    @Transactional(readOnly = true)
    public PostDetailView findById(Long postId) {
        Post post = getPost(postId);

        return new PostDetailView(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getName(),
                post.getAuthor().getId(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    public Long create(PostForm form, Long loginUserId) {
        User author = userRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Post post = new Post(
                form.getTitle(),
                form.getContent(),
                author
        );

        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    public void update(Long postId, PostForm form, Long loginUserId) {
        Post post = getPost(postId);
        validateAuthor(post, loginUserId);
        post.update(form.getTitle(), form.getContent());
    }

    public void delete(Long postId, Long loginUserId) {
        Post post = getPost(postId);
        validateAuthor(post, loginUserId);
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public PostForm getEditForm(Long postId, Long loginUserId) {
        Post post = getPost(postId);
        validateAuthor(post, loginUserId);
        return new PostForm(post.getTitle(), post.getContent());
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    private void validateAuthor(Post post, Long loginUserId) {
        if (!post.getAuthor().getId().equals(loginUserId)) {
            throw new IllegalArgumentException("작성자만 수정 또는 삭제할 수 있습니다.");
        }
    }
}