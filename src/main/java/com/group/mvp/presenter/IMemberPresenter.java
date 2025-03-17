package com.group.mvp.presenter;

import com.group.mvp.model.Member;

public interface IMemberPresenter {
    void loadMembers();
    void loadMembers(String type);
    void addMember(Member member);
    void updateMember(Member member);
    void deleteMember(Member member);
}