package com.fullstack.Backend.repositories.interfaces;

import com.fullstack.Backend.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    public VerificationToken findByToken(String VerificationToken);
}
