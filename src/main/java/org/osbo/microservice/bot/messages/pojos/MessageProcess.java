package org.osbo.microservice.bot.messages.pojos;

public class MessageProcess {
    private String message;
    private String tipo;
    private String username;
    private Long idchat;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Long getIdchat() {
        return idchat;
    }
    public void setIdchat(Long idchat) {
        this.idchat = idchat;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
