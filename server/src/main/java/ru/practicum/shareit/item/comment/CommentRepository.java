package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select new ru.practicum.shareit.item.comment.CommentDto(c.id, u.name, c.text, c.created) from Comment c join User u on c.userId = u.id where c.itemId = ?1")
    List<CommentDto> findCommentsByItemId(Long itemId);
}
