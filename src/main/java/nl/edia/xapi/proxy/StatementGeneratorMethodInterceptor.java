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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nl.edia.xapi.annotation.StatementGenerator;
import nl.edia.xapi.statements.NullStatementGenerator;
import nl.edia.xapi.statements.StatementClientFactory;

@Component
public class StatementGeneratorMethodInterceptor extends AstractMethodInterceptor implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8152898893200457420L;

	/**
	 * A reference to the log facility.
	 */
	private static final Log LOG = LogFactory.getLog(StatementGeneratorMethodInterceptor.class);

	/**
	 * The statementClientBuilder class.
	 */
	@Autowired
	protected StatementClientFactory statementClientFactory;

	/**
	 * Runs the {@link StatementGenerator} enhancement.
	 * @param invocation
	 * @param returnValue
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected void doAnalysis(MethodInvocation invocation, Object returnValue) throws IllegalAccessException, InvocationTargetException {
		// Get the called method
		Method method = invocation.getMethod();
		StatementGenerator annotation = method.getAnnotation(StatementGenerator.class);
		if (annotation != null) {
			nl.edia.xapi.statements.StatementGenerator bean = getStatementGenerator(annotation);
			if (bean == null) {
				LOG.error("Unable to locate StatementGenerator bean, @StatementGenerator annotation ignored.");
			} else {
				String verb = annotation.verb();
				Map<Class<?>, Object> values = AopReflectionUtils.getParameterClassesAndValues(invocation, returnValue);
				gov.adlnet.xapi.model.Statement statement = bean.generate(verb, returnValue, values);
				if (statement != null) {
					send(statementClientFactory.build(invocation, returnValue), statement);
				}
			}
		}
	}

	protected nl.edia.xapi.statements.StatementGenerator getStatementGenerator(StatementGenerator annotation) {
		Class<? extends nl.edia.xapi.statements.StatementGenerator> beanClass = annotation.statementGenerator();
		if (beanClass != null && !ObjectUtils.equals(NullStatementGenerator.class, beanClass)) {
			return instantiate(beanClass);
		}
		String beanName = annotation.beanName();
		nl.edia.xapi.statements.StatementGenerator bean = null;
		if (StringUtils.isNotEmpty(beanName)) {
			bean = applicationContext.getBean(beanName, nl.edia.xapi.statements.StatementGenerator.class);
		}
		if (bean == null) {
			LOG.error("Cannot locate StatementGenerator instance");
		}
		return bean;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		Object rv = invocation.proceed();
		try {
			doAnalysis(invocation, rv);
		} catch (Throwable t) {
			LOG.error("Something unexpected occured while intercepting method calls for the learningAnalyticsService, this error is suppressed.", t);
		}
		return rv;
	}

}