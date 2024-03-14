package org.osbo.microservice.bot.messages.pojos;

public class OllamaRequest implements java.io.Serializable {
    private String model;
    private String prompt;
    private boolean stream = false;
    private String system;
    private long[] context;

    public long[] getContext() {
        return context;
    }

    public void setContext(long[] context) {
        this.context = context;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public boolean getStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
