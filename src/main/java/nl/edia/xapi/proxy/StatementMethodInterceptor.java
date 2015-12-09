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
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import gov.adlnet.xapi.client.StatementClient;
import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Result;
import gov.adlnet.xapi.model.Verb;
import nl.edia.xapi.annotation.Statement;
import nl.edia.xapi.statements.DefaultStatementBuilder;
import nl.edia.xapi.statements.DefaultStatementClientFactory;
import nl.edia.xapi.statements.NullObjectBuilder;
import nl.edia.xapi.statements.ObjectBuilder;
import nl.edia.xapi.statements.StatementBuilder;
import nl.edia.xapi.statements.StatementClientFactory;

@Component
public class StatementMethodInterceptor extends AstractMethodInterceptor implements InitializingBean, Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5562031982628188978L;

	/**
	 * A reference to the log facility.
	 */
	private static final Log LOG = LogFactory.getLog(StatementMethodInterceptor.class);

	@Autowired(required = false)
	protected ObjectBuilder objectBuilder;

	@Autowired(required = false)
	protected StatementBuilder statementBuilder;

	@Autowired(required = false)
	protected StatementClientFactory statementClientFactory;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (statementBuilder == null) {
			statementBuilder = new DefaultStatementBuilder();
		}

		if (statementClientFactory == null) {
			StatementClient statementClient = applicationContext.getBean(StatementClient.class);
			if (statementClient != null) {
				statementClientFactory = new DefaultStatementClientFactory(statementClient);
			}
		}
		if (statementClientFactory == null) {
			throw new IllegalArgumentException("A statementClient bean or statementClientFactory is required");
		}

	}

	/**
	 * Runs the {@link Statement} enhancement.
	 * @param invocation the invocation of the method call.
	 * @param returnValue the return value of the method call.
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected void doAnalysis(MethodInvocation invocation, Object returnValue) throws IllegalAccessException, InvocationTargetException {
		// Get the called method
		Method method = invocation.getMethod();

		Statement statementAnnotation = method.getAnnotation(Statement.class);
		if (statementAnnotation != null) {
			processStatement(invocation, returnValue, statementAnnotation);
		}
	}

	/**
	 * Runs the {@link Statement} enhancement.
	 * 
	 * @param invocation the invocation of the method call.
	 * @param returnValue the return value of the method call.
	 * @param statementAnnotation the {@link Statement} annotation.
	 */
	protected void processStatement(MethodInvocation invocation, Object returnValue, Statement statementAnnotation) {
		ObjectBuilder objectBuilder = getObjectBuilder(statementAnnotation);
		StatementBuilder statementBuilder = getStatementBuilder(statementAnnotation);
		Assert.notNull(objectBuilder, "An ObjectBuilder is required, please provide an instance of the ObjectBuilder as a bean.");
		// Required
		Actor actor = findValueForType(nl.edia.xapi.annotation.Actor.class, invocation, returnValue, Actor.class, objectBuilder);
		// Required
		Verb verb = objectBuilder.build(statementAnnotation.verb(), Verb.class);
		// Required
		IStatementObject object = findValueForType(nl.edia.xapi.annotation.StatementObject.class, invocation, returnValue, IStatementObject.class, objectBuilder);

		if (actor != null && verb != null && object != null) {
			Context context = findValueForType(nl.edia.xapi.annotation.Context.class, invocation, returnValue, Context.class, objectBuilder);
			Result result = findValueForType(nl.edia.xapi.annotation.Result.class, invocation, returnValue, Result.class, objectBuilder);
			gov.adlnet.xapi.model.Statement statement = statementBuilder.build(actor, verb, object, result, context, null, null);
			StatementClient statementClient = statementClientFactory.build(invocation, returnValue);
			if (statementClient != null && statement != null) {
				send(statementClient, statement);
			}
			
		}
	}

	/**
	 * Finds a value for a given annotation in the method parameter, converts it with the given builder.
	 * 
	 * @param annotationType the annotation type.
	 * @param invocation the method invocation.
	 * @param returnValue the method's return value.
	 * @param objectType the object type 
	 * @param builder the object builder.
	 * @return a converted value, possibly null of the parameter was null or the {@link ObjectBuilder#build(Object, Class)} returned null;
	 */
	protected <T, A extends Annotation> T findValueForType(Class<A> annotationType, MethodInvocation invocation, Object returnValue, Class<T> objectType, ObjectBuilder builder) {
		if (builder == null) {
			return null;
		}
		Method method = invocation.getMethod();
		int index = AopReflectionUtils.findParameterIndex(method, annotationType);
		Object[] arguments = invocation.getArguments();
		if (index != -1 && index < arguments.length) {
			Object object = arguments[index];
			return builder.build(object, objectType);
		} 
		// Annotated on method, eg the return type
		A annotation = method.getAnnotation(annotationType);
		if (annotation != null) {
			return builder.build(returnValue, objectType);
		}
		
		// Call the builder with NULL.
		return builder.build(null, objectType);
	}

	/**
	 * Finds the object builder, null if one found.
	 * @param statementAnnotation
	 * @return the object builder, null if none found.
	 */
	public ObjectBuilder getObjectBuilder(Statement statementAnnotation) {
		Class<? extends ObjectBuilder> objectBuilderClass = statementAnnotation.objectBuilder();
		if (objectBuilderClass != null && !ObjectUtils.equals(NullObjectBuilder.class, objectBuilderClass)) {
			ObjectBuilder instance = instantiate(objectBuilderClass);
			if (instance != null) {
				return instance;
			}
		}
		String beanName = statementAnnotation.objectBuilderBean();
		if (StringUtils.isNotEmpty(beanName)) {
			return applicationContext.getBean(beanName, ObjectBuilder.class);
		}
		return objectBuilder;
	}

	/**
	 * Finds the statement builder, null if one found.
	 * 
	 * @param statementAnnotation
	 * @return the statement builder, null if one found.
	 */
	public StatementBuilder getStatementBuilder(Statement statementAnnotation) {
		String beanName = statementAnnotation.statementBuilderBean();
		if (StringUtils.isNotEmpty(beanName)) {
			return applicationContext.getBean(beanName, StatementBuilder.class);
		}

		Class<? extends StatementBuilder> objectBuilderClass = statementAnnotation.statementBuilder();
		if (objectBuilderClass != null) {
			StatementBuilder instance = instantiate(objectBuilderClass);
			if (instance != null) {
				return instance;
			}
		}

		return statementBuilder;
	}

	/**
	 * Gets the statementClientFactory field value.
	 * @return the statementClientFactory field value.
	 */
	public StatementClientFactory getStatementClientFactory() {
		return statementClientFactory;
	}

	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
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