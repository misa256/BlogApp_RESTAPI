package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@Schema(
        description = "PostDTO Model information"
)
public class PostDTO {
    private long id;
    //title should not be null or empty
    //title should have at least 2 characters
    //Bean Validationのアノテーションを１つのプロパティに複数付与し、検証時に複数件エラーとなる場合、
    // エラーの表示順は実行するたびに異なります。
    @NotBlank
    @Size(min = 2, message = "Post title should have at least 2 characters")
    @Schema(
            description = "Blog Post Title"
    )
    private String title;

    @NotBlank
    @Size(min = 10, message = "Post description should have at least 10 characters")
    @Schema(
            description = "Blog Post description"
    )
    private String description;

    @NotBlank
    @Schema(
            description = "Blog Post content"
    )
    private String content;

    private Set<CommentDTO> comments;

    @Schema(
            description = "Blog Post Category"
    )
    private Long categoryId;
}
