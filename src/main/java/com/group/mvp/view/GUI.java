package com.group.mvp.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class GUI extends Application {
    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        // Create MemberView and add to a tab
        MemberView memberView = new MemberView();
        FilmView filmView = new FilmView();
        Tab memberTab = new Tab("Members", memberView.getView());
        memberTab.setClosable(false);
        Tab filmTab = new Tab("Films", filmView.getView());
        filmTab.setClosable(false);

        tabPane.getTabs().addAll(memberTab, filmTab);

        Scene scene = new Scene(tabPane, 800, 600);
        stage.setTitle("Film Production Management");
        stage.setScene(scene);
        stage.show();
    }
}
