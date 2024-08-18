package com.example.projectbase.service.impl;

import com.example.projectbase.domain.entity.Wallet;
import com.example.projectbase.repository.WalletRepository;
import com.example.projectbase.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    @Override
    public Optional<Wallet> saveOrUpdate(Wallet wallet) {
        Wallet walletOld = walletRepository.save(wallet);
        return Optional.of(walletOld);
    }
}
