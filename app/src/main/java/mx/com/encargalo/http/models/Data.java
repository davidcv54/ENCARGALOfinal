package mx.com.encargalo.http.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("idUsuario")
    @Expose
    private String idUsuario;
    @SerializedName("idRolUsuario")
    @Expose
    private String idRolUsuario;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdRolUsuario() {
        return idRolUsuario;
    }

    public void setIdRolUsuario(String idRolUsuario) {
        this.idRolUsuario = idRolUsuario;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
