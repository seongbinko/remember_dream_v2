package shop.dream.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import shop.dream.domain.Comment;

import javax.persistence.NamedNativeQueries;
import java.util.List;

@Transactional(readOnly = true)

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);

    List<Comment> findAllByPostId(Long id);
}
