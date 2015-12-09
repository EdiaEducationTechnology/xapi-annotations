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
package nl.edia.xapi.proxy.impl;

import java.net.MalformedURLException;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import gov.adlnet.xapi.client.StatementClient;
import nl.edia.xapi.proxy.AopReflectionUtils;
import nl.edia.xapi.proxy.domain.ConfigurationKeys;
import nl.edia.xapi.proxy.domain.ConfigurationService;
import nl.edia.xapi.proxy.domain.Module;
import nl.edia.xapi.proxy.domain.ModuleRun;
import nl.edia.xapi.statements.StatementClientFactory;

@Component
@Qualifier("testStatementClientFactory")
public class TestStatementClientFactory implements StatementClientFactory {
	
	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = -4576394209753761102L;

	private static final Log LOG = LogFactory.getLog(TestStatementClientFactory.class);
	
	@Autowired
	protected ConfigurationService configurationService;

	@Override
	public StatementClient build(MethodInvocation invocation, Object returnValue) {
		Map<Class<?>, Object> values = AopReflectionUtils.getParameterClassesAndValues(invocation, returnValue);
		
		try {
			ModuleRun moduleRun = (ModuleRun)values.get(ModuleRun.class);
			if (moduleRun != null) {
				final String uri = configurationService.getProperty(ConfigurationKeys.XAPI_URL, moduleRun);
				final String user = configurationService.getProperty(ConfigurationKeys.XAPI_USERNAME, moduleRun);
				final String password = configurationService.getProperty(ConfigurationKeys.XAPI_PASSWORD, moduleRun);
				return new StatementClient(uri, user, password);
			} 
			Module module = (Module)values.get(Module.class);
			if (module != null) {
				final String uri = configurationService.getProperty(ConfigurationKeys.XAPI_URL, module);
				final String user = configurationService.getProperty(ConfigurationKeys.XAPI_USERNAME, module);
				final String password = configurationService.getProperty(ConfigurationKeys.XAPI_PASSWORD, module);
				return new StatementClient(uri, user, password);
			}
		} catch (MalformedURLException e) {
			LOG.warn("Cannot create URL", e);
		}
		return null;
	}


}
