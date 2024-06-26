package com.omkar.blogeditor.buisness.service.impl;

import com.omkar.blogeditor.buisness.service.PostsService;
import com.omkar.blogeditor.infra.entity.Posts;
import com.omkar.blogeditor.infra.entity.User;
import com.omkar.blogeditor.infra.model.BaseResponse;
import com.omkar.blogeditor.infra.model.UserDetailModel;
import com.omkar.blogeditor.infra.model.request.SetPostRequest;
import com.omkar.blogeditor.infra.model.response.GetPostResponse;
import com.omkar.blogeditor.infra.repository.PostRepository;
import com.omkar.blogeditor.infra.repository.UserRepository;
import com.omkar.blogeditor.security.CustomUserDetailsService;
import com.omkar.blogeditor.security.jwt.JwtUtil;
import com.omkar.blogeditor.util.AppUtil;
import com.omkar.blogeditor.util.response_builders.BaseFailure;
import com.omkar.blogeditor.util.response_builders.BaseSuccess;
import com.omkar.blogeditor.util.response_builders.failure.FailureResponseBuilder;
import com.omkar.blogeditor.util.response_builders.success.SuccessResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostsServiceImpl implements PostsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppUtil appUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private SuccessResponseBuilder successResponse;

    @Autowired
    private FailureResponseBuilder failureResponse;

    @Autowired
    private BaseSuccess baseSuccess;

    @Autowired
    private BaseFailure baseFailure;

    @Autowired
    private PostRepository postRepository;

    @Override
    public BaseResponse setPost(String authorizationHeader, SetPostRequest request) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetailModel userDetails = (UserDetailModel) userDetailsService.loadUserByUsername(email);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))){
            try {
                Optional<User> user = userRepository.findByEmail(email);
                if (user.isPresent()) {
                    Posts posts = Posts.builder()
                            .category(request.getCategory())
                            .title(request.getTitle())
                            .content(request.getContent())
                            .date(appUtil.getDateMonthYear())
                            .user(user.get())
                            .build();
                    postRepository.save(posts);
                    return baseSuccess.baseSuccessResponse("Posted");
                }
                return baseFailure.baseFailResponse("Error Posting");
            } catch (Exception ex) {
                return baseFailure.baseFailResponse("Something went wrong");
            }
        }
        return baseFailure.baseFailResponse("Invalid Jwt");
    }

    @Override
    public GetPostResponse getPosts(String authorizationHeader) {
        String jwt =authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetailModel userDetails = (UserDetailModel) userDetailsService.loadUserByUsername(email);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))){
            try {
                Optional<User> user = userRepository.findByEmail(email);
                if (user.isPresent()){
                    List<Posts> posts = postRepository.findAllByUser(user.get());
                    return successResponse.getPosts(posts);
                }
                return failureResponse.getPosts("User not present");
            } catch (Exception ex){
                return failureResponse.getPosts("Something Went Wrong");
            }
        }
        return failureResponse.getPosts("Invalid Jwt");
    }

    @Override
    public GetPostResponse getAllPosts() {
        List<Posts> allPosts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return successResponse.getAllPosts(allPosts);
    }

    @Override
    public GetPostResponse getPostId(Long postId) {
        Optional<Posts> post = postRepository.findById(postId);
        return successResponse.getPostId(post);
    }

    @Override
    public BaseResponse deletePost(String authorizationHeader, Long id) {
        String jwt =authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetailModel userDetails = (UserDetailModel) userDetailsService.loadUserByUsername(email);
        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            try {
                Optional<User> user = userRepository.findByEmail(email);
                if (user.isPresent()) {
                    Optional<Posts> posts = postRepository.findByIdAndUser(id, user.get());
                    if (posts.isPresent()) {
                        postRepository.delete(posts.get());
                        return baseSuccess.baseSuccessResponse("post deleted");
                    } else {
                        return baseFailure.baseFailResponse("You don't have permission to delete this post");
                    }
                } else {
                    return baseFailure.baseFailResponse("User is not present");
                }
            } catch (Exception ex) {
                return baseFailure.baseFailResponse("Something went wrong");

            }
        }
        return baseFailure.baseFailResponse("Invalid JWT");
    }

}
