package com.nexusblog.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content", columnDefinition = "text")
    private String content;
    @Column(name = "created_at")
    private Date created;
    @Column(name = "updated_at")
    private Date updated;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    public Post(String title, String content, Date created, Date updated, User user) {
        this.title = title;
        this.content = content;
        this.created = created;
        this.updated = updated;
        this.user = user;
    }

    public void setUser(User user){
        setUser(user, false);
    }
    public void setUser(User user, boolean isBacksideSet){
        if(!isBacksideSet){
            user.addPost(this, true);
        }
        this.user = user;
    }
}
