package com.group.mvp.model.repository;

import com.group.mvp.model.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilmREPO {
    CastREPO castRepo = new CastREPO();
    FilmImageREPO filmImageRepo = new FilmImageREPO();
    MemberREPO memberRepo = new MemberREPO();

    public List<Film> getAllFilms(String type) throws SQLException {
        List<Film> films = new ArrayList<>();
        String query = "SELECT f.*, d.name AS director_name, w.name AS writer_name, p.name AS producer_name " +
                "FROM Films f " +
                "JOIN Members d ON f.director_id = d.id " +
                "JOIN Members w ON f.writer_id = w.id " +
                "JOIN Members p ON f.producer_id = p.id";

        if (type != null && !type.isEmpty()) {
            query += " WHERE f.type = ?";
        } else {
            query += " ORDER BY f.type";
        }

        try (Connection connection = DatabaseREPO.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if (type != null && !type.isEmpty()) {
                statement.setString(1, type);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Film film = new Film();
                    film.setId(resultSet.getInt("id"));
                    film.setTitle(resultSet.getString("title"));
                    film.setYear(resultSet.getInt("year"));
                    film.setType(resultSet.getString("type"));
                    film.setCategory(resultSet.getString("category"));
                    film.setDirectorId(resultSet.getInt("director_id"));
                    film.setWriterId(resultSet.getInt("writer_id"));
                    film.setProducerId(resultSet.getInt("producer_id"));
                    film.setDirectorName(resultSet.getString("director_name"));
                    film.setWriterName(resultSet.getString("writer_name"));
                    film.setProducerName(resultSet.getString("producer_name"));

                    // Fetch Cast
                    film.setCast(castRepo.getFilmCast(film.getId()));

                    // Fetch Images
                    film.setImages(filmImageRepo.getFilmImages(film.getId()));

                    films.add(film);
                }
            }
        }
        return films;
    }


    public List<Film> getFilmsByCategory(String category) throws SQLException{
        List<Film> films = new ArrayList<>();
        String query = "SELECT * FROM Films WHERE category = ?";

        try (Connection connection = DatabaseREPO.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Film film = new Film();
                    film.setId(resultSet.getInt("id"));
                    film.setTitle(resultSet.getString("title"));
                    film.setYear(resultSet.getInt("year"));
                    film.setType(resultSet.getString("type"));
                    film.setCategory(resultSet.getString("category"));
                    film.setDirectorId(resultSet.getInt("director_id"));
                    film.setWriterId(resultSet.getInt("writer_id"));
                    film.setProducerId(resultSet.getInt("producer_id"));

                    // Fetch Cast
                    film.setCast(castRepo.getFilmCast(film.getId()));

                    // Fetch Images
                    film.setImages(filmImageRepo.getFilmImages(film.getId()));

                    films.add(film);
                }
            }
        }
        return films;
    }

    public List<Film> getFilmsByYear(int year) throws SQLException{
        List<Film> films = new ArrayList<>();
        String query = "SELECT * FROM Films WHERE year = ?";

        try (Connection connection = DatabaseREPO.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, year);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Film film = new Film();
                    film.setId(resultSet.getInt("id"));
                    film.setTitle(resultSet.getString("title"));
                    film.setYear(resultSet.getInt("year"));
                    film.setType(resultSet.getString("type"));
                    film.setCategory(resultSet.getString("category"));
                    film.setDirectorId(resultSet.getInt("director_id"));
                    film.setWriterId(resultSet.getInt("writer_id"));
                    film.setProducerId(resultSet.getInt("producer_id"));

                    // Fetch Cast
                    film.setCast(castRepo.getFilmCast(film.getId()));

                    // Fetch Images
                    film.setImages(filmImageRepo.getFilmImages(film.getId()));

                    films.add(film);
                }
            }
        }
        return films;
    }

    // NOTE: *by actor Name
    public List<Film> getFilmsByActor(String actorName) throws SQLException{
        int idActor = memberRepo.getMemberIDByName(actorName);
        List<Integer> filmIDs = castRepo.getStarredFilms(idActor);

        String idsString = filmIDs.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        List<Film> films = new ArrayList<>();
        String query = "SELECT * FROM films WHERE id IN (" + idsString +")";

        try (Connection connection = DatabaseREPO.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Film film = new Film();
                    film.setId(resultSet.getInt("id"));
                    film.setTitle(resultSet.getString("title"));
                    film.setYear(resultSet.getInt("year"));
                    film.setType(resultSet.getString("type"));
                    film.setCategory(resultSet.getString("category"));
                    film.setDirectorId(resultSet.getInt("director_id"));
                    film.setWriterId(resultSet.getInt("writer_id"));
                    film.setProducerId(resultSet.getInt("producer_id"));

                    // Fetch Cast
                    film.setCast(castRepo.getFilmCast(film.getId()));

                    // Fetch Images
                    film.setImages(filmImageRepo.getFilmImages(film.getId()));

                    films.add(film);
                }
            }
        }
        return films;
    }
    public void addFilm(Film film) throws SQLException {
        String query = "INSERT INTO Films (title, year, type, category, director_id, writer_id, producer_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseREPO.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getYear());
            statement.setString(3, film.getType());
            statement.setString(4, film.getCategory());
            statement.setInt(5, film.getDirectorId());
            statement.setInt(6, film.getWriterId());
            statement.setInt(7, film.getProducerId());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int filmId = generatedKeys.getInt(1);
                    castRepo.addCast(filmId, film.getCast());
                    filmImageRepo.addFilmImages(filmId, film.getImages());
                }
            }
        }
    }

    public void updateFilm(Film film) throws SQLException{
        String query = "UPDATE Films SET title = ?, year = ?, type = ?, category = ?," +
                " director_id = ?, writer_id = ?, producer_id = ? WHERE id = ?";

        try (Connection connection = DatabaseREPO.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getYear());
            statement.setString(3, film.getType());
            statement.setString(4, film.getCategory());
            statement.setInt(5, film.getDirectorId());
            statement.setInt(6, film.getWriterId());
            statement.setInt(7, film.getProducerId());
            statement.setInt(8, film.getId());

            statement.executeUpdate();
        }

        // update the cast and images by first deleting the old entries and replacing them with the new ones
        castRepo.deleteCast(film.getId());
        castRepo.addCast(film.getId(), film.getCast());

        filmImageRepo.deleteImages(film.getId());
        filmImageRepo.addFilmImages(film.getId(), film.getImages());
    }

    public void deleteFilm(int id) throws SQLException {
        String query = "DELETE FROM Films WHERE id = ?";

        try (Connection connection = DatabaseREPO.getConnection()) {
            // Delete Member
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        }
    }
}
