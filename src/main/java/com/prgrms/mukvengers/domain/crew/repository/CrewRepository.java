package com.prgrms.mukvengers.domain.crew.repository;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
			AND s.id < :id
		""")
	Slice<Crew> joinStoreByMapStoreId(@Param(value = "mapStoreId") String mapStoreId, @Param("id") Long id,
		Pageable pageable);

	@Query(nativeQuery = true, value =
		"SELECT *, ST_DISTANCE_SPHERE(ST_POINTFROMTEXT(:#{#location.toText()}, 4326), ST_SRID(crew.location, 4326)) dist FROM crew "
			+ "WHERE ST_DISTANCE_SPHERE(ST_POINTFROMTEXT(:#{#location.toText()}, 4326), ST_SRID(crew.location, 4326)) < :distance "
			+ "ORDER BY dist ASC")
	List<Crew> findAllByLocation(@Param("location") Point location, @Param("distance") int distance);

}
