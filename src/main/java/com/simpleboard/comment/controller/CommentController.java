package com.simpleboard.comment.controller;

import com.simpleboard.comment.dto.CommentForm;
import com.simpleboard.comment.service.CommentService;
import com.simpleboard.user.security.SecurityUser;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public String create(
            @PathVariable Long postId,
            @Valid @ModelAttribute("commentForm") CommentForm commentForm,
            BindingResult bindingResult,
            @AuthenticationPrincipal SecurityUser loginUser
    ) {
        if (bindingResult.hasErrors()) {
            return "redirect:/posts/" + postId;
        }

        commentService.create(postId, loginUser.getId(), commentForm);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{commentId}/delete")
    public String delete(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal SecurityUser loginUser
    ) {
        commentService.delete(commentId, loginUser.getId());
        return "redirect:/posts/" + postId;
    }
}