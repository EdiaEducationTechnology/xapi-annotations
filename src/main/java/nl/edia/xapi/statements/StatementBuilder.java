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
import java.util.Collection;

import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.Attachment;
import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Result;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.Verb;

/**
 * Builder for {@link Statement}s.
 * @author Roland Groen <roland@edia.nl>
 *
 */
public interface StatementBuilder extends Serializable {
	/**
	 * <p>
	 * This method generates a statement from its object. The {@link DefaultStatementBuilder} should do 
	 * the trick, you should only implement your own if you'd like to override the default behavior.
	 * </p>
	 * 
	 * @param actor A mandatory Agent or Group Object.
	 * @param verb The Verb defines the action between Actor and Activity.
	 * @param object The Object of a Statement can be an Activity, Agent/Group, Sub-Statement, or Statement Reference. It is the "this" part of the Statement, i.e. "I did this".
	 * @param result An optional field that represents a measured outcome related to the Statement in which it is included.
	 * @param context An optional field that provides a place to add contextual information to a Statement. All properties are optional.
	 * @param authority The authority property provides information about whom or what has asserted that this Statement is true.
	 * @param attachments A digital artifact providing evidence of a learning experience.
	 * @return
	 */
	Statement build(Actor actor, Verb verb, IStatementObject object, Result result, Context context, Actor authority, Collection<Attachment> attachments);
}
