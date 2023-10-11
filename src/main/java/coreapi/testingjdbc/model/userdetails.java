package coreapi.testingjdbc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class userdetails {
    private String id;
    @NotBlank@NotEmpty@NotNull(message = "Username is mandatory")
    private String name;
    private char gender;
    @NotBlank@NotEmpty@NotNull(message = "Mobile is mandatory")
    private String mobile;
    @NotBlank@NotEmpty@NotNull(message = "Email is mandatory")
    private String email;
    @NotBlank@NotEmpty@NotNull(message = "Password is mandatory")
    private String password;
private String active;
private String createddate;
private String modifieddate;


}
