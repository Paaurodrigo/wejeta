package net.ausiasmarch.wejeta.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogindataBean {
    private String email;
    private String password;
    private String password2;// se añade 2 contraseñas
}
