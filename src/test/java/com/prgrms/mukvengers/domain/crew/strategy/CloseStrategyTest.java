package com.prgrms.mukvengers.domain.crew.strategy;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.crew.exception.CrewStatusException;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.domain.crew.service.CrewService;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.utils.CrewMemberObjectProvider;
import com.prgrms.mukvengers.utils.CrewObjectProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CloseStrategyTest extends ServiceTest {

    @Autowired
    private CloseStrategy closeStrategy;

    @Autowired
    private CrewService crewService;

    @Test
    @DisplayName("상태를 Close 변경할 때 상태가 모집중이 아니면 에러가 발생합니다.")
    void execute_fail() {

        Crew crew = CrewObjectProvider.createCrew(savedStore);
        CrewMember crewMember = CrewMemberObjectProvider.createCrewMember(savedUser1Id, crew, CrewMemberRole.LEADER);
        crew.addCrewMember(crewMember);
        crewRepository.save(crew);
        crewMemberRepository.save(crewMember);

        crewService.updateStatus(crew.getId(), savedUser1Id, CrewStatus.CLOSE);
        Assertions.assertThatThrownBy(()-> closeStrategy.execute(crew, crewMember))
                        .isInstanceOf(CrewStatusException.class);

    }
}