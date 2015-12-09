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

import org.aopalliance.intercept.MethodInvocation;

import gov.adlnet.xapi.client.StatementClient;

/**
 * Simple implementation of the {@link StatementClientFactory}, just wraps the provided
 * {@link StatementClient}.
 * @author Roland Groen <roland@edia.nl>
 *
 */
public class DefaultStatementClientFactory implements StatementClientFactory {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2551030823805224564L;
	/**
	 * A reference to the statementClient.
	 */
	protected transient StatementClient statementClient;

	/**
	 * Default constructor.
	 */
	public DefaultStatementClientFactory() {
		super();
	}

	/**
	 * Constructor with statementclient.
	 * @param statementClient
	 */
	public DefaultStatementClientFactory(StatementClient statementClient) {
		super();
		this.statementClient = statementClient;
	}

	/* (non-Javadoc)
	 * @see nl.edia.xapi.statements.StatementClientFactory#build(org.aopalliance.intercept.MethodInvocation, java.lang.Object)
	 */
	@Override
	public StatementClient build(MethodInvocation invocation, Object returnValue) {
		return statementClient;
	}

	/**
	 * Sets the statementClient field.
	 * @param statementClient
	 */
	public void setStatementClient(StatementClient statementClient) {
		this.statementClient = statementClient;
	}

}
