package com.prgrms.mukvengers.domain.crew.strategy;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class FinishStrategy implements CrewStatusStrategy{

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void execute(Crew crew, CrewMember crewMember) {
        crew.isNotClose();

        crew.getCrewMembers().forEach(c -> {
            User user = userRepository.findById(c.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(c.getUserId()));
            user.updateLeaderCount(c);
            user.updateCrewCount();
        });
    }
}
