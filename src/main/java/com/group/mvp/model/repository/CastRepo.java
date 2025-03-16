package com.group.mvp.model.repository;

import com.group.mvp.model.Cast;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CastRepo {
    public CastRepo() {
    }
    protected List<Cast> getFilmCast(int filmId) throws SQLException {
        List<Cast> cast = new ArrayList<>();
        String query = "SELECT c.actor_id, c.role FROM Casts c " +
                "JOIN Members m ON c.actor_id = m.id WHERE c.film_id = ?";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, filmId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cast.add(new Cast(filmId,resultSet.getInt("actor_id"), resultSet.getString("role")));
                }
            }
        }
        return cast;
    }
    public List<Integer> getStarredFilms(int idActor) throws SQLException{
        List<Integer> filmIDs = new ArrayList<>();
        String query = "SELECT film_id FROM Casts WHERE actor_id = ?";

        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idActor);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    filmIDs.add(resultSet.getInt("film_id"));
                }
            }
        }
        return filmIDs;
    }

    protected void addCast(int filmId, List<Cast> cast) throws SQLException{
        String query = "INSERT INTO Casts (film_id, actor_id, role) VALUES (?, ?, ?)";
        try (Connection connection = Repository.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (Cast entry : cast) {
                statement.setInt(1, filmId);
                statement.setInt(2, entry.getIdActor());
                statement.setString(3, entry.getRole());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    public void deleteCast(int idFilm) throws SQLException{
        String query = "DELETE FROM Casts WHERE film_id = ?";
        try (Connection connection = Repository.getConnection()) {
            // Delete Member
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idFilm);
                statement.executeUpdate();
            }
        }
    }
}
