# xapi-annotations

This project integrates the https://github.com/adlnet/jxapi library into a spring application with annotations.

To integrate make use of this library, the following steps are required.

## Include the dependecy
Add the library to your maven project pom.xml, mind to take the latest stable version.
```
	<dependency>
		<groupId>nl.edia</groupId>
		<artifactId>xapi-annotations</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
```


## Set up the spring proxy
Configure the spring proxy inside one of you spring XML configuration files
```
	<bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator">
		<property name="proxyTargetClass" value="true" />
	</bean>
	<bean class="nl.edia.xapi.proxy.StatementAdvisor">
		<property name="interceptor">
		  <bean class="nl.edia.xapi.proxy.StatementMethodInterceptor"/>
		</property>
	</bean>
	<bean class="nl.edia.xapi.proxy.StatementGeneratorAdvisor">
		<property name="interceptor">
		  <bean class="nl.edia.xapi.proxy.StatementGeneratorMethodInterceptor"/>
		</property>
	</bean>
````
or use the annotation based configurtion
```
	@Configuration
	@ComponentScan(basePackages = "nl.edia.xapi.proxy.impl")
	public class MyConfiguration {
		@Bean public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
			return new DefaultAdvisorAutoProxyCreator();
		}
	}
```

## Place some annotations
Put the annotation on the business methods in your application, the @Actor and @StatementObject annotations are required.
```
	@Statement(verb = "experienced")
	public void readText(@Actor User user, @StatementObject Text text, @Context Course course) {
		// ...
	}
```

## Implement the ObjectBuilder for your domain
Implement your own ObjectBuilder from business objects to XAPI entities, expose the builder as a spring bean.
```
	@Component
	public class MyObjectBuilder extends ObjectBuilderAdapter {
	@Override
	protected Actor buildActor(Object value) {
		Agent actor = new Agent();
		// Populate the actor properties with the value object.
		return actor;
	}

	@Override
	protected IStatementObject buildActivity(Object value) {
		IStatementObject activity = new Activity();
		// Populate the activity properties with the value object.
		return activity;
	}
	}
```

## Configure the endpoint
Configure the StatementClient as statement endpoint
```
	<bean class="gov.adlnet.xapi.client.StatementClient">
		<constructor-arg value="http://example.com/xapi/"/>
		<constructor-arg value="username"/>
		<constructor-arg value="password"/>
	</bean>
```
Or with annotations
```
	@Bean StatementClient statementClient() {
		return new StatementClient("http://example.com/xapi/", "username", "password");
	}
```
