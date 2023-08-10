package com.prgrms.mukvengers.domain.crew.strategy;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;

public interface CrewStatusStrategy {
    void execute(Crew crew, CrewMember crewMember);
}
