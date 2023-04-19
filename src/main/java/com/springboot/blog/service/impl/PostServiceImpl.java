package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final ModelMapper mapper;

    private final CategoryRepository categoryRepository;

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("category", "id", postDTO.getCategoryId()));

        //convert DTO into Entity
        Post post = mapToEntity(postDTO);
        post.setCategory(category);
        //save entity
        Post responseEntity = postRepository.save(post);
        //convert entity into DTO
        return mapToDTO(responseEntity);
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        //if(sortDir.equals(Sort.Direction.ASC.name())){ASCの新しいSortクラスを返す}
        //else{DESCの新しいSortクラスを返す}
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //create pageable instance, デフォルトでASCで並べ替えられる
        //PageRequestクラスはPageableインタフェースの実装クラス
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        //create page instance
       Page<Post> postPage = postRepository.findAll(pageable);
       //get content for page object
        List<Post> listOfPosts = postPage.getContent();
        //convert List of entity to List of DTO
       List<PostDTO> content = listOfPosts.stream()
               .map(post->mapToDTO(post))
               .collect(Collectors.toList());
        PostResponse postResponse = getPostResponse(postPage, content);
        return postResponse;
    }



    @Override
    public PostDTO getPostById(long id) {
        Optional<Post> postOpt = postRepository.findById(id);
        Post post = postOpt.orElseThrow(()->{throw new ResourceNotFoundException("Post","id",id);});
        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, long id) {
        //get post by id from DB
        Post post = postRepository.findById(id).orElseThrow(()->{throw new ResourceNotFoundException("Post","id",id);});
        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("category", "id", postDTO.getCategoryId()));
        //update
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());
        post.setCategory(category);
        //save in DB
        Post updatedPost = postRepository.save(post);
        //convert entity into DTO
        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        //get post by id from DB
        Post post = postRepository.findById(id).orElseThrow(()->{throw new ResourceNotFoundException("Post","id",id);});
        postRepository.delete(post);
    }

    @Override
    public List<PostDTO> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category", "id", categoryId));

        List<Post> posts = postRepository.findByCategory(category);

        return posts.stream()
                .map(post -> mapToDTO(post))
                .collect(Collectors.toList());
    }

    //convert entity into DTO
    private PostDTO mapToDTO(Post post) {
        PostDTO postDTO = mapper.map(post,PostDTO.class);
        return postDTO;
    }
    //convert DTO into Entity
    private Post mapToEntity(PostDTO postDTO) {
        Post post = mapper.map(postDTO, Post.class);
        return post;
    }

    //create PostResponse instance
    private static PostResponse getPostResponse(Page<Post> postPage, List<PostDTO> content) {
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(postPage.getNumber());
        postResponse.setPageSize(postPage.getSize());
        postResponse.setTotalElements(postPage.getTotalElements());
        postResponse.setTotalPages(postPage.getTotalPages());
        postResponse.setLast(postPage.isLast());
        return postResponse;
    }


}
