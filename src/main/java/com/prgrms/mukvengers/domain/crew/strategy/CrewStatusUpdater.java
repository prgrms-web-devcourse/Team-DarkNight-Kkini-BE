package com.prgrms.mukvengers.domain.crew.strategy;

import com.prgrms.mukvengers.domain.crew.exception.CrewStatusException;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crew.model.vo.CrewStatus;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class CrewStatusUpdater {
    private final Map<CrewStatus, CrewStatusStrategy> strategies;

    public CrewStatusUpdater(CloseStrategy closeStrategy, FinishStrategy finishStrategy) {
        strategies = new HashMap<>();
        strategies.put(CrewStatus.CLOSE, closeStrategy);
        strategies.put(CrewStatus.FINISH, finishStrategy);
    }

    public void updateStatus(Crew crew, CrewMember crewMember, CrewStatus crewStatus) {
        CrewStatusStrategy strategy = strategies.get(crewStatus);
        if (strategy == null) {
            throw new CrewStatusException(crewStatus);
        }

        strategy.execute(crew, crewMember);
        crew.changeStatus(crewStatus);
    }
}
