package com.prgrms.mukvengers.domain.crew.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.crew.model.Crew;

public interface CrewRepository extends JpaRepository<Crew, Long> {

	// N+1 발생
	@Query(value = """
		SELECT c
		FROM Crew c
		JOIN FETCH c.store s
		WHERE s.placeId = :placeId AND c.status = 'RECRUITING'
		""",
		countQuery = "SELECT count(c) FROM Crew c WHERE c.store.placeId = :placeId")
	Page<Crew> findAllByPlaceId(@Param(value = "placeId") String placeId, Pageable pageable);

	@Query(nativeQuery = true, value =
		"SELECT * FROM crew c "
		+ "WHERE ST_DISTANCE_SPHERE(:location, c.location) < :distance AND c.status = 'RECRUITING'")
	List<Crew> findAllByLocation(@Param("location") Point location, @Param("distance") int distance);

	@Query(nativeQuery = true, value = """
		SELECT * FROM crew c
		WHERE ST_Contains(
		ST_Buffer(:location, :radius), c.location)
		AND c.status = 'RECRUITING'
		""")
	List<Crew> findAllByLocation(@Param("location") Point location, @Param("radius") Double radius);

	@Modifying
	@Query(value = """
		UPDATE Crew c
		SET c.status = 'FINISH'
		WHERE c.status != 'FINISH' AND c.promiseTime < :time
		""")
	int updateAllStatusToFinish(@Param("time") LocalDateTime now);

	@Query(value = "SELECT * FROM crew c ORDER BY RAND() LIMIT :count", nativeQuery = true)
	List<Crew> findRandom(@Param("count") int count);
}
