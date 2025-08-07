package com.TextHub.TextHub.Entity;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String login;
    private String email;
    private String password;
    
    public static UserDTO fromUser(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());
        return dto;
    }
}