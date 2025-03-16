package com.group.mvp.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
public class Cast {
    private SimpleIntegerProperty idFilm;
    private SimpleIntegerProperty idActor;
    private SimpleStringProperty role;

    public Cast() {
        this.idFilm = new SimpleIntegerProperty(1);
        this.idActor = new SimpleIntegerProperty(1);
        this.role = new SimpleStringProperty("UNDEFINED");
    }
    public Cast(Integer idFilm, Integer idActor, String role) {
        this.idFilm = new SimpleIntegerProperty(idFilm);
        this.idActor = new SimpleIntegerProperty(idActor);
        this.role = new SimpleStringProperty(role);
    }

    // getters / setters
    public int getIdActor() {
        return idActor.get();
    }

    public void setIdActor(int idActor) {
        this.idActor.set(idActor);
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }
}
