package com.nexusblog.dto;

import com.nexusblog.persistence.entity.Post;
import com.nexusblog.persistence.entity.Profile;
import com.nexusblog.persistence.entity.Role;
import com.nexusblog.persistence.entity.User;

import java.util.stream.Collectors;

public class ConverterDto {
    public static UserDto userToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getUsername(),
                user.isEnabled(),
                user.getRoles().stream().map(ConverterDto::roleToDto).collect(Collectors.toSet()),
                user.getPosts().stream().map(ConverterDto::postToDto).collect(Collectors.toSet()),
                ConverterDto.profileToDto(user.getProfile())
        );
    }

    public static RoleDto roleToDto(Role role) {
        RoleDto roleDto = new RoleDto(
                role.getId(),
                role.getName());
        if(role.getUsers() != null){
            roleDto.setUserIds(role.getUsers().stream().map(User::getId).collect(Collectors.toSet()));
        }
        return roleDto;
    }

    public static PostDto postToDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreated(),
                post.getUpdated(),
                post.getUser().getUsername()
        );
    }

    public static ProfileDto profileToDto(Profile profile) {
        return new ProfileDto(
                profile.getId(),
                profile.getName(),
                profile.getSurname(),
                profile.getMail(),
                profile.getBirthdate(),
                profile.getUser().getId()
        );
    }
}
