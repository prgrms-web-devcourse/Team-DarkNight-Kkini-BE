package com.prgrms.mukvengers.domain.crew.strategy;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.utils.CrewMemberObjectProvider;
import com.prgrms.mukvengers.utils.CrewObjectProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class CrewStatusUpdaterTest extends ServiceTest {

    @Autowired
    private CrewStatusUpdater crewStatusUpdater;

    @Test
    @DisplayName("전략 패턴으로 상태를 변경합니다.")
    void updateStatus_success(){

        Crew crew = CrewObjectProvider.createCrew(savedStore);
        CrewMember crewMember = CrewMemberObjectProvider.createCrewMember(savedUser1Id, crew, CrewMemberRole.LEADER);
        crew.addCrewMember(crewMember);
        crewRepository.save(crew);
        crewMemberRepository.save(crewMember);

        crewStatusUpdater.updateStatus(crew,crewMember, CrewStatus.CLOSE);

        Assertions.assertThat(crew.getStatus()).isEqualTo(CrewStatus.CLOSE);
    }
}