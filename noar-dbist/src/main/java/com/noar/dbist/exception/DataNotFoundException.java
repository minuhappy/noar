/**
 * Copyright 2011-2012 the original author or authors.
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
package com.noar.dbist.exception;

/**
 * @author Steve M. Jung
 * @since 2012. 2. 19. (version 0.0.1)
 */
@SuppressWarnings("serial")
public class DataNotFoundException extends DbistException {
	public DataNotFoundException() {
		super();
	}
	public DataNotFoundException(String message) {
		super(message);
	}
	public DataNotFoundException(Throwable cause) {
		super(cause);
	}
	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
