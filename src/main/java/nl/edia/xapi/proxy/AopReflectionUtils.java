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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;

/**
 * Class holding some AOP helper methods.
 * @author Roland Groen <roland@edia.nl>
 *
 */
public final class AopReflectionUtils {

	private AopReflectionUtils() {
	}

	/**
	 * Class to resolve null semantics in parameters. 
	 * this == null means none found, 
	 * this.value = null found but parameter value not present.
	 * @author Roland Groen <roland@edia.nl>
	 *
	 * @param <T>
	 */
	public static class Parameter<T> {
		/**
		 * Value holder.
		 */
		private final T value;

		/**
		 * Default constructor.
		 * @param value
		 */
		public Parameter(T value) {
			this.value = value;
		}
	}

	/**
	 * Find a parameter value.
	 * @param invocation the given invocation. 
	 * @param annotationClazz the annotation class.
	 * @param returnValue the value returned by the method call.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Parameter<T> findParameterValue(MethodInvocation invocation, Class<T> annotationClazz, Object returnValue) {
		Class<?> returnType = invocation.getMethod().getReturnType();
		if (annotationClazz.isAssignableFrom(returnType)) {
			return new Parameter<T>((T) returnValue);
		}

		Class<?>[] parameterTypes = invocation.getMethod().getParameterTypes();
		int index = 0;
		for (Class<?> clazz : parameterTypes) {
			Object[] arguments = invocation.getArguments();
			// a instanceof B
			// B.class.isAssignableFrom(a.getClass())
			if (annotationClazz.isAssignableFrom(clazz)) {
				return new Parameter<T>((T) arguments[index]);
			}

			index++;
		}
		return null;
	}

	/**
	 * Gets the classes of the method parameter.
	 * @param invocation the given invocation. 
	 * @param returnValue the value returned by the method call.
	 * @return a set of classes;
	 */
	public static Set<Class<?>> getParameterClasses(MethodInvocation invocation, Object returnValue) {
		// Use the getParameterClassesAndValues to get the matchin classes
		Map<Class<?>, Object> values = getParameterClassesAndValues(invocation, returnValue);
		return values.keySet();
	}

	/**
	 * Get a map of values as class > value map.
	 * @param invocation the given invocation. 
	 * @param returnValue the value returned by the method call.
	 * @return the class > value map.
	 */
	public static Map<Class<?>, Object> getParameterClassesAndValues(MethodInvocation invocation, Object returnValue) {
		Map<Class<?>, Object> values = new HashMap<>();
		Method method = invocation.getMethod();
		List<Class<?>> parameterTypes = new ArrayList<>(Arrays.asList(method.getParameterTypes()));
		parameterTypes.add(0, method.getReturnType());
		for (Class<?> clazz : parameterTypes) {
			Parameter<?> value = findParameterValue(invocation, clazz, returnValue);
			if (value != null) {
				values.put(clazz, value.value);
			}
		}
		
		return values;
	}

	/**
	 * Gets the first index of the annotation of the parameters.
	 * @param method the method.
	 * @param type the annotation type to search for.
	 * @return the index, -1 if none found.
	 */
	public static int findParameterIndex(Method method, Class<? extends Annotation> type) {
		int index = 0;
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		for (Annotation[] annotations : parameterAnnotations) {
			for (Annotation annotation : annotations) {
				if (type.equals(annotation.annotationType())) {
					return index;
				}
			}
			index++;
		}
		return -1;
	}

}
