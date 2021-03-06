/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hantsylab.example.ee7.blog.domain.model;

import com.hantsylab.example.ee7.blog.domain.support.AbstractEntity;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author hantsy
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(name = "uni_username", columnNames = {"username"})
})
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    @NotBlank
    private String username;

    @Column(name = "password")
    @NotBlank
    private String password;
    
    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "is_active")
    private boolean active = false;

//    @ElementCollection
//    @JoinTable(
//        name = "users_roles",
//        joinColumns = {
//            @JoinColumn(name = "user_id")
//        }
//    )
//    private List<Role> roles = new ArrayList<>();
    @Column(name = "role_name")
    private Role role = Role.USER;
    
    @Column(name="created_at")
    private OffsetDateTime createdAt;
    
    @PrePersist
    public void beforePersist(){
        this.createdAt=OffsetDateTime.now();
    }

}
