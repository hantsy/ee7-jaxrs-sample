package com.hantsylab.example.ee7.blog.api.users;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author hantsy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credentials implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

}
