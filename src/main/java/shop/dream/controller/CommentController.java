package shop.dream.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.dream.config.UserDetailsImpl;
import shop.dream.domain.Comment;
import shop.dream.domain.Post;
import shop.dream.dto.CommentRequestDto;
import shop.dream.repository.CommentRepository;
import shop.dream.repository.PostRepository;
import shop.dream.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/posts/{postId}/comments")
    public List<Comment> getCommentsByRecipeNo(@PathVariable Long postId) {

        return commentRepository.findByPostId(postId);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity saveComment(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto commentRequestDto) {
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null) {
            return ResponseEntity.badRequest().build();
        }
        commentService.saveComment(post, commentRequestDto, userDetails.getAccount());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public void updateComment(@PathVariable Long postId, @PathVariable Long commentId,
                              @RequestBody String comment) throws Exception {
        commentService.updateComment(commentId, comment);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(postId, commentId, userDetails.getAccount().getId());
    }

}
