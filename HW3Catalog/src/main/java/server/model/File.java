package server.model;


import common.FileDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


import java.sql.Timestamp;

@Entity(name = "File")
@Table(name = "File")


@NamedQueries({

        @NamedQuery(
                name = "deleteFileByName",
                query = "DELETE FROM File f WHERE f.name LIKE :name"
        ),

        @NamedQuery(
                name = "deleteFileByOwnerName",
                query = "DELETE FROM File f WHERE f.account.username LIKE :name"
        ),

        @NamedQuery(
                name = "findFileByName",
                query = "SELECT f FROM File f WHERE f.name LIKE :name",
                lockMode = LockModeType.OPTIMISTIC
        ),

        @NamedQuery(
                name = "getFiles",
                query = "SELECT f FROM File f",
                lockMode = LockModeType.OPTIMISTIC
        )
})

public class File implements Serializable, FileDTO {


    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @Version
    private int optlock;
    @ManyToOne
    @JoinColumn(name="account_id")
    public Account account;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "size", nullable = false)
    private long size;
    @Column(name = "createdAt", nullable = false)
    private Timestamp createdAt;
    @Column(name = "updatedAt", nullable = false)
    private Timestamp updatedAt;

    public File() {

    }

    /*public File(Account owner, int size) {
        this.account = account;
        this.id = id;
        Date date= new Date();
        long time = date.getTime();
        this.createdAt = new Timestamp(time);
        this.updatedAt = new Timestamp(time);
    }*/

    public File(Account userByUsername, String name, int length) {
        this.account = userByUsername;
        this.size = length;
        this.name = name;
        Date date= new Date();
        long time = date.getTime();
        this.createdAt = new Timestamp(time);
        this.updatedAt = new Timestamp(time);

    }

    @Override
    public Account getOwner() {
        return account;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasPrivateAccess() {
        return false;
    }

    @Override
    public boolean hasReadPermission() {
        return false;
    }

    @Override
    public boolean hasWritePermission() {
        return false;
    }

    @Override
    public long getDimension() {
        return size;
    }

    @Override
    public Date getCreatedAt() {
        return null;
    }

    @Override
    public Date getUpdatedAt() {
        return null;
    }
}
