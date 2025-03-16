package com.group.mvp.presenter;
import java.util.List;

/** View Interface - implemented by MemberView */
public interface IView<T> {

        void setItemList(List<T> members);
        void showError(String message);
        T getSelectedItem();
}
