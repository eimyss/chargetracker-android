package de.server.eimantas.expensesapp.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;


@Entity
public class Location {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "sreet")
    private String street;
    @ColumnInfo(name = "zip")
    private String zip;
    @ColumnInfo(name = "server_id")
    private int serverId;

    public int getServerBookingId() {
        return serverBookingId;
    }

    public void setServerBookingId(int serverBookingId) {
        this.serverBookingId = serverBookingId;
    }

    @ColumnInfo(name = "server_booking_id")
    private int serverBookingId;

    public String getName() {
        return name;
    }

    public Location(int id, String name, String street, String zip, int serverId) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.zip = zip;
        this.serverId = serverId;
    }

    public Location() {

    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", zip='" + zip + '\'' +
                ", serverId=" + serverId +
                ", serverBookingId=" + serverBookingId +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
