package de.server.eimantas.expensesapp.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;


@Entity
public class Booking {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "start")
    private LocalDateTime startdate;
    @ColumnInfo(name = "end")
    private LocalDateTime endDate;
    @ColumnInfo(name = "project_id")
    private int project_id;


    public LocalDateTime getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDateTime startdate) {
        this.startdate = startdate;
    }


    public String getName() {
        return name;
    }

    public Booking(int id, String name, LocalDateTime startdate, LocalDateTime endDate, int project_id) {
        this.id = id;
        this.name = name;
        this.startdate = startdate;
        this.endDate = endDate;
        this.project_id = project_id;
    }

    public Booking() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startdate=" + startdate +
                ", endDate=" + endDate +
                '}';
    }
}
