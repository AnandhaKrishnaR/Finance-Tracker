package com.budgetai.repository;

import com.budgetai.model.Transaction;
import com.budgetai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTop5ByUserOrderByDateDesc(User user);

    @Query("select sum(t.amount) from Transaction t where t.user = :user and t.date between :start and :end")
    Double sumByUserAndDateBetween(@Param("user") User user,
                                   @Param("start") LocalDate start,
                                   @Param("end") LocalDate end);

    @Query("select t.category.name, sum(t.amount) from Transaction t where t.user = :user and t.date between :start and :end group by t.category.name order by 2 desc")
    List<Object[]> sumByCategoryForPeriod(@Param("user") User user,
                                          @Param("start") LocalDate start,
                                          @Param("end") LocalDate end);
}











