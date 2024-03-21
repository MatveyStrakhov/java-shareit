package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment toComment(CommentCreateDto commentCreateDto, Long userId, Long itemId) {
        Comment comment = Comment.builder()
                .userId(userId)
                .itemId(itemId)
                .text(commentCreateDto.getText())
                .created(LocalDateTime.now())
                .build();
        return comment;
    }

    public static CommentDto toCommentDto(Comment comment, User user) {
        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .authorName(user.getName())
                .created(comment.getCreated())
                .text(comment.getText())
                .build();
        return commentDto;
    }
}
