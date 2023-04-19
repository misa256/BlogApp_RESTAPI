package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(
        name = "comments",
        schema = "public"
)
public class Comment {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String body;

    //Many→comments, One→post
    //The FetchType.LAZY tells Hibernate to only fetch the related entities from DB when you use the relationship.
    @ManyToOne(fetch = FetchType.LAZY)
    //specify foreign key
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
