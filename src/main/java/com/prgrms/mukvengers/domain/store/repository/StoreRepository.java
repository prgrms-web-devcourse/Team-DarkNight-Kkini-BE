package com.prgrms.mukvengers.domain.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.store.model.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

	Optional<Store> findByPlaceId(@Param("placeId") String placeId);

}
