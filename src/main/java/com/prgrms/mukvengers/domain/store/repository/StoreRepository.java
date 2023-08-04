package com.prgrms.mukvengers.domain.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.store.model.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

	Optional<Store> findByPlaceId(@Param("placeId") String placeId);

	@Query(value = "SELECT * FROM store s ORDER BY RAND() LIMIT :count", nativeQuery = true)
	List<Store> findRandom(@Param("count") int count);
}
