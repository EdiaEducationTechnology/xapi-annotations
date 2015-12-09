package nl.edia.xapi.statements;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import gov.adlnet.xapi.client.StatementClient;

public class NullObjectBuilderTest {

	@Test
	public void test() {
		StatementClient statementClient = Mockito.mock(StatementClient.class);
		DefaultStatementClientFactory factory = new DefaultStatementClientFactory();
		assertNull(factory.build(null, null));
		factory.setStatementClient(statementClient);
		assertEquals(statementClient, factory.build(null, null));
		 

		factory = new DefaultStatementClientFactory(statementClient);
		assertEquals(statementClient, factory.build(null, null));
}

}
