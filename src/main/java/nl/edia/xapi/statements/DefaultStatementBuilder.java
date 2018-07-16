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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.Attachment;
import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Result;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.Verb;

/**
 * The default implementation of the {@link StatementBuilder}.
 * @author Roland Groen <roland@edia.nl>
 *
 */
public class DefaultStatementBuilder implements StatementBuilder {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9059323269522911848L;
	/**
	 * 
	 */
	protected static final String DATE_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ssX";

	/* (non-Javadoc)
	 * @see eu.knowble.leaninganalytics.statements.StatementBuilder#build(gov.adlnet.xapi.model.Actor, gov.adlnet.xapi.model.Verb, gov.adlnet.xapi.model.IStatementObject, gov.adlnet.xapi.model.Result, gov.adlnet.xapi.model.Context, gov.adlnet.xapi.model.Actor, java.util.Collection)
	 */
	public Statement build(Actor actor, Verb verb, IStatementObject object, Result result, Context context, Actor authority, Collection<Attachment> attachments) {
		if (actor == null || verb == null || object == null) {
			return null;
		}
		Statement st = new Statement();
		st.setActor(actor);
		st.setId(UUID.randomUUID().toString());
		st.setObject(object);
		st.setVerb(verb);
		st.setResult(result);
		st.setContext(context);
		st.setAuthority(authority);
		if (attachments != null) {
			st.setAttachments(new ArrayList<>(attachments));
		}
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_ISO8601, Locale.ENGLISH);
		st.setTimestamp(df.format(new Date()));
		return st;
	}

}
