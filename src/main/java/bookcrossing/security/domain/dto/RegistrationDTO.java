package bookcrossing.security.domain.dto;

import lombok.Data;

@Data
public class RegistrationDTO {
    private String firstName;
    private String secondName;
    private Integer age;
    private Integer phone;
    private String email;
    private String address;
    private String userName;
    private String personPassword;
}
