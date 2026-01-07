package com.TextHub.app.mapper;

import com.TextHub.TextHub.Entity.Post;
import com.TextHub.TextHub.Entity.PostDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "login", source = "user.login")
    @Mapping(target = "likesCount", expression = "java(post.getLikesCount())")
    @Mapping(target = "likedByCurrentUser", ignore = true)
    PostDTO toDto(Post post, @Context Long currentUserId);

    List<PostDTO> toDto(List<Post> posts, @Context Long currentUserId);

    @AfterMapping
    default void fillDerived(Post post, @Context Long currentUserId, @MappingTarget PostDTO dto) {
        boolean liked = currentUserId != null && post.isLikedByUser(currentUserId);
        dto.setLikedByCurrentUser(liked);
    }
}
