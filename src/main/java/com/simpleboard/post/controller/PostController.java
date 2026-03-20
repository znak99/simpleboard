package com.simpleboard.post.controller;

import com.simpleboard.comment.dto.CommentForm;
import com.simpleboard.comment.service.CommentService;
import com.simpleboard.post.dto.PostDetailView;
import com.simpleboard.post.dto.PostForm;
import com.simpleboard.post.dto.PostListItem;
import com.simpleboard.post.dto.PostSearchCondition;
import com.simpleboard.post.service.PostService;
import com.simpleboard.user.security.SecurityUser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public String list(
            @ModelAttribute("condition") PostSearchCondition condition,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal SecurityUser loginUser,
            Model model
    ) {
        Page<PostListItem> postPage = postService.findPage(condition, pageable);

        model.addAttribute("postPage", postPage);
        model.addAttribute("loginUser", loginUser);

        return "posts/list";
    }

    @GetMapping("/{postId}")
    public String detail(
            @PathVariable Long postId,
            @AuthenticationPrincipal SecurityUser loginUser,
            Model model
    ) {
        PostDetailView post = postService.findById(postId);

        model.addAttribute("post", post);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("comments", commentService.findByPostId(postId));
        model.addAttribute("commentForm", new CommentForm());

        return "posts/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        return "posts/form";
    }

    @PostMapping("/new")
    public String create(
            @Valid @ModelAttribute PostForm postForm,
            BindingResult bindingResult,
            @AuthenticationPrincipal SecurityUser loginUser
    ) {
        if (bindingResult.hasErrors()) {
            return "posts/form";
        }

        Long postId = postService.create(postForm, loginUser.getId());
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/{postId}/edit")
    public String editForm(
            @PathVariable Long postId,
            @AuthenticationPrincipal SecurityUser loginUser,
            Model model
    ) {
        PostForm postForm = postService.getEditForm(postId, loginUser.getId());
        model.addAttribute("postForm", postForm);
        model.addAttribute("postId", postId);
        return "posts/edit-form";
    }

    @PostMapping("/{postId}/edit")
    public String edit(
            @PathVariable Long postId,
            @Valid @ModelAttribute PostForm postForm,
            BindingResult bindingResult,
            @AuthenticationPrincipal SecurityUser loginUser,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("postId", postId);
            return "posts/edit-form";
        }

        postService.update(postId, postForm, loginUser.getId());
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/delete")
    public String delete(
            @PathVariable Long postId,
            @AuthenticationPrincipal SecurityUser loginUser
    ) {
        postService.delete(postId, loginUser.getId());
        return "redirect:/posts";
    }
}