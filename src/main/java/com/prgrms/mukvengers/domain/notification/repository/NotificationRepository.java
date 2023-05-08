package com.prgrms.mukvengers.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.mukvengers.domain.notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
