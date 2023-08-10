package com.prgrms.mukvengers.domain.notification.mapper;

import com.prgrms.mukvengers.domain.notification.dto.response.NotificationResponse;
import com.prgrms.mukvengers.domain.notification.model.Notification;
import com.prgrms.mukvengers.domain.notification.model.vo.NotificationContent;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-10T11:38:02+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Amazon.com Inc.)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationResponse toNotificationResponse(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        String content = null;
        String type = null;
        Long id = null;
        boolean isRead = false;
        LocalDateTime createdAt = null;

        content = notificationContentContent( notification );
        if ( notification.getType() != null ) {
            type = notification.getType().name();
        }
        id = notification.getId();
        if ( notification.getIsRead() != null ) {
            isRead = notification.getIsRead();
        }
        createdAt = notification.getCreatedAt();

        NotificationResponse notificationResponse = new NotificationResponse( id, type, content, createdAt, isRead );

        return notificationResponse;
    }

    private String notificationContentContent(Notification notification) {
        if ( notification == null ) {
            return null;
        }
        NotificationContent content = notification.getContent();
        if ( content == null ) {
            return null;
        }
        String content1 = content.getContent();
        if ( content1 == null ) {
            return null;
        }
        return content1;
    }
}
