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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import gov.adlnet.xapi.client.StatementClient;

public abstract class AstractMethodInterceptor implements MethodInterceptor, DisposableBean, ApplicationContextAware {

	/**
	 * A reference to the log facility.
	 */
	private static final Log LOG = LogFactory.getLog(AstractMethodInterceptor.class);

	/**
	 * The aSync=true setting will cause the messages to be sent on a seperate thread. 
	 */
	protected boolean aSync = true;

	/**
	 * A reference to the {@link ApplicationContext}.
	 */
	protected ApplicationContext applicationContext;

	/**
	 * The execution service.
	 */
	protected ExecutorService executorService = Executors.newSingleThreadExecutor();

	/**
	 * Creates a new class instance, null of it fails for some reason.
	 * @param clazz the class to instantiate.
	 * @return the new class instance, null of it fails for some reason.
	 */
	protected <T> T instantiate(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			LOG.error("Problem with the ObjectBuilder configuration", e);
		} catch (IllegalAccessException e) {
			LOG.error("Problem with the ObjectBuilder configuration", e);
		}
		return null;
	}

	/**
	 * Send the statement to the LRS system. Any errors are logged but not thrown.
	 * @param client the client to connect to.
	 * @param statement the statement to be sent.
	 */
	public void send(final StatementClient client, final gov.adlnet.xapi.model.Statement statement) {
		Assert.notNull(client, "The StatementClient cannot be null");
		Assert.notNull(statement, "The Statement cannot be null");
		if (aSync) {
			executorService.execute(new Runnable() {
				/* (non-Javadoc)
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					try {
						client.postStatement(statement);
					} catch (IOException e) {
						LOG.warn("Cannot send xApi message", e);
					}

				}
			});
		} else {
			try {
				client.postStatement(statement);
			} catch (IOException e) {
				LOG.warn("Cannot send xApi message", e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public boolean isaSync() {
		return aSync;
	}

	public void setaSync(boolean aSync) {
		this.aSync = aSync;
	}
	
	public void destroy() throws Exception {
		if (executorService != null) {
			executorService.shutdownNow();
			executorService.awaitTermination(60, TimeUnit.SECONDS);
		}
	}

}