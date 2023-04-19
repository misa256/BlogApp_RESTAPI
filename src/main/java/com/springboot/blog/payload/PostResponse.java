package com.springboot.blog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//provide all these fields in response of the Pagination API
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private List<PostDTO> content;
    private int pageNo;
    private int pageSize;
    //要素の総数
    private long totalElements;
    private int totalPages;
    //現在のページ(スライス)が最後のものかどうか
    private boolean last;
}
