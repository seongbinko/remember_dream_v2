package shop.dream.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dream.domain.Account;
import shop.dream.domain.Comment;
import shop.dream.domain.Post;
import shop.dream.dto.PostRequestDto;
import shop.dream.repository.AccountRepository;
import shop.dream.repository.CommentRepository;
import shop.dream.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;

    public void savePost(PostRequestDto postRequestDto, Account account) {
        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .contents(postRequestDto.getContents())
                .build();

        Post newPost =  postRepository.save(post);
        account.addPost(newPost);
    }

    public Post updatePost(Long postId, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        post.updatePost(postRequestDto);
        return post;
    }

    public void deletePost(Post post, Account account) {
        List<Comment> commentList = commentRepository.findAllByPostId(post.getId());

        post.deleteComments(commentList);
        commentRepository.deleteAll(commentList);

        for(Comment comment : commentList) {
            Account havenCommentAccount = accountRepository.findById(comment.getAccount().getId()).orElseThrow(
                    () -> new IllegalArgumentException()
            );
            havenCommentAccount.deleteComment(comment);
        }
        account.deletePost(post);
        postRepository.delete(post);
    }

}
