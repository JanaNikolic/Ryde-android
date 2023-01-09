package com.example.app_tim17.model.response;

public class PassengerResponse extends UserResponse {
    public PassengerResponse() {
        super();
    }

    public PassengerResponse(Long id, String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
        super(id, name, surname, profilePicture, telephoneNumber, email, address);
    }
}
