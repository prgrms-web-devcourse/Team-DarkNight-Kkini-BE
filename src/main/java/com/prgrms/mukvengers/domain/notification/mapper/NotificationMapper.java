package com.prgrms.mukvengers.domain.notification.mapper;

import static org.mapstruct.ReportingPolicy.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.prgrms.mukvengers.domain.notification.dto.response.NotificationResponse;
import com.prgrms.mukvengers.domain.notification.model.Notification;

@Mapper(componentModel = "spring", unmappedSourcePolicy = IGNORE)
public interface NotificationMapper {

	@Mapping(target = "content", source = "content.content")
	@Mapping(target = "type", source = "type")
	@Mapping(target = "id", source = "id")
	@Mapping(target = "isRead", source = "isRead")
	@Mapping(target = "createdAt", source = "createdAt")
	NotificationResponse toNotificationResponse(Notification notification);
}
