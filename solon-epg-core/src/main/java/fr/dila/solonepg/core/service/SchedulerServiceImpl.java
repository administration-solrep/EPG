package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.dto.JobDTO;
import fr.dila.solonepg.api.dto.JobDTOImpl;
import fr.dila.solonepg.api.service.SchedulerService;
import fr.dila.st.core.service.STServiceLocator;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.nuxeo.common.Environment;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SchedulerServiceImpl implements SchedulerService {

    @Override
    public List<JobDTO> findAllCronJob() {
        List<JobDTO> listJobDTO = new ArrayList<>();

        Environment env = Environment.getDefault();
        File dir = env.getConfig();
        String[] names = dir.list();

        if (names != null) {
            for (String name : names) {
                if (name.endsWith("scheduler-config.xml")) {
                    //pas performant mais on va les chercher que si administrator et 1 seule fois par session
                    File file = new File(dir, name);
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder;
                    try {
                        dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(file);
                        NodeList nList = doc.getElementsByTagName("schedule");
                        for (int i = 0; i < nList.getLength(); i++) {
                            Node nNode = nList.item(i);
                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element eElement = (Element) nNode;
                                JobDTO jobDTO = new JobDTOImpl();
                                jobDTO.setName(eElement.getAttribute("id"));
                                nNode.getNextSibling();
                                jobDTO.setEventName(getTagValue("eventId", eElement));
                                listJobDTO.add(jobDTO);
                            }
                        }
                    } catch (Exception e) {
                        throw new NuxeoException(e);
                    }
                }
            }
        }

        return listJobDTO;
    }

    private static String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = nlList.item(0);
        return nValue.getNodeValue();
    }

    @Override
    public void execute(JobDTO jobDTO, CoreSession session) {
        EventProducer eventProducer = STServiceLocator.getEventProducer();
        Map<String, Serializable> eventProperties = new HashMap<>();
        InlineEventContext inlineEventContext = new InlineEventContext(
            session,
            session.getPrincipal(),
            eventProperties
        );
        eventProducer.fireEvent(inlineEventContext.newEvent(jobDTO.getEventName()));
    }
}
