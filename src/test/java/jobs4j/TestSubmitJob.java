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

import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jobs4j.threaded.JobManager;

class TestSubmitJob {

	private static JobManager jm = null;

	public class TestJob extends Job {
		public boolean ran = false;

		@Override
		public void execute() {
			try {
				Thread.sleep(1000);
				ran = true;
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	@BeforeAll
	public static void setup() {
		jm = JobManager.getInstance();
	}

	@Test
	public void testSubmit() {
		try {
			TestJob in = new TestJob();
			assert (in.ran == false);

			UUID id = jm.submit(in);

			TestJob out = (TestJob) jm.waitFor(id);
			assert (out.ran == true);

			assert (jm.isActive() == true);
		} catch (JobException | InterruptedException e) {
			fail("Job Exception: " + e.getLocalizedMessage());
		}
	}

	@AfterAll
	public static void close() {
		if (jm == null)
			fail("No JobManager there to close");
		jm.close();
		assert (jm.isActive() == false);
	}

}
