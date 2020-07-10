/*
 * (C) Copyright 2007-2010 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Florent Guillaume
 */
package fr.dila.solonepg.elastic.batch;

import org.nuxeo.ecm.platform.scheduler.core.EventJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

/**
 * Spécialisation non concurrente du job
 */
@DisallowConcurrentExecution
public class DisallowConcurrentExecutionEventJob extends EventJob implements InterruptableJob {

	private volatile Thread jobThread;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		this.jobThread = Thread.currentThread();
		super.execute(context);
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		// when quartz is stopped, interrupt running thread
		if (this.jobThread != null) {
			jobThread.interrupt();
		}
	}
}
