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

import java.util.Map;

import gov.adlnet.xapi.model.Statement;

/**
 * A StatementGenerator enables the generation of statements from the annotated verb 
 * and the parameters from the method call.
 * @author Roland Groen <roland@edia.nl>
 *
 */
public interface StatementGenerator {
	/**
	 * <p>
	 * Generate a statement based on the method parameters and return value.
	 * </p>
	 * 
	 * @param verb the verb from the annotation.
	 * @param returnValue the returned value from the method call.
	 * @param values the method parameters.
	 * @return the generated statement, may be null.
	 */
	Statement generate(String verb, Object returnValue, Map<Class<?>, Object> values);
}
