package com.group;

import com.group.mvp.view.GUI;

public class AppLauncher {
    public static void main(String[] args) {
        com.group.mvp.view.GUI.launch(GUI.class, args);
    }
}
