/***********************************************************************************************************************
 *
 * Copyright (C) 2010 by the Stratosphere project (http://stratosphere.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/
package eu.stratosphere.sopremo.packages;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

import eu.stratosphere.sopremo.operator.Name;

/**
 * @author arv
 */
public class BuiltinUtil {
	public static String getName(Object element, IRegistry<?> registry) {
		final Class<? extends Object> clazz = element.getClass();
		// if this a non-anonymous class, it should be self-descriptive
		Name nameAnnotation = clazz.getAnnotation(Name.class);
		if (nameAnnotation != null)
			return registry.getName(nameAnnotation);

		if (!clazz.isAnonymousClass())
			return clazz.getSimpleName();

		// anonymous inner class
		// find the field and check if there is an annotation
		final Field[] fields = clazz.getDeclaringClass().getFields();
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()))
				try {
					if (field.get(null).getClass() == clazz) {
						nameAnnotation = field.getAnnotation(Name.class);
						if (nameAnnotation != null)
							return registry.getName(nameAnnotation);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
		}

		// last chance, is it in already registered?
		final Set<String> allFunctions = registry.keySet();
		for (String function : allFunctions) 
			// use equals to find the original object in case of cloned instances
			if (registry.get(function).equals(element)) 
				return function;
		
		// fall through
		return "unknown";
	}
}