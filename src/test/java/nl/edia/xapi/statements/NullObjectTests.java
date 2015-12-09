package nl.edia.xapi.statements;

import org.junit.Before;
import org.junit.Test;

public class NullObjectTests {

	@Before
	public void setUp() throws Exception {
	}

	@Test(expected = InstantiationException.class)
	public void testNullObjectBuilder() throws Exception {
		Class.forName(NullObjectBuilder.class.getName()).newInstance();
	}

	@Test(expected = InstantiationException.class)
	public void testNull() throws Exception {
		Class.forName(NullStatementGenerator.class.getName()).newInstance();
	}
	

}
