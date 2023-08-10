package com.prgrms.mukvengers.domain.crew.strategy;

import com.prgrms.mukvengers.base.ServiceTest;
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

class FinishStrategyTest extends ServiceTest {

    @Autowired
    private FinishStrategy finishStrategy;

    @Autowired
    private CrewService crewService;

    @Test
    @DisplayName("상태를 Finish로 변경하고 user의 방장 횟수와 모임 횟수를 업데이트합니다.")
    void execute_success() {

        Crew crew = CrewObjectProvider.createCrew(savedStore);
        CrewMember crewMember = CrewMemberObjectProvider.createCrewMember(savedUser1Id, crew, CrewMemberRole.LEADER);
        crew.addCrewMember(crewMember);
        crewRepository.save(crew);
        crewMemberRepository.save(crewMember);

        crewService.updateStatus(crew.getId(), savedUser1Id, CrewStatus.CLOSE);
        finishStrategy.execute(crew, crewMember);

        Optional<User> optionalUser = userRepository.findById(savedUser1Id);

        User user = optionalUser.get();

        Assertions.assertThat(user)
                .hasFieldOrPropertyWithValue("crewCount", 1)
                .hasFieldOrPropertyWithValue("leaderCount", 1);
    }
}