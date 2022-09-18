package com.projectboated.backend.account.account.entity;

public enum Role {
    USER("ROLE_USER");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
