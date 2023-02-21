package com.prgrms.mukvengers.base;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.prgrms.mukvengers.global.config.jpa.JpaConfig;

@Import(JpaConfig.class)
@DataJpaTest
public abstract class RepositoryTest {

}
