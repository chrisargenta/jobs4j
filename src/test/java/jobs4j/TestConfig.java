/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package jobs4j;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import jobs4j.utils.Config;

class TestConfig {

	@Test
	void test() {
		Config config = Config.getInstance();
		if (config == null)
			fail("Config.getInstance returned null");

		try {
			Integer n = Integer.parseInt(config.get(Config.PROPERTY_LOCAL_THREAD_COUNT));
			if (n < 1 || n > 9999)
				fail("Value (" + n + ") out of reasonable range of 1-9999");

		} catch (NumberFormatException | IOException e) {
			fail(e.getMessage());
		}
	}

}
