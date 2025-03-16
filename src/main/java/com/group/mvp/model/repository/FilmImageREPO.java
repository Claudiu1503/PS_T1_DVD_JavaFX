package com.group.mvp.model.repository;

import com.group.mvp.model.FilmImage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilmImageREPO {
    public FilmImageREPO() {
    }

    protected List<FilmImage> getFilmImages(int filmId) throws SQLException {
        List<FilmImage> filmImages = new ArrayList<>();
        String query = "SELECT * FROM filmImages WHERE film_id = ?";

        try (Connection connection = DatabaseREPO.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, filmId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    filmImages.add(new FilmImage(resultSet.getInt("id"), filmId, resultSet.getString("url")));
                }
            }
        }
        return filmImages;
    }

    protected void addFilmImages(int filmId, List<FilmImage> images) throws SQLException{
        String query = "INSERT INTO Filmimages (film_id, url) VALUES (?, ?)";
        try(Connection connection = DatabaseREPO.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            for(FilmImage image : images){
                statement.setInt(1, filmId);
                statement.setString(2, image.getUrl());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    public void deleteImages(int idFilm) throws SQLException{
        String query = "DELETE FROM Filmimages WHERE film_id = ?";
        try (Connection connection = DatabaseREPO.getConnection()) {
            // Delete Member
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idFilm);
                statement.executeUpdate();
            }
        }
    }
}
