package dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDTO {
    Long commentId;

    String content;

    Long authorId;

    Long taskId;

    LocalDateTime date = LocalDateTime.now();
}
