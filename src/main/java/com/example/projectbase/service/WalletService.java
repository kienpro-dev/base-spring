package com.example.projectbase.service;

import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.entity.Wallet;

import java.util.Optional;

public interface WalletService {
    Optional<Wallet> saveOrUpdate(Wallet wallet);
}
