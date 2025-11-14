package com.parking.slot.repository;

import com.parking.slot.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByStatus(String status);
    List<Slot> findByLocation(String location);
}