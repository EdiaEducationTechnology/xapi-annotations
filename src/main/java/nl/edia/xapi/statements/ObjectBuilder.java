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

import java.io.Serializable;

import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Result;
import gov.adlnet.xapi.model.Verb;

/**
 * Builder interface that delegates the creation of Objects to the build method. 
 * @author Roland Groen <roland@edia.nl>
 *
 */
public interface ObjectBuilder extends Serializable {
	/**
	 * <p>
	 * The build method delegates the creation of the xAPI domain objects (gov.adlnet.xapi.model.*) 
	 * to the implementing builder. Typically the domain objects are:
	 * {@link IStatementObject}
	 * {@link Actor}
	 * {@link Context}
	 * {@link Result}
	 * {@link Verb}.
	 * </p>
	 * 
	 * <p>
	 * PLEASE NOTE: The builder is also called if no value for the object is available. For instance, if
	 * the agent object can be fetched from a different location than the method parameters, the @Agent annotation
	 * can be omitted and the implementation can return the agent object regardless of the null value.
	 * </p>
	 * 
	 * @param value, this is the object value, null will be passed if there is no value.
	 * @param clazz, the Class object, may never be null.
	 * @return a domain object, null if none can be created.
	 */
	<T> T build(Object value, Class<T> clazz);
}
