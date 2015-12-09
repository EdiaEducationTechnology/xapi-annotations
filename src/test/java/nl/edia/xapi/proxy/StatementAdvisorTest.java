package nl.edia.xapi.proxy;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import nl.edia.xapi.annotation.Actor;
import nl.edia.xapi.annotation.Context;
import nl.edia.xapi.annotation.Statement;
import nl.edia.xapi.annotation.StatementObject;
import nl.edia.xapi.annotation.Statement.ValidationStyle;
import nl.edia.xapi.proxy.StatementMethodInterceptorTest.LeaningAnalyticsMethodInterceptorTestTarget;
import nl.edia.xapi.proxy.domain.Course;
import nl.edia.xapi.proxy.domain.Module;
import nl.edia.xapi.proxy.domain.User;

public class StatementAdvisorTest extends StatementAdvisor {

	private static class PrivateTest {
		public void doSomething0() {
		}

		@Statement(verb = "bla")
		public void doSomething1() {
		}

		@Statement(verb = "bla")
		public void doSomething2() {
		}
		@Statement(verb = "bla", validation=ValidationStyle.lenient)
		public void doSomething3() {
		}
		@Statement(verb = "bla")
		public void doSomething4(@Actor User user, @StatementObject Module module) {
		}
		@Statement(verb = "bla")
		public void doSomething5(@StatementObject Module module) {
		}
		@Statement(verb = "bla")
		public void doSomething6(@Actor User user) {
		}
		@Statement(verb = "")
		public void doSomething7(@Actor User user, @StatementObject Module module) {
		}
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void test() {
		getPointcut().getMethodMatcher().matches(null, null);
	}

	@Test
	public void testSomething0a() {
		assertFalse(getPointcut().getMethodMatcher().matches(getMethod("doSomething0"), null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSomething1() {
		assertFalse(getPointcut().getMethodMatcher().matches(getMethod("doSomething1"), null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSomething2() {
		assertFalse(getPointcut().getMethodMatcher().matches(getMethod("doSomething2"), null));
	}

	@Test
	public void testSomething3() {
		assertTrue(getPointcut().getMethodMatcher().matches(getMethod("doSomething3"), null));
	}

	@Test
	public void testSomething4() {
		assertTrue(getPointcut().getMethodMatcher().matches(getMethod("doSomething4"), null));
	}
	@Test(expected = IllegalArgumentException.class)
	public void testSomething5() {
		assertTrue(getPointcut().getMethodMatcher().matches(getMethod("doSomething5"), null));
	}
	@Test(expected = IllegalArgumentException.class)
	public void testSomething6() {
		assertTrue(getPointcut().getMethodMatcher().matches(getMethod("doSomething6"), null));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSomething7() {
		assertTrue(getPointcut().getMethodMatcher().matches(getMethod("doSomething7"), null));
	}
	
	@Test
	public void testSetterAndGetters() {
		StatementMethodInterceptor interceptor = Mockito.mock(StatementMethodInterceptor.class);
		assertNull(getAdvice());
		setInterceptor(interceptor);
		assertEquals(interceptor, getAdvice());
	}
	

	protected Method getMethod(String name) {
		Method m = null;
		Method[] methods = PrivateTest.class.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(name)) {
				m = method;
				break;
			}
		}
		return m;
	}

}
