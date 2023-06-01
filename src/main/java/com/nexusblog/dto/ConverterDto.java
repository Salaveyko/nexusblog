package com.nexusblog.dto;

import com.nexusblog.persistence.entity.*;

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
                profileToDto(user.getProfile())
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
                profile.getAvatarPath(),
                profile.getName(),
                profile.getSurname(),
                profile.getBirthdate(),
                profileContactToDto(profile.getContacts()),
                addressToDto(profile.getAddress()),
                profile.getUser().getUsername()
        );
    }
    public static ProfileContactsDto profileContactToDto(ProfileContacts profileContacts){
        return new ProfileContactsDto(
                profileContacts.getId(),
                profileContacts.getPhone(),
                profileContacts.getEmail()
        );
    }
    public static AddressDto addressToDto(Address address){
        return new AddressDto(
                address.getId(),
                address.getCountry(),
                address.getStatement(),
                address.getStreet(),
                address.getBuildingNumber(),
                address.getPostalCode()
        );
    }
}
