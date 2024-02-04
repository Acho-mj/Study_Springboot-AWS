package com.acho.studyAws.web.controller;

import com.acho.studyAws.config.auth.LoginUser;
import com.acho.studyAws.config.auth.dto.SessionUser;
import com.acho.studyAws.service.posts.PostsService;
import com.acho.studyAws.web.dto.PostsResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){
        // 게시물리스트를 posts라는 이름으로 모델에 추가
        model.addAttribute("posts",postsService.findAllDesc());

        //SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user!=null){
            model.addAttribute("userName",user.getName());
        }
        return "index";
    }

    // 글 등록
    @GetMapping("/posts/save")
    public String postSave() {
        return "post-save";
    }

    // 글 수정
    @GetMapping("/posts/update/{id}")
    public String postUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "post-update";
    }
}
