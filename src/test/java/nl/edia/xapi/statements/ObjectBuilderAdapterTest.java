package nl.edia.xapi.statements;

import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gov.adlnet.xapi.model.IStatementObject;
import nl.edia.xapi.proxy.domain.Text;
import nl.edia.xapi.proxy.impl.TestObjectBuilder;

public class ObjectBuilderAdapterTest {

	/**
	 * builder
	 */
	private ObjectBuilderAdapter builder;
	@Before
	public void setUp() {
		builder = spy(new TestObjectBuilder());
	}
	
	@Test
	public void testIStatementObject() {
		builder.build(null, ObjectBuilderAdapterTest.class);
		builder.build(null, IStatementObject.class);
		verify(builder).buildActivity(isNull());
		Text text = new Text();
		text.setId(13L);
		String location = "http://somewhere.com/x";
		text.setLocation(location);
		IStatementObject build = builder.build(text, IStatementObject.class);
		verify(builder).buildActivity(text);
		Assert.assertNotNull(build);
		Assert.assertEquals(location, build.serialize().getAsJsonObject().get("id").getAsString());
	}

}
