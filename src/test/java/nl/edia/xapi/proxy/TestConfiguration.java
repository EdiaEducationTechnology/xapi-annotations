/**
 * 	Copyright 2015 EDIA B.V. Licensed under the
 * 	Educational Community License, Version 2.0 (the "License"); you may
 * 	not use this file except in compliance with the License. You may
 * 	obtain a copy of the License at
 * 	
 * 	http://www.osedu.org/licenses/ECL-2.0
 * 	
 * 	Unless required by applicable law or agreed to in writing,
 * 	software distributed under the License is distributed on an "AS IS"
 * 	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * 	or implied. See the License for the specific language governing
 * 	permissions and limitations under the License.
 */
package nl.edia.xapi.proxy;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import nl.edia.xapi.proxy.domain.ConfigurationKeys;
import nl.edia.xapi.proxy.domain.ConfigurationService;
import nl.edia.xapi.proxy.domain.Module;
import nl.edia.xapi.proxy.domain.ModuleRun;
import nl.edia.xapi.statements.StatementBuilder;
import nl.edia.xapi.statements.StatementGenerator;

@Configuration
@ComponentScan(basePackages = "nl.edia.xapi.proxy.impl")
public class TestConfiguration {
	
	@Bean
	public ConfigurationService configuration() {
		ConfigurationService rv = Mockito.mock(ConfigurationService.class);
		Mockito.when(rv.getProperty(Mockito.eq(ConfigurationKeys.XAPI_URL), Mockito.any(ModuleRun.class))).thenReturn("http://localhost:80/test");
		Mockito.when(rv.getProperty(Mockito.eq(ConfigurationKeys.XAPI_URL), Mockito.any(Module.class))).thenReturn("http://localhost:80/test");
		return rv;
	}
	
	@Bean(name = "myStatementGenerator")
	public StatementGenerator statementGenerator() {
		return new StatementGeneratorMethodInterceptorTest.MyStatementGenerator();
	}
	@Bean(name = "myStatementBuilder")
	public StatementBuilder statementBuilder() {
		return Mockito.mock(StatementBuilder.class);
	}
	
	
}