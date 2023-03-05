package com.prgrms.mukvengers.domain.crewmember.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.exception.CrewNotFoundException;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.crewmember.mapper.CrewMemberMapper;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.Role;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.common.dto.IdResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrewMemberServiceImpl implements CrewMemberService {

	private final CrewRepository crewRepository;
	private final CrewMemberRepository crewMemberRepository;
	private final CrewMemberMapper crewMemberMapper;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public IdResponse create(Long crewId, Long userId, Role role) {

		userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new CrewNotFoundException(crewId));

		CrewMember crewMember = crewMemberMapper.toCrewMember(crew, userId, role);

		crewMemberRepository.save(crewMember);

		return new IdResponse(crewMember.getId());
	}
}
