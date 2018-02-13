/**
 * Copyright 2011-2013 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.noar.dbist.annotation;

/**
 * Defines a type or personality of a column
 * 
 * @author Steve M. Jung
 * @since 2012. 2. 18. (version 1.0.0)
 */
public class GenerationRule {
	public static final String NONE = "";

	/**
	 * ID_GENERATION_STRATEGY : Generate UUID
	 */
	public static final String UUID = "uuid";

	/**
	 * ID_GENERATION_STRATEGY : Increase Sequence
	 */
	public static final String AUTO_INCREMENT = "auto-increment";

	/**
	 * ID_GENERATION_STRATEGY : Union Unique Key
	 */
	public static final String MEANINGFUL = "meaningful";
	
	/**
	 * ID_GENERATION_STRATEGY : Complex Key - Primary Key Field Count is two or more
	 */
	public static final String COMPLEX_KEY = "complex-key";
}
