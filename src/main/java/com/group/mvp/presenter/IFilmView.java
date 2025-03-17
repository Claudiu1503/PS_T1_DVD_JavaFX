package com.group.mvp.presenter;

import com.group.mvp.model.Film;
import javafx.scene.layout.VBox;

import java.util.List;

public interface IFilmView {
    void setItemList(List<Film> films);
    void showRolesTable(Film film);
    void showImagesPopup(Film film);
    void showError(String message);
    void showConfirmation(String message);
    Film getSelectedItem();
    VBox getView();
}
