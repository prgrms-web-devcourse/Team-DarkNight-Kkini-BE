package com.prgrms.mukvengers.domain.crew.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.mukvengers.domain.crew.model.Crew;

public interface CrewRepository extends JpaRepository<Crew, Long> {
}
