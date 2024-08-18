package com.example.projectbase.service;

import com.example.projectbase.domain.entity.Wallet;

import java.util.List;
import java.util.Optional;

public interface WalletService {
    Optional<Wallet> saveOrUpdate(Wallet wallet);

    List<Wallet> getWalletByUserId(String id);
}
