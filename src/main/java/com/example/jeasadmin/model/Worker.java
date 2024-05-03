package com.example.jeasadmin.model;

public class Worker extends User {
    private String skills;

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", personality='" + personality + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                ", birthday='" + birthday + '\'' +
                ", skills='" + skills + '\'' +
                '}';
    }
}
