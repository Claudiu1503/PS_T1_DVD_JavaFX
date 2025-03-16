package com.group.mvp.presenter;

import com.group.mvp.model.Member;
import com.group.mvp.model.repository.MemberRepo;
import com.group.mvp.view.MemberView;

import java.sql.SQLException;
import java.util.List;

public class MemberPresenter {
    private MemberView view;  // The View interface reference
    private MemberRepo memberRepo;

    public MemberPresenter(MemberView view) {
        this.view = view;
        this.memberRepo = new MemberRepo(); // Initialize the repository
        loadMembers();
    }

    public void loadMembers() {
        try {
            List<Member> members = memberRepo.getAllMembers(null);
            view.setItemList(members);  // update the UI via View interface
        } catch (SQLException e) {
            view.showError("Failed to load members: " + e.getMessage());
        }
    }

    // Load members based on type
    public void loadMembers(String type) {
        try {
            List<Member> members = memberRepo.getAllMembers(type);
            view.setItemList(members);
        } catch (SQLException e) {
            view.showError("Failed to load members: " + e.getMessage());
        }
    }

    public void addMember(Member member) {
        try {
            memberRepo.addMember(member);
            loadMembers(); // Refresh the list
        } catch (SQLException e) {
            view.showError("Failed to add member: " + e.getMessage());
        }
    }

    public void updateMember(Member member) {
        try {
            memberRepo.updateMember(member);
            loadMembers();
        } catch (SQLException e) {
            view.showError("Failed to update member: " + e.getMessage());
        }
    }

    public void deleteMember(Member member) {
        try {
            memberRepo.deleteMember(member.getId());
            loadMembers();
        } catch (SQLException e) {
            view.showError("Failed to delete member: " + e.getMessage());
        }
    }
}
