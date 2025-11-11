package com.parking.qr.repository;

import com.parking.qr.model.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface QRRepository extends JpaRepository<QRCode, Long> {
    Optional<QRCode> findByCode(String code);
    Optional<QRCode> findByBookingId(Long bookingId);
}