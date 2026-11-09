package com.supertuanis.repository;

import com.supertuanis.domain.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoItemRepository
        extends JpaRepository<CarritoItem, Integer> {

}