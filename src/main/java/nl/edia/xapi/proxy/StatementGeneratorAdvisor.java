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

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import nl.edia.xapi.annotation.StatementGenerator;
import nl.edia.xapi.statements.NullStatementGenerator;

@Component
public class StatementGeneratorAdvisor extends AbstractPointcutAdvisor {

	private static final long serialVersionUID = 1L;

	/**
	 * The {@link Pointcut} instance to use.
	 */
	private static final StaticMethodMatcherPointcut POINTCUT = new StaticMethodMatcherPointcut() {
		@Override
		public boolean matches(Method method, Class<?> targetClass) {
			Assert.notNull(method);
			boolean annotationPresent = method.isAnnotationPresent(StatementGenerator.class);
			if (annotationPresent) {
				StatementGenerator annotation = method.getAnnotation(StatementGenerator.class);
				if (StringUtils.isEmpty(annotation.beanName()) && ObjectUtils.equals(annotation.statementGenerator(), NullStatementGenerator.class)) {
					throw new IllegalArgumentException("At least a beanName or statementGenerator should be defined.");
				}
				
			}
			return annotationPresent;
		}
	};
	
	/**
	 * A reference to the interceptor.
	 */
	@Autowired
	private StatementGeneratorMethodInterceptor interceptor;

	/* (non-Javadoc)
	 * @see org.springframework.aop.Advisor#getAdvice()
	 */
	@Override
	public Advice getAdvice() {
		return this.interceptor;
	}

	/* (non-Javadoc)
	 * @see org.springframework.aop.PointcutAdvisor#getPointcut()
	 */
	@Override
	public Pointcut getPointcut() {
		return POINTCUT;
	}

	/**
	 * @param interceptor
	 */
	public void setInterceptor(StatementGeneratorMethodInterceptor interceptor) {
		this.interceptor = interceptor;
	}
}
