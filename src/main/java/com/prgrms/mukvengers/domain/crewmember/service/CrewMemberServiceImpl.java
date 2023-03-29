package com.prgrms.mukvengers.domain.crewmember.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.exception.MemberNotFoundException;
import com.prgrms.mukvengers.domain.crewmember.exception.NotLeaderException;
import com.prgrms.mukvengers.domain.crewmember.exception.NotMemberException;
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

	@Override
	@Transactional
	public void block(Long userId, Long blockUserId, Long crewId) {

		CrewMember leader = crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, userId)
			.orElseThrow(() -> new MemberNotFoundException(userId));

		if (!leader.isLeader()) {
			throw new NotLeaderException(leader.getCrewMemberRole());
		}

		CrewMember member = crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, blockUserId)
			.orElseThrow(() -> new MemberNotFoundException(blockUserId));

		if (!member.isMember()) {
			throw new NotMemberException(member.getCrewMemberRole());
		}

		member.blockMember();
  	}
  
	public void delete(Long userId, Long crewId) {

		crewMemberRepository.deleteByUserIdAndCrewId(userId, crewId);
	}
  
}
