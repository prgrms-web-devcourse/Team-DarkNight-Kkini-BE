package com.prgrms.mukvengers.domain.proposal.dto.request;

import javax.validation.constraints.NotBlank;

public record UpdateProposalRequest(@NotBlank String proposalStatus) {
}
