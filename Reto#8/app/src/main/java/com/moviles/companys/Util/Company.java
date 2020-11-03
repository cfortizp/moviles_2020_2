package com.moviles.companys.Util;

public class Company {
    private int id;
    private String name;
    private String url;
    private String email;
    private String phone;
    private String services;
    private String classification;
    private int idclassification;

    public Company(int id) {
        this.id = id;
        this.name = null;
        this.url = null;
        this.email = null;
        this.phone = null;
        this.services = null;
        this.classification = null;
        this.idclassification = -1;
    }
    public Company() {
        this.id = -1;
        this.name = null;
        this.url = null;
        this.email = null;
        this.phone = null;
        this.services = null;
        this.classification = null;
        this.idclassification = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public int getIdclassification() {
        return idclassification;
    }

    public void setIdclassification(int idclassification) {
        this.idclassification = idclassification;
    }
}
