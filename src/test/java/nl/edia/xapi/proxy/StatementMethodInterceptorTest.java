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

import java.io.IOException;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import gov.adlnet.xapi.client.StatementClient;
import nl.edia.xapi.annotation.Actor;
import nl.edia.xapi.annotation.Context;
import nl.edia.xapi.annotation.Statement;
import nl.edia.xapi.annotation.StatementObject;
import nl.edia.xapi.proxy.domain.Course;
import nl.edia.xapi.proxy.domain.Module;
import nl.edia.xapi.proxy.domain.Text;
import nl.edia.xapi.proxy.domain.User;
import nl.edia.xapi.proxy.impl.TestObjectBuilder;
import nl.edia.xapi.proxy.impl.TestStatementClientFactory;
import nl.edia.xapi.statements.DefaultStatementBuilder;
import nl.edia.xapi.statements.DefaultStatementClientFactory;
import nl.edia.xapi.statements.NullObjectBuilder;
import nl.edia.xapi.statements.ObjectBuilder;
import nl.edia.xapi.statements.StatementClientFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes={TestConfiguration.class})
public class StatementMethodInterceptorTest extends StatementMethodInterceptor {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3705560407788286073L;
	
	StatementClient statementClient;

	protected class LeaningAnalyticsMethodInterceptorTestTarget {
		public void doSomeThing0a() {
			// Empty
		}
		@Statement(verb = "experienced")
		public void doSomeThing0b() {
			// Empty
		}

		@Statement(verb = "experienced")
		public void doSomeThing1a(@Actor User user, @Context Course course, @StatementObject Module module) {
			// Empty
		}

		@Statement(verb = "experienced", objectBuilder=TestObjectBuilder.class, statementBuilder=DefaultStatementBuilder.class)
		public void doSomeThing1b(@Actor User user, @Context Course course, @StatementObject Module module) {
			// Empty
		}
		
		@Statement(verb = "experienced", objectBuilderBean="testObjectBuilder", statementBuilderBean="myStatementBuilder")
		public void doSomeThing1c(@Actor User user, @Context Course course, @StatementObject Module module) {
			// Empty
		}
		
		@Statement(verb = "experienced", objectBuilderBean="x", statementBuilderBean="x")
		public void doSomeThing1d(@Actor User user, @Context Course course, @StatementObject Module module) {
			// Empty
		}
		
		@Statement(verb = "experienced")
		public void doSomeThing4a(@Actor User user, @StatementObject  Text text, @Context Course course,  Module module) {
			// Empty
		}
		@Statement(verb = "experienced")
		public  @StatementObject  Text doSomeThing4b(@Actor User user, @Context Course course,  Module module) {
			return Mockito.mock(Text.class);
		}

	}

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
		Mockito.verify(this.statementClientFactory, Mockito.times(0)).build(Mockito.eq(mock), Mockito.any());
	}

	@Test
	public void test1a() throws Throwable {
		MethodInvocation mock = Mockito.mock(MethodInvocation.class);
		Mockito.when(mock.getMethod()).thenReturn(getMethod("doSomeThing1a"));
		Mockito.when(mock.getArguments()).thenReturn(new Object[] { Mockito.mock(User.class), Mockito.mock(Course.class), Mockito.mock(Module.class) });
		Assert.assertTrue(isaSync());
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(1)).build(Mockito.eq(mock), Mockito.any());
		synchronized (this) {
			Thread.currentThread().sleep(1000);
		}
		Mockito.verify(statementClient, Mockito.times(1)).postStatement(Mockito.any(gov.adlnet.xapi.model.Statement.class));

		// Without sync
		setaSync(false);
		Assert.assertFalse(isaSync());
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(2)).build(Mockito.eq(mock), Mockito.any());
		Mockito.verify(statementClient, Mockito.times(2)).postStatement(Mockito.any(gov.adlnet.xapi.model.Statement.class));

		
		Mockito.when(statementClient.postStatement(Mockito.any(gov.adlnet.xapi.model.Statement.class))).thenThrow(IOException.class);
		// Try an exception async false
		setaSync(false);
		Assert.assertFalse(isaSync());
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(3)).build(Mockito.eq(mock), Mockito.any());
		Mockito.verify(statementClient, Mockito.times(3)).postStatement(Mockito.any(gov.adlnet.xapi.model.Statement.class));
		// Try an exception async true
		setaSync(true);
		Assert.assertTrue(isaSync());
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(4)).build(Mockito.eq(mock), Mockito.any());
		Mockito.verify(statementClient, Mockito.times(4)).postStatement(Mockito.any(gov.adlnet.xapi.model.Statement.class));

	}
	@Test
	public void test1b() throws Throwable {
		MethodInvocation mock = Mockito.mock(MethodInvocation.class);
		Mockito.when(mock.getMethod()).thenReturn(getMethod("doSomeThing1b"));
		Mockito.when(mock.getArguments()).thenReturn(new Object[] { Mockito.mock(User.class), Mockito.mock(Course.class), Mockito.mock(Module.class) });
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(1)).build(Mockito.eq(mock), Mockito.any());
	}

	@Test
	public void test1c() throws Throwable {
		MethodInvocation mock = Mockito.mock(MethodInvocation.class);
		Mockito.when(mock.getMethod()).thenReturn(getMethod("doSomeThing1c"));
		Mockito.when(mock.getArguments()).thenReturn(new Object[] { Mockito.mock(User.class), Mockito.mock(Course.class), Mockito.mock(Module.class) });
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(1)).build(Mockito.eq(mock), Mockito.any());
	}
	
	@Test
	public void test1d() throws Throwable {
		MethodInvocation mock = Mockito.mock(MethodInvocation.class);
		Mockito.when(mock.getMethod()).thenReturn(getMethod("doSomeThing1d"));
		Mockito.when(mock.getArguments()).thenReturn(new Object[] { Mockito.mock(User.class), Mockito.mock(Course.class), Mockito.mock(Module.class) });
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(0)).build(Mockito.eq(mock), Mockito.any());
	}
	
	@Test
	public void testa4a() throws Throwable {
		MethodInvocation mock = Mockito.mock(MethodInvocation.class);
		Mockito.when(mock.getMethod()).thenReturn(getMethod("doSomeThing4a"));
		Text text = Mockito.mock(Text.class);
		Mockito.when(text.getId()).thenReturn(12L);
		Mockito.when(text.getLocation()).thenReturn("http://somwehere.com");
		Mockito.when(mock.getArguments()).thenReturn(new Object[] { Mockito.mock(User.class), text, Mockito.mock(Course.class),  Mockito.mock(Module.class) });
		invoke(mock);
		Mockito.verify(this.statementClientFactory, Mockito.times(1)).build(Mockito.eq(mock), Mockito.any());
	}
	
	@Test
	public void testInstanciate() {
		instantiate(String.class);
		instantiate(NullObjectBuilder.class);
		instantiate(PrivateObjectBuilder.class);
	}
	
	@Test
	public void testAfterPropsSet() throws Exception {
		statementBuilder = null;
		this.afterPropertiesSet();
	}
	
	@Test(expected=NoSuchBeanDefinitionException.class)
	public void testAfterPropsSet1() throws Exception {
		this.statementClientFactory = null;
		this.afterPropertiesSet();
	}
	@Test
	public void testAfterPropsSet2() throws Exception {
		this.statementClientFactory = null;
		
		ApplicationContext x = this.applicationContext;
		this.applicationContext = Mockito.mock(ApplicationContext.class);
		StatementClient statementClient2 = Mockito.mock(StatementClient.class);
		Mockito.when(applicationContext.getBean(Mockito.eq(StatementClient.class))).thenReturn(statementClient2);
		this.afterPropertiesSet();
		Assert.assertEquals(statementClient2, statementClientFactory.build(null, null));
		this.applicationContext = x;
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAfterPropsSet3() throws Exception {
		this.statementClientFactory = null;
		
		ApplicationContext x = this.applicationContext;
		this.applicationContext = Mockito.mock(ApplicationContext.class);
		StatementClient statementClient2 = Mockito.mock(StatementClient.class);
		Mockito.when(applicationContext.getBean(Mockito.eq(StatementClient.class))).thenReturn(null);
		this.afterPropertiesSet();
	}
	
	private static class PrivateObjectBuilder implements ObjectBuilder{
		private PrivateObjectBuilder(){}

		@Override
		public <T> T build(Object value, Class<T> clazz) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
