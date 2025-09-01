package edu.util;

import dto.CommentDTO;
import edu.entity.Comment;
import edu.entity.Task;
import edu.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class ConverterCommentDTO {

    public Comment convertCommentDTO(CommentDTO commentDTO, User user, Task task) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setTask(task);
        comment.setContent(commentDTO.getContent());
        comment.setDate(commentDTO.getDate());

        return comment;
    }

    public CommentDTO convertComment(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(comment.getCommentId());
        commentDTO.setAuthorName(comment.getAuthor().getUserName());
        commentDTO.setTaskId(comment.getTask().getTaskId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setDate(comment.getDate());

        return commentDTO;
    }

    public List<CommentDTO> convertComments(List<Comment> comments) {
        return comments.stream()
                .map(comment -> {
//                    assert comment. != null;
                    return new CommentDTO(
                            comment.getCommentId(),
                            comment.getContent(),
                            comment.getAuthor().getUserName(),
                            comment.getTask().getTaskId(),
                            comment.getDate()
                    );
                })
                .collect(Collectors.toList());
    }

}
