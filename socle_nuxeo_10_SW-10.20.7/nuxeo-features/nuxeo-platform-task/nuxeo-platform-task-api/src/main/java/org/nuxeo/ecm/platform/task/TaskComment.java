/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     Nicolas Ulrich
 */

package org.nuxeo.ecm.platform.task;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 5.5
 */
public class TaskComment extends HashMap<String, Serializable> {

    private static final long serialVersionUID = 1L;

    public TaskComment(Map<String, Serializable> taskCommentMap) {
        super();
        this.putAll(taskCommentMap);
    }

    public TaskComment(String author, String text) {
        super();
        this.put(TaskConstants.TASK_COMMENT_AUTHOR_KEY, author);
        this.put(TaskConstants.TASK_COMMENT_TEXT_KEY, text);
        this.put(TaskConstants.TASK_COMMENT_CREATION_DATE_KEY, Calendar.getInstance());
    }

    public TaskComment(String author, String text, Date commentDate) {
        super();
        this.put(TaskConstants.TASK_COMMENT_AUTHOR_KEY, author);
        this.put(TaskConstants.TASK_COMMENT_TEXT_KEY, text);
        this.put(TaskConstants.TASK_COMMENT_CREATION_DATE_KEY, commentDate);
    }

    public String getAuthor() {
        return (String) this.get(TaskConstants.TASK_COMMENT_AUTHOR_KEY);
    }

    public String getText() {
        return (String) this.get(TaskConstants.TASK_COMMENT_TEXT_KEY);
    }

    public Calendar getCreationDate() {
        return (Calendar) this.get(TaskConstants.TASK_COMMENT_CREATION_DATE_KEY);
    }

}
