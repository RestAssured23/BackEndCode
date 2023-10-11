package coreapi.testingjdbc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class userupdateBO {
    private String name;
    private char gender;
    private String mobile;
    private String email;
    private String password;
}
