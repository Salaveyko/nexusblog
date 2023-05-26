package com.nexusblog.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "is_enabled")
    @ColumnDefault("true")
    private boolean isEnabled;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Post> posts;

    public User(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.isEnabled = true;
    }

    public void addRole(Role role){
        addRole(role, false);
    }
    public void addRole(Role role, boolean isBacksideSet){
        if(!isBacksideSet){
            role.addUser(this, true);
        }
        roles.add(role);
    }

    public void addPost(Post post){
        addPost(post, false);
    }
    public void addPost(Post post, boolean isBacksideSet){
        if(!isBacksideSet){
            post.setUser(this, true);
        }
        posts.add(post);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
