package com.nexusblog.persistence.service.Impl;

import com.google.common.base.VerifyException;
import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.UserDto;
import com.nexusblog.persistence.entity.*;
import com.nexusblog.persistence.repository.RoleRepository;
import com.nexusblog.persistence.repository.TokenRepository;
import com.nexusblog.persistence.repository.UserRepository;
import com.nexusblog.persistence.service.interfaces.UserService;
import com.nexusblog.util.TbConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final SecurityContextRepository securityContextRepository;

    @Override
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        Optional<Role> roleOpt = roleRepository.findByName(TbConstants.Roles.USER);
        Role role = roleOpt.orElseGet(() -> roleRepository.save(new Role(TbConstants.Roles.USER)));

        userDto.setUsername(userDto.getUsername().substring(0, 1).toUpperCase()
                + userDto.getUsername().substring(1).toLowerCase());

        User user = new User(
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()));
        user.addRole(role);

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setContacts(new ProfileContacts());
        profile.setAddress(new Address());
        profile.getContacts().setEmail(userDto.getProfile().getContacts().getEmail());

        user.setProfile(profile);

        userRepository.save(user);

        return ConverterDto.userToDto(user);
    }

    @Override
    @Transactional
    public UserDto findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return ConverterDto.userToDto(user.get());
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    @Transactional
    public void createVerificationToken(UserDto userDto, String token) {
        Optional<User> userOpt = userRepository.findByUsername(userDto.getUsername());
        if (userOpt.isEmpty()) throw new UsernameNotFoundException("User not found");

        VerificationToken myToken = new VerificationToken(token, userOpt.get());
        tokenRepository.save(myToken);
    }

    @Override
    @Transactional
    public void checkVerificationToken(String token, HttpServletRequest request,
                                          HttpServletResponse response) {
        Optional<VerificationToken> verTokenOpt = tokenRepository.findByToken(token);
        if (verTokenOpt.isEmpty()) {
            throw new VerifyException("Invalid verification token");
        }

        VerificationToken verToken = verTokenOpt.get();
        Calendar calendar = Calendar.getInstance();
        if (verToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0) {
            throw new VerifyException("Time expired");
        }

        User user = verToken.getUser();
        user.setEnabled(true);

        userRepository.save(user);
        tokenRepository.delete(verToken);

        authenticateUser(user, request, response);

    }

    public void authenticateUser(User user, HttpServletRequest request,
                                   HttpServletResponse response){

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
                .getContextHolderStrategy();
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();

        context.setAuthentication(auth);
        securityContextHolderStrategy.setContext(context);

        securityContextRepository.saveContext(context, request, response);
    }
}
