package com.hantsylab.example.ee7.blog.service;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author hantsy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PasswordForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

}
