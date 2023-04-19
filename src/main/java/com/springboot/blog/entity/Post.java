package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

//ModelMapperでエンティティとDTOをマッピングする時にtoStringがあると無限ループになっちゃうらしいから@Dataではなく@Getter,@Setterにした
//↑なぜ無限ループになるのかはよくわからない。
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//JPA entity
@Entity
//You can give a name to the table,and provide unique constraints.
//Postgresの場合、schemaは設定するべき。schema設定してないとselectで抽出できない？ぽい
@Table(
        name = "posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}, schema = "public"
)
public class Post {
    //to specify a primary key
    @Id
    //to specify primary key generation strategy
    //プライマリキーカラムにユニークな値を自動で生成，付与する方法を指定するアノテーション
    @GeneratedValue(
            //データベースのID列を使用して主キー値を生成する。
            strategy = GenerationType.IDENTITY
    )
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "content", nullable = false)
    private String content;

    //mappedBy:関連関係のオーナーを決定する。
    //cascade:Cascadeを指定すると複数のテーブルにまたがるDB操作を連鎖的に行うことが可能になります。
    //orphanRemoval:oneToManyに、orphanRemoval=true属性をつけないと、関連テーブルを削除してもメモリ上だけで、永続化がされない。
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    //Listは重複可、Setは重複不可→ここではSetを使用する
    private Set<Comment> comments = new HashSet<>();

    //One category has many posts
    //FetchType.LAZY : Whenever we load Post entity,then the category object won't load immediately.
    //We can get category object from post entity object on demand just by calling the getter.
    //In order to improve performance, we use fetch type Lazy.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
