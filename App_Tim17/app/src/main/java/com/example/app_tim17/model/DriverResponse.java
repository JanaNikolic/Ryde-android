package com.example.app_tim17.model;

public class DriverResponse extends UserResponse {
    public DriverResponse() {
        super();
    }

    public DriverResponse(Long id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
        super(id, name, surname, profilePicture, telephoneNumber, email, address);
    }
}
