/**
 * P6Spy
 *
 * Copyright (C) 2002 - 2018 P6Spy
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
package com.codingapi.txlcn.jdbcproxy.p6spy.common;

/**
 * {@link Hasher} using {@code class.hashCode()} for object hash computing.
 * 
 * @author Peter Butkovic
 */
public class ClassHasher implements Hasher {

	/* (non-Javadoc)
	 * @see com.p6spy.engine.commons.Hasher#getHashCode(java.lang.Object)
	 */
	@Override
	public int getHashCode(Object object) {
		return object.getClass().hashCode();
	}

}
