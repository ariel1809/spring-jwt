package com.example.springjwt.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@ToString
@Getter @Setter
@AllArgsConstructor
public class JwtResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
}
