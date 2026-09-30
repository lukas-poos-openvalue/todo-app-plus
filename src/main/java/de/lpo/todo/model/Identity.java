package de.lpo.todo.model;

/** An identity of a user. */
public record Identity(
    String userId, String userName, String email, String displayName, Role role) {}
