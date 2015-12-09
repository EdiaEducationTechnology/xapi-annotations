package nl.edia.xapi.statements;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.Agent;
import gov.adlnet.xapi.model.Attachment;
import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Result;
import gov.adlnet.xapi.model.Verb;

public class DefaultStatementBuilderTest extends DefaultStatementBuilder {
	
	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = -1946681591319751611L;
	private DefaultStatementBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = spy(new DefaultStatementBuilder());
	}

	@Test
	public void test() {
		assertNotNull(builder.build(null, null, null, null, null, null, null));
		assertNotNull(builder.build(mock(Agent.class), mock(Verb.class), mock(IStatementObject.class), null, null, null, null));
		assertNotNull(builder.build(mock(Agent.class), mock(Verb.class), mock(IStatementObject.class), mock(Result.class), mock(Context.class), mock(Actor.class), new ArrayList<>(Arrays.asList(mock(Attachment.class)))));
	}

}
