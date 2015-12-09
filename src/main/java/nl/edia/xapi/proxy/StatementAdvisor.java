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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aopalliance.aop.Advice;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import nl.edia.xapi.annotation.Actor;
import nl.edia.xapi.annotation.Statement;
import nl.edia.xapi.annotation.StatementObject;
import nl.edia.xapi.annotation.Statement.ValidationStyle;

@Component
public class StatementAdvisor extends AbstractPointcutAdvisor {

	private static final long serialVersionUID = 1L;

	/**
	 * The {@link Pointcut} instance to use.
	 */
	private static final StaticMethodMatcherPointcut POINTCUT = new StaticMethodMatcherPointcut() {
		
		/* (non-Javadoc)
		 * @see org.springframework.aop.MethodMatcher#matches(java.lang.reflect.Method, java.lang.Class)
		 */
		@Override
		public boolean matches(Method method, Class<?> targetClass) {
			Assert.notNull(method);
			boolean annotationPresent = method.isAnnotationPresent(Statement.class);
			if (annotationPresent) {
				// Get the annotation
				Statement annotation = method.getAnnotation(Statement.class);
				// Get the validation
				ValidationStyle validation = annotation.validation();
				if (validation == ValidationStyle.strict) {
					validateStatement(method, annotation);
				}

			}
			return annotationPresent;
		}

		/**
		 * Validates the statement annotation and the associated parameter annotations.
		 * @param method
		 * @param annotation
		 */
		protected void validateStatement(Method method, Statement annotation) {
			boolean verbPresent = StringUtils.isNotEmpty(annotation.verb());
			boolean objectPresent = method.isAnnotationPresent(StatementObject.class);
			boolean actorPresent = method.isAnnotationPresent(Actor.class);
			Annotation[][] parameterAnnotations = method.getParameterAnnotations();
			for (Annotation[] annotations : parameterAnnotations) {
				for (Annotation ann : annotations) {
					if (ann.annotationType().equals(Actor.class)) {
						actorPresent = true;
					}
					if (ann.annotationType().equals(StatementObject.class)) {
						objectPresent = true;
					}
				}
			}
			if (!verbPresent) {
				throw new IllegalArgumentException("The verb should be set!");
			}
			if (!objectPresent) {
				throw new IllegalArgumentException("There should be an @StatementObject parameter defined for method: " + method);
			}
			if (!actorPresent) {
				throw new IllegalArgumentException("There should be an @Actor parameter defined for method: " + method);
			}
		}
	};

	/**
	 * A reference to the interceptor.
	 */
	@Autowired
	private StatementMethodInterceptor interceptor;

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
	public void setInterceptor(StatementMethodInterceptor interceptor) {
		this.interceptor = interceptor;
	}
}
