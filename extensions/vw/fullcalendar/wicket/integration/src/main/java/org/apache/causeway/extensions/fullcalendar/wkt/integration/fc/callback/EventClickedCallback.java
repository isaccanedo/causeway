/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.causeway.extensions.fullcalendar.wkt.integration.fc.callback;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.Request;

import org.apache.causeway.extensions.fullcalendar.wkt.integration.fc.CalendarResponse;
import org.apache.causeway.extensions.fullcalendar.wkt.integration.fc.Event;
import org.apache.causeway.extensions.fullcalendar.wkt.integration.fc.EventSource;

public abstract class EventClickedCallback
extends AbstractAjaxCallback
implements CallbackWithHandler {

    private static final long serialVersionUID = 1L;

    @Override
	protected String configureCallbackScript(final String script, final String urlTail) {
		return script.replace(urlTail, "&eventId=\"+info.event.id+\"&sourceId=\"+info.event.source.id"
			+ "+\"");
	}

	@Override
	public String getHandlerScript() {
		return "function(info) { " + getCallbackScript() + "}";
	}

	@Override
	protected void respond(final AjaxRequestTarget target) {
		Request r = getCalendar().getRequest();
		String eventId = r.getRequestParameters().getParameterValue("eventId").toString();
		String sourceId = r.getRequestParameters().getParameterValue("sourceId").toString();

		EventSource source = getCalendar().getEventSource(sourceId);
		Event event = source.getEventById(eventId);

		onClicked(new ClickedEvent(source, event), new CalendarResponse(getCalendar(), target));
	}

	protected abstract void onClicked(ClickedEvent event, CalendarResponse response);
}
