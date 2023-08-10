package com.prgrms.mukvengers.domain.crew.strategy;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CloseStrategy implements CrewStatusStrategy{
    @Override
    public void execute(Crew crew, CrewMember crewMember) {
        crew.isNotRecruiting();
    }
}
