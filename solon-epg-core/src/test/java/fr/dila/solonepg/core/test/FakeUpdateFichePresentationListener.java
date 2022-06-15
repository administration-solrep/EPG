package fr.dila.solonepg.core.test;

import java.util.ArrayList;
import java.util.List;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;

public class FakeUpdateFichePresentationListener implements EventListener {
    public static List<Event> events = new ArrayList<>();

    @Override
    public void handleEvent(Event event) {
        events.add(event);
    }
}
