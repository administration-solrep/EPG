/*
 * (C) Copyright 2006-2010 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     bstefanescu
 */
package org.nuxeo.shell.automation.cmds;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.shell.Command;
import org.nuxeo.shell.Context;
import org.nuxeo.shell.Parameter;
import org.nuxeo.shell.ShellConsole;
import org.nuxeo.shell.automation.DocumentHelper;
import org.nuxeo.shell.automation.RemoteContext;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
@Command(name = "pwd", help = "Print the current context document")
public class Pwd implements Runnable {

    @Context
    protected RemoteContext ctx;

    @Parameter(name = "-s", hasValue = false, help = "Use this flag to show the context documents stack")
    protected boolean stack = false;

    public void run() {
        ShellConsole console = ctx.getShell().getConsole();

        if (stack) {
            for (Document doc : ctx.getStack()) {
                DocumentHelper.printPath(console, doc);
            }
        } else {
            DocumentHelper.printPath(console, ctx.getDocument());
        }
    }
}
