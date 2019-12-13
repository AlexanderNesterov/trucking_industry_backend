package com.example.database.models;

import com.example.database.models.interfaces.Searchable;

import javax.persistence.*;

@Entity
@Table(name = "managers")
public class Manager implements Searchable {

    @Id
    @SequenceGenerator(name = "manager_seq", sequenceName = "managers_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manager_seq")
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "search_string")
    private String searchString;

    public Manager() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                .append(user.getPhone()).append(" ")
                .append(user.getEmail());

        searchString = sb.toString().toLowerCase();
    }
}
