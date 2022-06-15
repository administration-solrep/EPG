/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     <a href="mailto:grenard@nuxeo.com">Guillaume Renard</a>
 *
 */

package org.nuxeo.functionaltests.pages.admincenter;

import org.nuxeo.functionaltests.Required;
import org.nuxeo.functionaltests.pages.workflow.WorkflowGraph;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @since 7.1
 */
public class WorkflowsPage extends AdminCenterBasePage {

    @Required
    @FindBy(id = "admin_workflow_models")
    protected WebElement adminWorkflowModelForm;

    @Required
    @FindBy(linkText = "Parallel document review")
    protected WebElement parallelDocumentReviewLink;

    @Required
    @FindBy(linkText = "Serial document review")
    protected WebElement serialDocumentReviewLink;

    public WorkflowsPage(WebDriver driver) {
        super(driver);
    }

    public WorkflowGraph getParallelDocumentReviewGraph() {
        parallelDocumentReviewLink.click();
        return asPage(WorkflowGraph.class);
    }

    public WorkflowGraph getSerialDocumentReviewGraph() {
        serialDocumentReviewLink.click();
        return asPage(WorkflowGraph.class);
    }

}
