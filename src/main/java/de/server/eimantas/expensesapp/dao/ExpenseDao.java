package de.server.eimantas.expensesapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import de.server.eimantas.expensesapp.entities.Expense;

@Dao
public interface ExpenseDao {
    @Query("SELECT * FROM expense")
    List<Expense> getAll();

    @Query("SELECT * FROM expense WHERE id IN (:expenseId)")
    List<Expense> loadAllByIds(int[] expenseId);

    @Insert
    void insertAll(Expense... expenses);
    @Delete
    void delete(Expense expense);
}
