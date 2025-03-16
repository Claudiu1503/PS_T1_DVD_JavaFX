package com.group.mvp.presenter;


import com.group.mvp.model.Film;
import com.group.mvp.model.repository.FilmRepo;
import com.group.mvp.view.FilmView;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class FilmPresenter {
    private FilmView view;
    private FilmRepo filmRepo;

    public FilmPresenter(FilmView view) {
        this.view = view;
        this.filmRepo = new FilmRepo();
        loadFilms();
    }

    public void loadFilms() {
        try {
            List<Film> Films = filmRepo.getAllFilms(null);
            view.setItemList(Films);  // Update the UI via View interface
        } catch (SQLException e) {
            view.showError("Failed to load Films: " + e.getMessage());
        }
    }

    // Load Films based on type
    public void loadFilms(String type) {
        try {
            List<Film> Films = filmRepo.getAllFilms(type);
            view.setItemList(Films);  // Update the UI via View interface
        } catch (SQLException e) {
            view.showError("Failed to load Films: " + e.getMessage());
        }
    }

    public void loadFilmsByCategory(String category) {
        try {
            List<Film> Films = filmRepo.getFilmsByCategory(category);
            view.setItemList(Films);  // Update the UI via View interface
        } catch (SQLException e) {
            view.showError("Failed to load Films: " + e.getMessage());
        }
    }

    public void loadFilmsByYear(int year) {
        try {
            List<Film> Films = filmRepo.getFilmsByYear(year);
            view.setItemList(Films);
        } catch (SQLException e) {
            view.showError("Failed to load Films: " + e.getMessage());
        }
    }

    public void loadFilmsByActor(String actorName) {
        try {
            List<Film> Films = filmRepo.getFilmsByActor(actorName);
            view.setItemList(Films);
        } catch (SQLException e) {
            view.showError("Failed to load Films: " + e.getMessage());
        }
    }

    public void addFilm(Film Film) {
        try {
            filmRepo.addFilm(Film);
            loadFilms();
        } catch (SQLException e) {
            view.showError("Failed to add Film: " + e.getMessage());
        }
    }

    public void updateFilm(Film Film) {
        try {
            filmRepo.updateFilm(Film);
            loadFilms();
        } catch (SQLException e) {
            view.showError("Failed to update Film: " + e.getMessage());
        }
    }

    public void deleteFilm(Film Film) {
        try {
            filmRepo.deleteFilm(Film.getId());
            loadFilms();
        } catch (SQLException e) {
            view.showError("Failed to delete Film: " + e.getMessage());
        }
    }

    public void exportToCsv(TableView<Film> filmTableView){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // write headers
                writer.write("ID,Title,Year,Type,Category,DirectorID,WriterID,ProducerID\n");

                // write film data
                for (Film film : filmTableView.getItems()) {
                    writer.write(String.format("%d,%s,%d,%s,%s,%d,%d,%d\n",
                            film.getId(),
                            film.getTitle(),
                            film.getYear(),
                            film.getType(),
                            film.getCategory(),
                            film.getDirectorId(),
                            film.getWriterId(),
                            film.getProducerId()));
                }

                view.showConfirmation("CSV file saved successfully!");
            } catch (IOException e) {
                view.showError("Error saving CSV file.");
            }
        }
    }

    public void exportToDocx(TableView<Film> filmTableView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Document", "*.docx"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (FileOutputStream out = new FileOutputStream(file)) {
                XWPFDocument document = new XWPFDocument();

                // create the title paragraph
                XWPFParagraph titleParagraph = document.createParagraph();
                XWPFRun titleRun = titleParagraph.createRun();
                titleRun.setText("Film List");
                titleRun.setBold(true);
                titleRun.setFontSize(16);
                titleParagraph.setAlignment(ParagraphAlignment.CENTER);

                // create a table
                XWPFTable table = document.createTable();

                // Create the header row
                XWPFTableRow headerRow = table.getRow(0);
                headerRow.getCell(0).setText("ID");
                headerRow.addNewTableCell().setText("Title");
                headerRow.addNewTableCell().setText("Year");
                headerRow.addNewTableCell().setText("Type");
                headerRow.addNewTableCell().setText("Category");
                headerRow.addNewTableCell().setText("Director ID");
                headerRow.addNewTableCell().setText("Writer ID");
                headerRow.addNewTableCell().setText("Producer ID");

                // add rows for each film in the table
                for (Film film : filmTableView.getItems()) {
                    XWPFTableRow row = table.createRow();
                    row.getCell(0).setText(String.valueOf(film.getId()));
                    row.getCell(1).setText(film.getTitle());
                    row.getCell(2).setText(String.valueOf(film.getYear()));
                    row.getCell(3).setText(film.getType());
                    row.getCell(4).setText(film.getCategory());
                    row.getCell(5).setText(String.valueOf(film.getDirectorId()));
                    row.getCell(6).setText(String.valueOf(film.getWriterId()));
                    row.getCell(7).setText(String.valueOf(film.getProducerId()));
                }

                document.write(out);
                view.showConfirmation("DOCX file saved successfully!");

            } catch (IOException e) {
                view.showError("Error saving DOCX file.");
            }
        }
    }
}
