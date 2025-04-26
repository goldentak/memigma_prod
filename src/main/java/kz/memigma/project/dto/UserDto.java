package kz.memigma.project.dto;


import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }
}