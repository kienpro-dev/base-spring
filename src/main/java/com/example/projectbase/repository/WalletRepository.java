package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,String> {

    @Query(value = "SELECT w FROM Wallet w WHERE w.userOwn.id=?1 order by w.createdDate DESC ")
    List<Wallet> getAllByUserId(String id);
}
