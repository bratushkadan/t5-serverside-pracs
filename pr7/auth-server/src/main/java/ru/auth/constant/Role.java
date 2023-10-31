package ru.auth.constant;

public enum Role {
    USER,
    SELLER,
    ADMINISTRATOR;

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.toString().equals(role)) {
                return r;
            }
        }
        return null;
    }
    
}

