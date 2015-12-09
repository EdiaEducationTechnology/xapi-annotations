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
package nl.edia.xapi.proxy.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.Duration;
import org.joda.time.format.ISOPeriodFormat;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import gov.adlnet.xapi.model.Activity;
import gov.adlnet.xapi.model.ActivityDefinition;
import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.Agent;
import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Result;
import gov.adlnet.xapi.model.Score;
import gov.adlnet.xapi.model.Verb;
import gov.adlnet.xapi.model.Verbs;
import nl.edia.xapi.proxy.domain.*;
import nl.edia.xapi.statements.ObjectBuilderAdapter;

@Component
@Qualifier("testObjectBuilder")
public class TestObjectBuilder extends ObjectBuilderAdapter {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4569134324374193836L;

	/**
	 * The logging facility.
	 */
	private static final Log LOG = LogFactory.getLog(TestObjectBuilder.class);
	
	public IStatementObject buildActivity(Object target) {
		String id = null;
		ActivityDefinition definition = new ActivityDefinition();
		if (target instanceof TextExposure) {
			Text text = ((TextExposure) target).getText();
			id = text.getLocation();
			definition.setName(new HashMap<String, String>());
			definition.getName().put(Locale.getDefault().toString(), text.getTitleTxt());
		} else if (target instanceof Text) {
			Text text = ((Text) target);
			id = text.getLocation();
			definition.setName(new HashMap<String, String>());
			definition.getName().put(Locale.getDefault().toString(), text.getTitleTxt());
		} else if (target instanceof TestResult) {
			id = Long.toString(((TestResult) target).getId());
		} else if (target instanceof Module) {
			Module module = (Module) target;
			id = Long.toString(module.getId());
			definition.setName(new HashMap<String, String>());
			definition.setDescription(new HashMap<String, String>());
			definition.getName().put(Locale.getDefault().toString(), module.getName());
			definition.getDescription().put(Locale.getDefault().toString(), module.getDescription());
		} else if (target instanceof Course) {
			Course course = (Course) target;
			id = Long.toString(course.getId());
			definition.setName(new HashMap<String, String>());
			definition.getName().put(Locale.getDefault().toString(), course.getName());
		}

		if (id != null) {
			return new Activity(id, definition);
		} 
		LOG.warn("Cannnot build object from object: " + (target == null ? null: target.getClass()));
		return null;
	}
	
	public Actor buildActor(Object object) {
		if (object instanceof User) {
			User user = (User) object;
			return new Agent(user.getUname(), user.getEmail());
		}
		return null;
	}

	public Context buildContext(Object object) {
		Context context = new Context();
		if (object instanceof Course) {
			Course course = (Course) object;
			context.setPlatform("knowble");
			context.setRegistration(course.getCourseGuid());
		}
		return context;
	}
	
	public Result buildResult(Object target) {
		Result result = null;
		if (target instanceof TestResult) {
			TestResult testResult = (TestResult) target;
			result = new Result();
			result.setCompletion(testResult.isFinished());
			Score score = new Score();
			score.setScaled((float) testResult.getPercCorrect());
			result.setScore(score);

			Timestamp starttime = testResult.getStarttime();
			Timestamp endtime = testResult.getEndtime();
			if (starttime != null && endtime != null) {
				Duration duration = new Duration(starttime.getTime(), endtime.getTime());
				result.setDuration(ISOPeriodFormat.standard().print(duration.toPeriod()));
			}
		}
		return result;
	}

	public Verb buildVerb(Object object) {
		if (object instanceof String) {
			String event = (String) object;
			try {
				Method method = Verbs.class.getMethod(event);
				return (Verb) method.invoke(null, new Object[]{});
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOG.error("Error creating verb", e);
			}
		}
		return null;
	}


}
