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

import org.aopalliance.intercept.MethodInvocation;

import gov.adlnet.xapi.client.StatementClient;

/**
 * The StatementClientFactory is used for implementations that need
 * dynamic {@link StatementClient}s instead of one single per application.
 * 
 * @author Roland Groen <roland@edia.nl>
 *
 */
public interface StatementClientFactory extends Serializable {
	/**
	 * Method that builds the stament client in the context of the 
	 * method call. The invocation and returnValue are provided.
	 * @param invocation the current method invocation.
	 * @param returnValue the return value of the call.
	 * @return an instance of the {@link StatementClient}.
	 */
	StatementClient build(MethodInvocation invocation, Object returnValue);
}
