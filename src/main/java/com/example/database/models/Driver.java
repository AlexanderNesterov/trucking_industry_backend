package com.example.database.models;

import com.example.database.models.commons.DriverStatus;
import javax.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {

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
}
