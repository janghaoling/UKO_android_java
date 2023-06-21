package com.mobile.uko.model;

public class UserDetails {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gender;
    private String employment;
    private String dateOfBirth;
    private String education;
    private String countryOrigin;

    public UserDetails(String firstName, String lastName, String phone, String gender, String employment, String dateOfBirth, String education, String countryOrigin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phone;
        this.gender = gender;
        this.employment = employment;
        this.dateOfBirth = dateOfBirth;
        this.education = education;
        this.countryOrigin = countryOrigin;
    }
}
