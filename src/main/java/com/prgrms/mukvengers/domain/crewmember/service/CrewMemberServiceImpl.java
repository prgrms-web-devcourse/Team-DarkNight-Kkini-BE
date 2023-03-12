package com.prgrms.mukvengers.domain.crewmember.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.mapper.CrewMemberMapper;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrewMemberServiceImpl implements CrewMemberService {

	private final CrewMemberRepository crewMemberRepository;
	private final CrewMemberMapper crewMemberMapper;

	@Override
	@Transactional
	public IdResponse create(Crew crew, Long userId, CrewMemberRole crewMemberRole) {

		CrewMember crewMember = crewMemberMapper.toCrewMember(crew, userId, crewMemberRole);
		crewMemberRepository.save(crewMember);

		return new IdResponse(crewMember.getId());
	}
}
