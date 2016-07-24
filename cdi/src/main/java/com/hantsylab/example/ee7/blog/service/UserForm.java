package com.hantsylab.example.ee7.blog.service;

import com.hantsylab.example.ee7.blog.domain.model.Role;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author hantsy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserForm implements Serializable{

    private static final long serialVersionUID = 1L;

    private String firstName;

    private String lastName;

    @NotBlank
    @NonNull
    private String username;

    @NotBlank
    private String password;

    private boolean active;

    private List<Role> roles = new ArrayList<>();

}
