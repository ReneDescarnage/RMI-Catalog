package server.model;

import common.UserDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity(name = "Account")
@NamedQueries({

        @NamedQuery(
                name = "deleteAccountByName",
                query = "DELETE FROM Account act WHERE act.username LIKE :name"
        ),

        @NamedQuery(
                name = "findAccountByName",
                query = "SELECT act FROM Account act WHERE act.username LIKE :name",
                lockMode = LockModeType.OPTIMISTIC
        )
})



public class Account implements Serializable, UserDTO {




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Version
    private int optlock;


    @Column(name = "username",nullable = false)
    private String username;

    @Column(name = "sessionId")
    private long sessionId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "createdat",nullable = false)
    private Date createdAt;

    @Column(name = "updatedat",nullable = false)
    private Date updatedAt;

    public Account(){

    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @PrePersist
    private void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }


    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void updateSessionId(long id) {
        this.sessionId = id;
    }

    public long getSessionId() {
        return sessionId;
    }
}
