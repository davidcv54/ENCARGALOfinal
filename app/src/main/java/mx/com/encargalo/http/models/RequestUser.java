package mx.com.encargalo.http.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestUser {

    @SerializedName("sp_usuCorreo")
    @Expose
    private String sp_usuCorreo;

    @SerializedName("sp_usuImagen")
    @Expose
    private String sp_usuImagen;

    @SerializedName("sp_idRolUusuario")
    @Expose
    private String nomsp_idRolUusuariobre;

    @SerializedName("sp_idDocumentoPersona")
    @Expose
    private String sp_idDocumentoPersona;

    @SerializedName("sp_perNombres")
    @Expose
    private String sp_perNombres;

    @SerializedName("sp_perApellidos")
    @Expose
    private String sp_perApellidos;

    @SerializedName("sp_perTipo")
    @Expose
    private String sp_perTipo;

    @SerializedName("sp_perNumeroCelular")
    @Expose
    private String sp_perNumeroCelular;


    public String getSp_usuCorreo() {
        return sp_usuCorreo;
    }

    public void setSp_usuCorreo(String sp_usuCorreo) {
        this.sp_usuCorreo = sp_usuCorreo;
    }

    public String getSp_usuImagen() {
        return sp_usuImagen;
    }

    public void setSp_usuImagen(String sp_usuImagen) {
        this.sp_usuImagen = sp_usuImagen;
    }

    public String getNomsp_idRolUusuariobre() {
        return nomsp_idRolUusuariobre;
    }

    public void setNomsp_idRolUusuariobre(String nomsp_idRolUusuariobre) {
        this.nomsp_idRolUusuariobre = nomsp_idRolUusuariobre;
    }

    public String getSp_idDocumentoPersona() {
        return sp_idDocumentoPersona;
    }

    public void setSp_idDocumentoPersona(String sp_idDocumentoPersona) {
        this.sp_idDocumentoPersona = sp_idDocumentoPersona;
    }

    public String getSp_perNombres() {
        return sp_perNombres;
    }

    public void setSp_perNombres(String sp_perNombres) {
        this.sp_perNombres = sp_perNombres;
    }

    public String getSp_perApellidos() {
        return sp_perApellidos;
    }

    public void setSp_perApellidos(String sp_perApellidos) {
        this.sp_perApellidos = sp_perApellidos;
    }

    public String getSp_perTipo() {
        return sp_perTipo;
    }

    public void setSp_perTipo(String sp_perTipo) {
        this.sp_perTipo = sp_perTipo;
    }

    public String getSp_perNumeroCelular() {
        return sp_perNumeroCelular;
    }

    public void setSp_perNumeroCelular(String sp_perNumeroCelular) {
        this.sp_perNumeroCelular = sp_perNumeroCelular;
    }
}
