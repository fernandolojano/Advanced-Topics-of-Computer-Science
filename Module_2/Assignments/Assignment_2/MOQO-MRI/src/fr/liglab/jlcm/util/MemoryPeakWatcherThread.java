/*
	This file is part of jLCM - see https://github.com/martinkirch/jlcm/
	
	Copyright 2013,2014 Martin Kirchgessner, Vincent Leroy, Alexandre Termier, Sihem Amer-Yahia, Marie-Christine Rousset, Université Joseph Fourier and CNRS
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	 http://www.apache.org/licenses/LICENSE-2.0
	 
	or see the LICENSE.txt file joined with this program.
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/


package fr.liglab.jlcm.util;

public class MemoryPeakWatcherThread extends Thread {
	/**
	 * garbage-collect-n-peek delay, in milliseconds
	 */
	private static final long GC_AND_CHECK_DELAY = 15000;
	
	/**
	 * In bytes
	 */
	private long maxUsedMemory = 0; 
	
	private Runtime runtime;
	
	public long getMaxUsedMemory() {
		return maxUsedMemory;
	}
	
	@Override
	public void run() {
		this.runtime = Runtime.getRuntime();
		this.maxUsedMemory = 0;
		this.peek();
		
		while (true) {
			try {
				Thread.sleep(GC_AND_CHECK_DELAY);
				this.peek();
			} catch (InterruptedException e) {
				this.peek();
				return;
			}
		}
	}
	
	private void peek() {
		this.runtime.gc();
		
		final long used = this.runtime.totalMemory() - this.runtime.freeMemory();
		
		if (used > this.maxUsedMemory) {
			this.maxUsedMemory = used;
		}
	}
}