package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(
            //データベースのID列を使用して主キー値を生成する。
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String name;
    private String description;
    //mappedBy = "category" : Postクラスのフィールド、category
    //And this attribute tells hibernate that we are using one to many mapping and don't create additional table.
    //orphanRemoval = true : this attribute tells hibernate that remove all the orphaned entities from DB table.
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

}
