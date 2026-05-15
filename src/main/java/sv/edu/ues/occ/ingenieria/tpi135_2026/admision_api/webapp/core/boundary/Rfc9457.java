package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary;

import java.io.Serializable;

public class Rfc9457 implements Serializable{

    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;

    public Rfc9457(){}

    private Rfc9457(Builder builder){
        this.type = builder.type;
        this.title = builder.title;
        this.status = builder.status;
        this.detail = builder.detail;
        this.instance = builder.instance;
    }

    public static class Builder{
        
        private String type;
        private String title;
        private int status;
        private String detail;
        private String instance;

        public Builder(){}

        public Builder type(String type){this.type = type; return this;}
        public Builder title(String title){this.title = title; return this;}
        public Builder status(int status){this.status = status; return this;}
        public Builder detail(String detail){this.detail = detail; return this;}
        public Builder instance(String instance){this.instance = instance; return this;}

        public Rfc9457 build(){
            return new Rfc9457(this);
        }
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }
}
