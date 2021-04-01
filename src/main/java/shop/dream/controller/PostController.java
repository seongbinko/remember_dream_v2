package shop.dream.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.dream.config.UserDetailsImpl;
import shop.dream.domain.Account;
import shop.dream.domain.Post;
import shop.dream.dto.PostRequestDto;
import shop.dream.repository.AccountRepository;
import shop.dream.repository.PostRepository;
import shop.dream.service.PostService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final AccountRepository accountRepository;
    private final PostService postService;
    private final PostRepository postRepository;

    // 게시글 등록화면
    @GetMapping("/posts/update")
    public String postPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails == null) {
            return "/error/404";
        }
        model.addAttribute(new PostRequestDto());

        return "post/save-dream";
    }

    // 게시글 수정화면
    @GetMapping("/posts/update/{postId}")
    public String postUpdatePage(@PathVariable Long postId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Post post  = postRepository.findById(postId).orElse(null);
        if(post == null || post.getAccount().getUsername() != userDetails.getAccount().getUsername()) {
            return "error/404";
        }
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle(post.getTitle());
        postRequestDto.setContents(post.getContents());

        model.addAttribute(postRequestDto);
        model.addAttribute("postId", post.getId());

        return "post/update-dream";
    }

    // 게시글 전체조회
    @GetMapping("/posts")
    public String readPosts(Model model, String keyword,@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("postPage", postRepository.findByTitleIgnoreCaseContains(pageable,keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortProperty", pageable.getSort().toString().contains("DESC") ? "desc" : "asc");
        return "index";
    }

    //게시글 등록
    @PostMapping("/posts")
    public String savePost(@Valid @ModelAttribute PostRequestDto postRequestDto, Errors errors, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (errors.hasErrors()) {
            return "post/save-dream";
        }
        postService.savePost(postRequestDto, userDetails.getAccount());
        return "redirect:/";
    }

    // 게시글 수정 페이지
    @GetMapping("/posts/{postId}")
    public String readPost(@PathVariable Long postId, Model model) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return "error/404";
        }
        model.addAttribute("post",post);
        return "post/read-dream";
    }
    // 게시글 수정
    @PutMapping("/posts/{postId}")
    public String updatePost(@PathVariable Long postId, @Valid @ModelAttribute PostRequestDto postRequestDto, Errors errors, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            return "post/update-dream";
        }
        Post post = postService.updatePost(postId, postRequestDto);
        attributes.addFlashAttribute("message", "게시글을 수정했습니다.");

        return "redirect:" + "/posts/update/" + post.getId();
    }
    // 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public String deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElse(null);
        Account account = accountRepository.findById(userDetails.getAccount().getId()).orElse(null);

        if(post == null || account == null) {
            return "error/404";
        }
        postService.deletePost(post, account);
        return "redirect:/";
    }
}
