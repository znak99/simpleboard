package com.simpleboard.post.controller;

import com.simpleboard.comment.service.CommentService;
import com.simpleboard.post.dto.PostListItem;
import com.simpleboard.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("게시글 목록 페이지를 조회할 수 있다")
    void list() throws Exception {
        when(postService.findPage(any(), any())).thenReturn(new PageImpl<>(
                List.of(new PostListItem(
                        1L,
                        "테스트 제목",
                        "tester",
                        LocalDateTime.of(2026, 3, 21, 23, 0)
                )),
                PageRequest.of(0, 10),
                1
        ));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/list"));
    }
}
