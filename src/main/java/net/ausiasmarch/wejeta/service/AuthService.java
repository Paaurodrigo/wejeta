package net.ausiasmarch.wejeta.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import net.ausiasmarch.wejeta.bean.LogindataBean;
import net.ausiasmarch.wejeta.entity.UsuarioEntity;
import net.ausiasmarch.wejeta.exception.UnauthorizedAccessException;
import net.ausiasmarch.wejeta.repository.UsuarioRepository;

@Service
public class AuthService {

    @Autowired
    JWTService JWTHelper;

    @Autowired
    UsuarioRepository oUsuarioRepository;

    @Autowired
    HttpServletRequest oHttpServletRequest;

    public boolean checkLogin(LogindataBean oLogindataBean) {
        if (oUsuarioRepository.findByEmailAndPasswordAndPassword2(oLogindataBean.getEmail(), oLogindataBean.getPassword(), oLogindataBean.getPassword2())
                .isPresent()) {
            return true;
        } else {
            return false;
        }
    }
    
//OJO PREGUNTA EXAMEN TE DEVUELVE EMAIL Y NOMBRE
    private Map<String, String> getClaims(String email) {
        Map<String, String> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("nombre", oUsuarioRepository.findByEmail(email).get().getNombre());
        return claims;
    };

    public String getToken(String email) {
        return JWTHelper.generateToken(getClaims(email));
    }

    public UsuarioEntity getUsuarioFromToken() {
        if (oHttpServletRequest.getAttribute("email") == null) {
            throw new UnauthorizedAccessException("No hay usuario en la sesión");
        } else {
            String email = oHttpServletRequest.getAttribute("email").toString();
            return oUsuarioRepository.findByEmail(email).get();
        }                
    }

    public boolean isSessionActive() {
        return oHttpServletRequest.getAttribute("email") != null;
    }

    public boolean isAdmin() {
        return this.getUsuarioFromToken().getTipousuario().getId() == 1L;
    }

    public boolean isContable() {
        return this.getUsuarioFromToken().getTipousuario().getId() == 2L;
    }

    public boolean isAuditor() {
        return this.getUsuarioFromToken().getTipousuario().getId() == 3L;
    }

    public boolean isAdminOrContable() {
        return this.isAdmin() || this.isContable();
    }

    public boolean isContableWithItsOwnData(Long id) {
        UsuarioEntity oUsuarioEntity = this.getUsuarioFromToken();
        return this.isContable() && oUsuarioEntity.getId() == id;
    }

    public boolean isAuditorWithItsOwnData(Long id) {
        UsuarioEntity oUsuarioEntity = this.getUsuarioFromToken();
        return this.isAuditor() && oUsuarioEntity.getId() == id;
    }

}
