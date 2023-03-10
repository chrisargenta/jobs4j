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

import java.util.UUID;

public abstract class JobObserver {

	// Status updates from jobs must be implemented by Observer

	protected abstract void onStatus(UUID id, JobStatus status);

	protected abstract void onPercentDone(UUID id, double percentDone);

	public abstract void onCompletion(Job job);

	// Messaging from jobs defaults to outputting simple messages to console
	// Consider if logging framework might be a better idea for your application

	public void onMessage(UUID id, String message) {
		System.out.println(Thread.currentThread().getName() + "\t" + id + "\t" + message);
	}

	public void onException(UUID id, Exception e) {
		System.err.println(Thread.currentThread().getName() + "\t" + id + "\t" + e.getLocalizedMessage());
	}

}