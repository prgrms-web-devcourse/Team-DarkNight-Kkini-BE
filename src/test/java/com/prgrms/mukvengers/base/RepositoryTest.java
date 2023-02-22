package com.prgrms.mukvengers.base;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.prgrms.mukvengers.global.config.jpa.JpaConfig;

@DataJpaTest
@Import(JpaConfig.class)
public abstract class RepositoryTest {

}
