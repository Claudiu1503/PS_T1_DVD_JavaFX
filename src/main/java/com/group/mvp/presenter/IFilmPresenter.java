package com.group.mvp.presenter;


import com.group.mvp.model.Film;
import javafx.scene.control.TableView;

public interface IFilmPresenter {
    void loadFilms();
    void loadFilms(String type);
    void loadFilmsByCategory(String category);
    void loadFilmsByYear(int year);
    void loadFilmsByActor(String actorName);
    void addFilm(Film film);
    void updateFilm(Film film);
    void deleteFilm(Film film);
    void exportToCsv(TableView<Film> filmTableView);
    void exportToDocx(TableView<Film> filmTableView);
}
