package com.yusuf.yusufmart.dto;

import com.yusuf.yusufmart.model.Role;
import com.yusuf.yusufmart.model.User;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Sanitized user transfer object.
 * As mandated by Section 13 Rule 4: UserResponseDTO must NOT contain passwordHash.
 */
public class UserResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String email;
    private Role role;
    private Timestamp createdAt;

    public UserResponseDTO() {
    }

    public UserResponseDTO(int id, String name, String email, Role role, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static UserResponseDTO fromUser(User user) {
        if (user == null) return null;
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
