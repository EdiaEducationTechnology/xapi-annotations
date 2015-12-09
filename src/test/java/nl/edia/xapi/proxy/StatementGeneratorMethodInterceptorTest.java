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
package nl.edia.xapi.proxy;

import java.lang.reflect.Method;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import gov.adlnet.xapi.client.StatementClient;
import nl.edia.xapi.annotation.Actor;
import nl.edia.xapi.annotation.Context;
import nl.edia.xapi.annotation.Statement;
import nl.edia.xapi.annotation.StatementGenerator;
import nl.edia.xapi.annotation.StatementObject;
import nl.edia.xapi.proxy.domain.Course;
import nl.edia.xapi.proxy.domain.Module;
import nl.edia.xapi.proxy.domain.Text;
import nl.edia.xapi.proxy.domain.User;
import nl.edia.xapi.statements.DefaultStatementClientFactory;
import nl.edia.xapi.statements.StatementClientFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { TestConfiguration.class })
public class StatementGeneratorMethodInterceptorTest extends StatementGeneratorMethodInterceptor {

	public static class MyStatementGenerator implements nl.edia.xapi.statements.StatementGenerator {

		@Override
		public gov.adlnet.xapi.model.Statement generate(String verb, Object returnValue, Map<Class<?>, Object> values) {
			return Mockito.mock(gov.adlnet.xapi.model.Statement.class);
		}

	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3705560407788286073L;

	protected class LeaningAnalyticsMethodInterceptorTestTarget {
		public void doSomeThing0a() {
			// Empty
		}
		@StatementGenerator(verb = "experienced", statementGenerator = MyStatementGenerator.class)
		public void doSomeThing0b() {
			// Empty
		}

		@StatementGenerator(verb = "experienced", statementGenerator = MyStatementGenerator.class)
		public void doSomeThing1(@Actor User user, @Context Course course, @StatementObject Module module) {
			// Empty
		}

		@StatementGenerator(verb = "experienced", statementGenerator = MyStatementGenerator.class)
		public @StatementObject Text doSomeThing4a(@Actor User user, @Context Course course, Module module) {
			return Mockito.mock(Text.class);
		}
		
		@StatementGenerator(verb = "experienced", beanName="myStatementGenerator")
		public @StatementObject Text doSomeThing4b(@Actor User user, @Context Course course, Module module) {
			return Mockito.mock(Text.class);
		}
		@StatementGenerator(verb = "experienced", beanName="noStatementGenerator")
		public @StatementObject Text doSomeThing4c(@Actor User user, @Context Course course, Module module) {
			return Mockito.mock(Text.class);
		}

	}

	private StatementClient statementClient;

	protected Method getMethod(String name) {
		Method m = null;
		Method[] methods = LeaningAnalyticsMethodInterceptorTestTarget.class.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(name)) {
				m = method;
				break;
			}
		}
		return m;
	}

	@Before
	public void setUp() {
		statementClient = Mockito.mock(StatementClient.class);
		this.statementClientFactory = Mockito.spy(new DefaultStatementClientFactory(statementClient));
	}

	@Test
	public void test0a() throws Throwable {
		MethodInvocation mock = Mockito.mock(MethodInvocation.class);
		Mockito.when(mock.getMethod()).thenReturn(getMethod("doSomeThing0a"));
		Mockito.when(mock.getArguments()).thenReturn(new Object[] {});
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(0)).build(Mockito.eq(mock), Mockito.any());
	}

	@Test
	public void test0b() throws Throwable {
		MethodInvocation mock = Mockito.mock(MethodInvocation.class);
		Mockito.when(mock.getMethod()).thenReturn(getMethod("doSomeThing0b"));
		Mockito.when(mock.getArguments()).thenReturn(new Object[] {});
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(1)).build(Mockito.eq(mock), Mockito.any());
	}
	
	@Test
	public void test1() throws Throwable {
		MethodInvocation mock = Mockito.mock(MethodInvocation.class);
		Mockito.when(mock.getMethod()).thenReturn(getMethod("doSomeThing1"));
		Mockito.when(mock.getArguments()).thenReturn(new Object[] { Mockito.mock(User.class), Mockito.mock(Course.class), Mockito.mock(Module.class) });
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(1)).build(Mockito.eq(mock), Mockito.any());
	}

	@Test
	public void testa4a() throws Throwable {
		MethodInvocation mock = Mockito.mock(MethodInvocation.class);
		Mockito.when(mock.getMethod()).thenReturn(getMethod("doSomeThing4a"));
		Text text = Mockito.mock(Text.class);
		Mockito.when(text.getId()).thenReturn(12L);
		Mockito.when(text.getLocation()).thenReturn("http://somwehere.com");
		Mockito.when(mock.getArguments()).thenReturn(new Object[] { Mockito.mock(User.class), text, Mockito.mock(Course.class), Mockito.mock(Module.class) });
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(1)).build(Mockito.eq(mock), Mockito.any());
	}
	@Test
	public void testa4b() throws Throwable {
		MethodInvocation mock = Mockito.mock(MethodInvocation.class);
		Mockito.when(mock.getMethod()).thenReturn(getMethod("doSomeThing4b"));
		Text text = Mockito.mock(Text.class);
		Mockito.when(text.getId()).thenReturn(12L);
		Mockito.when(text.getLocation()).thenReturn("http://somwehere.com");
		Mockito.when(mock.getArguments()).thenReturn(new Object[] { Mockito.mock(User.class), text, Mockito.mock(Course.class), Mockito.mock(Module.class) });
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(1)).build(Mockito.eq(mock), Mockito.any());
	}
	@Test
	public void testa4c() throws Throwable {
		MethodInvocation mock = Mockito.mock(MethodInvocation.class);
		Mockito.when(mock.getMethod()).thenReturn(getMethod("doSomeThing4c"));
		Text text = Mockito.mock(Text.class);
		Mockito.when(text.getId()).thenReturn(12L);
		Mockito.when(text.getLocation()).thenReturn("http://somwehere.com");
		Mockito.when(mock.getArguments()).thenReturn(new Object[] { Mockito.mock(User.class), text, Mockito.mock(Course.class), Mockito.mock(Module.class) });
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(0)).build(Mockito.eq(mock), Mockito.any());
	}
}
