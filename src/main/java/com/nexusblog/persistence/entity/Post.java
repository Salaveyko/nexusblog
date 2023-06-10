package com.nexusblog.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Comment> comments;

    public Post(String title, String content, Date created, Date updated) {
        this(0L, title, content, created, updated, new User(), new HashSet<>());
    }
}
