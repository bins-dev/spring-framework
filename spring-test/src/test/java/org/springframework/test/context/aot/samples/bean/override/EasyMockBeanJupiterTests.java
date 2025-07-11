/*
 * Copyright 2002-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.test.context.aot.samples.bean.override;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.aot.samples.common.GreetingService;
import org.springframework.test.context.aot.samples.common.MessageService;
import org.springframework.test.context.bean.override.easymock.EasyMockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

/**
 * @author Sam Brannen
 * @since 6.2
 */
@SpringJUnitConfig
public class EasyMockBeanJupiterTests {

	/**
	 * Mock for nonexistent bean.
	 */
	@EasyMockBean
	GreetingService greetingService;

	/**
	 * Mock for existing bean.
	 */
	@EasyMockBean
	MessageService messageService;

	@BeforeEach
	void configureMocks(@Autowired GreetingService greetingService, @Autowired MessageService messageService) {
		expect(greetingService.greeting()).andReturn("enigma");
		expect(messageService.generateMessage()).andReturn("override");
		replay(greetingService, messageService);
	}

	@Test
	void test() {
		assertThat(greetingService.greeting()).isEqualTo("enigma");
		assertThat(messageService.generateMessage()).isEqualTo("override");
	}

	@Configuration(proxyBeanMethods = false)
	static class Config {

		@Bean
		MessageService messageService() {
			return () -> "prod";
		}
	}

}
