package shop.dream.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dream.domain.Account;
import shop.dream.domain.Comment;
import shop.dream.domain.Post;
import shop.dream.dto.CommentRequestDto;
import shop.dream.repository.AccountRepository;
import shop.dream.repository.CommentRepository;
import shop.dream.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void saveComment(Post post, CommentRequestDto commentRequestDto, Account account) {

        Comment comment = Comment.builder().comment(commentRequestDto.getComment()).build();
        Comment newComment = commentRepository.save(comment);
        post.addComment(newComment);
        account.addComment(newComment);
    }
    @Transactional
    public void updateComment(Long commentId, String stringComment) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
        comment.updateComment(stringComment);
    }
    @Transactional
    public void deleteComment(Long postId, Long commentId, Long accountId) {
        Comment deleteComment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다")
        );
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

        post.deleteComment(deleteComment);
        account.deleteComment(deleteComment);
        commentRepository.deleteById(deleteComment.getId());

    }
}
