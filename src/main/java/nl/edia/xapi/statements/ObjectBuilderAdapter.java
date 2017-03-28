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
package nl.edia.xapi.statements;

import gov.adlnet.xapi.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

/**
 * Adapter class for the {@link ObjectBuilder}.
 * @author Roland Groen <roland@edia.nl>
 *
 */
public abstract class ObjectBuilderAdapter implements ObjectBuilder {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4045556449663328682L;
	
	/**
	 * The logging facility.
	 */
	private static final Log LOG = LogFactory.getLog(ObjectBuilderAdapter.class);

	/* (non-Javadoc)
	 * @see nl.edia.xapi.statements.ObjectBuilder#build(java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T build(Object value, Class<T> clazz) {
		Assert.notNull(clazz, "The clazz parameter may never be null");
		// Actor, verb and activity are required
		if (clazz.isAssignableFrom(Actor.class)) {
			return (T) buildActor(value);
		}
		if (clazz.isAssignableFrom(Verb.class)) {
			return (T) buildVerb(value);
		}
		if (clazz.isAssignableFrom(IStatementObject.class)) {
			return (T) buildActivity(value);
		}
		// Not required.
		if (clazz.isAssignableFrom(Context.class)) {
			return (T) buildContext(value);
		}
		if (clazz.isAssignableFrom(Result.class)) {
			return (T) buildResult(value);
		}
		return null;
	}

	/**
	 * Builds the {@link Actor} object.
	 * @param value the parameter value.
	 * @return the value represented as {@link Actor}.
	 */
	protected abstract Actor buildActor(Object value);

	/**
	 * Builds the {@link Verb} object.
	 * @param value the parameter value.
	 * @return the value represented as {@link Verb}.
	 */
	protected Verb buildVerb(Object value){
		if (value instanceof String) {
			String event = (String) value;
			if (isNotEmpty(event)) {
				try {
					Method method = Verbs.class.getMethod(event);
					return (Verb) method.invoke(null, new Object[]{});
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					LOG.error("Error creating verb", e);
				}
			}
		}
		return null;

	}

	/**
	 * Builds the {@link IStatementObject} object.
	 * @param value the parameter value.
	 * @return the value represented as {@link IStatementObject}.
	 */
	protected abstract IStatementObject buildActivity(Object value);

	/**
	 * Builds the {@link Context} object.
	 * @param value the parameter value.
	 * @return the value represented as {@link Context}.
	 */
	protected Context buildContext(Object value){
		// Adapter implementation returns null
		return null; 
	};

	/**
	 * Builds the {@link Result} object.
	 * @param value the parameter value.
	 * @return the value represented as {@link Result}.
	 */
	protected Result buildResult(Object value){ 
		// Adapter implementation returns null
		return null; 
	}

}
