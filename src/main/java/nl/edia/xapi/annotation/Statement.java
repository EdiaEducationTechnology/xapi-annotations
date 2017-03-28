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
package nl.edia.xapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.edia.xapi.statements.DefaultStatementBuilder;
import nl.edia.xapi.statements.NullObjectBuilder;
import nl.edia.xapi.statements.ObjectBuilder;
import nl.edia.xapi.statements.StatementBuilder;

@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface Statement {
	
	enum ValidationStyle {
		strict,
		lenient
	}
	
	String verb();

	Class<? extends ObjectBuilder> objectBuilder() default NullObjectBuilder.class;

	String objectBuilderBean() default "";
	
	Class<? extends StatementBuilder> statementBuilder() default DefaultStatementBuilder.class;
	
	String statementBuilderBean() default "";
	
	ValidationStyle validation() default ValidationStyle.strict;
}
