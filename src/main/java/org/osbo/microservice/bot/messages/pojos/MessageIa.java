package org.osbo.microservice.bot.messages.pojos;

public class MessageIa {
    private String message;
    private String tipo;
    private Long idchat;
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
    public Long getIdchat() {
        return idchat;
    }
    public void setIdchat(Long idchat) {
        this.idchat = idchat;
    }
    
}
