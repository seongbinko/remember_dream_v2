package shop.dream.testdata;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import shop.dream.domain.Account;
import shop.dream.domain.AccountRole;
import shop.dream.domain.Post;
import shop.dream.repository.AccountRepository;
import shop.dream.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestDataRunner implements ApplicationRunner {

    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account testAccount = Account.builder()
                .username("테스트유저")
                .role(AccountRole.ROLE_USER)
                .email("wnrhd1082@naver.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        Account newAccount = accountRepository.save(testAccount);

        List<Post> posts = new ArrayList<>();
        for(int i=1000; i < 1051; i++) {
            Post post = new Post();
            post.setTitle(String.valueOf(i));
            post.setContents(String.valueOf(UUID.randomUUID().toString()));
            post.setAccount(newAccount);
            posts.add(post);
        }
        postRepository.saveAll(posts);
    }
}
