package com.example.database.models;

import com.example.database.models.commons.DriverStatus;
import com.example.database.models.interfaces.Searchable;

import javax.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver implements Searchable {

    @Id
    @SequenceGenerator(name = "driver_seq", sequenceName = "drivers_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "driver_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "driver_license")
    private String driverLicense;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private DriverStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "search_string")
    private String searchString;

    public Driver() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    @Override
    public void combineSearchString() {
        StringBuilder sb = new StringBuilder();

        sb
                .append(user.getFirstName()).append(" ")
                .append(user.getLastName()).append(" ")
                .append(user.getEmail()).append(" ")
                .append(user.getPhone()).append(" ")
                .append(driverLicense);

        searchString = sb.toString().toLowerCase();
    }
}
