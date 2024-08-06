package com.example.springjwt.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Getter @Setter
@Document(collection = "log_users")
public class LogUser {
    String id;
    String username;
    String password;
    String token;
}
