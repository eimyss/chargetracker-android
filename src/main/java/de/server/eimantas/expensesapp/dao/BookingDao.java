package de.server.eimantas.expensesapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import de.server.eimantas.expensesapp.entities.Booking;

@Dao
public interface BookingDao {
    @Query("SELECT * FROM Booking")
    List<Booking> getAll();

    @Query("SELECT * FROM Booking WHERE id IN (:expenseId)")
    List<Booking> loadAllByIds(int[] expenseId);

    @Insert
    void insertAll(Booking... bookings);

    @Insert
    long insert(Booking booking);

    @Delete
    void delete(Booking booking);
}
