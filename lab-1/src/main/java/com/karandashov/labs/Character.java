package com.karandashov.labs;

import java.time.OffsetDateTime;

public class Character {
    private Long id;
    private String name;
    private OffsetDateTime created;

    public Character(Long id, String name, OffsetDateTime created) {
        this.id = id;
        this.name = name;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "id=" + id + ", name='" + name + "', created=" + created;
    }
}