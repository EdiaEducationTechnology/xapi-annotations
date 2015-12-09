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

import org.springframework.util.Assert;

import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Result;
import gov.adlnet.xapi.model.Verb;

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

	/* (non-Javadoc)
	 * @see nl.edia.xapi.statements.ObjectBuilder#build(java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T build(Object value, Class<T> clazz) {
		Assert.notNull(clazz, "The clazz parameter may never be null");
		if (clazz.isAssignableFrom(IStatementObject.class)) {
			return (T) buildActivity(value);
		}
		if (clazz.isAssignableFrom(Actor.class)) {
			return (T) buildActor(value);
		}
		if (clazz.isAssignableFrom(Context.class)) {
			return (T) buildContext(value);
		}
		if (clazz.isAssignableFrom(Result.class)) {
			return (T) buildResult(value);
		}
		if (clazz.isAssignableFrom(Verb.class)) {
			return (T) buildVerb(value);
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
	 * Builds the {@link Actor} object.
	 * @param value the parameter value.
	 * @return the value represented as {@link Actor}.
	 */
	protected abstract Actor buildActor(Object value);

	/**
	 * Builds the {@link Context} object.
	 * @param value the parameter value.
	 * @return the value represented as {@link Context}.
	 */
	protected abstract Context buildContext(Object value);

	/**
	 * Builds the {@link Result} object.
	 * @param value the parameter value.
	 * @return the value represented as {@link Result}.
	 */
	protected abstract Result buildResult(Object value);

	/**
	 * Builds the {@link Verb} object.
	 * @param value the parameter value.
	 * @return the value represented as {@link Verb}.
	 */
	protected abstract Verb buildVerb(Object value);

}
