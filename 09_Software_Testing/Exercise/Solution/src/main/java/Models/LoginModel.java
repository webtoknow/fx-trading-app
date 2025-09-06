package Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginModel {

    private String username;
    private String password;
    private String token;

    public LoginModel(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
