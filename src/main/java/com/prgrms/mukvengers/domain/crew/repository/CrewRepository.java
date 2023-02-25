package com.prgrms.mukvengers.domain.crew.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.crew.model.Crew;

public interface CrewRepository extends JpaRepository<Crew, Long> {

	@Query("""
		SELECT c
		FROM Crew c
		JOIN FETCH  c.store s
		WHERE s.mapStoreId = :mapStoreId
	""")
	List<Crew> joinStoreByMapStoreId(@Param(value = "mapStoreId") String mapStoreId);
}
