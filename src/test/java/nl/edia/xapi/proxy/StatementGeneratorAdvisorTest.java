package nl.edia.xapi.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import nl.edia.xapi.annotation.StatementGenerator;

public class StatementGeneratorAdvisorTest extends StatementGeneratorAdvisor {

	public static class PrivateTest {
		public void doSomething0() {
		}

		@StatementGenerator(verb = "bla")
		public void doSomething1() {
		}

		@StatementGenerator(verb = "bla")
		public void doSomething2() {
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
	public void testSetterAndGetters() {
		StatementGeneratorMethodInterceptor interceptor = Mockito.mock(StatementGeneratorMethodInterceptor.class);
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
