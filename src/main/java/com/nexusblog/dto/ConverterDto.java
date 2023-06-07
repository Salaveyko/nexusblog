package com.nexusblog.dto;

import com.nexusblog.persistence.entity.*;

import java.util.stream.Collectors;

public class ConverterDto {
    public static UserDto userToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getPassword(),
                user.isEnabled(),
                user.getRoles().stream().map(ConverterDto::roleToDto).collect(Collectors.toSet()),
                user.getPosts().stream().map(ConverterDto::postToDto).collect(Collectors.toSet()),
                profileToDto(user.getProfile()));
    }

    public static RoleDto roleToDto(Role role) {
        RoleDto roleDto = new RoleDto(
                role.getId(),
                role.getName());
        if (role.getUsers() != null) {
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
                post.getUser().getUsername(),
                post.getUser().getProfile().getAvatarPath());
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
                profile.getUser().getUsername());
    }

    public static ProfileContactsDto profileContactToDto(ProfileContacts profileContacts) {
        return new ProfileContactsDto(
                profileContacts.getId(),
                profileContacts.getPhone(),
                profileContacts.getEmail());
    }

    public static AddressDto addressToDto(Address address) {
        return new AddressDto(
                address.getId(),
                address.getCountry(),
                address.getStatement(),
                address.getStreet(),
                address.getBuildingNumber(),
                address.getPostalCode());
    }

    public static Profile profileFromDto(Profile profile, ProfileDto profileDto) {
        profile.setAvatarPath(profileDto.getAvatarPath());
        profile.setName(profileDto.getName());
        profile.setSurname(profileDto.getSurname());
        profile.setBirthdate(profileDto.getBirthdate());
        profile.setContacts(
                profileContactsFromDto(profile.getContacts(), profileDto.getContacts()));
        profile.setAddress(
                addressFromDto(profile.getAddress(), profileDto.getAddress()));

        return profile;
    }

    public static Address addressFromDto(Address address, AddressDto addressDto) {
        address.setCountry(addressDto.getCountry());
        address.setStatement(addressDto.getStatement());
        address.setStreet(addressDto.getStreet());
        address.setBuildingNumber(addressDto.getBuildingNumber());
        address.setPostalCode(addressDto.getPostalCode());
        return address;
    }

    public static ProfileContacts profileContactsFromDto(ProfileContacts prfContacts,
                                                         ProfileContactsDto prfContactsDto) {
        if (!prfContactsDto.getEmail().isEmpty()) {
            prfContacts.setEmail(prfContactsDto.getEmail());
        }
        if (!prfContactsDto.getPhone().isEmpty()) {
            prfContacts.setPhone(prfContactsDto.getPhone());
        }
        return prfContacts;
    }
}
