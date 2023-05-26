package com.nexusblog.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(String name){
        this.name = name;
    }

    public void addUser(User user){
        addUser(user, false);
    }
    public void addUser(User user, boolean isBacksideSet){
        if(!isBacksideSet){
            user.addRole(this, true);
        }
        users.add(user);
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
