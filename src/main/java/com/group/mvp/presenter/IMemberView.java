package com.group.mvp.presenter;

import com.group.mvp.model.Member;
import javafx.scene.layout.VBox;

import java.util.List;

public interface IMemberView {
    void setItemList(List<Member> members);
    void showError(String message);
    Member getSelectedItem();
    VBox getView();
}
