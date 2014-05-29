package de.kaixo.mubi.lists;

import java.io.*;
import java.net.URL;
import java.util.*;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;

public enum MubiListsReader {
	INSTANCE;

	private final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	{
		inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
		inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		inputFactory.setProperty(
				XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
		inputFactory.setProperty(
				XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
	}

	public static MubiListsReader getInstance() {
		return INSTANCE;
	}

	public List<MubiListRef> readMubiLists(URL url) throws XMLStreamException,
			FactoryConfigurationError, IOException {
		return readMubiLists(url.openStream());
	}

	public List<MubiListRef> readMubiLists(InputStream is)
			throws XMLStreamException, FactoryConfigurationError, IOException {
		List<MubiListRef> result = new ArrayList<>();

		try {
			XMLStreamReader reader = inputFactory.createXMLStreamReader(is);

			while (reader.hasNext()) {
				int event = reader.next();
				if (event == XMLEvent.START_ELEMENT
						&& "div".equals(reader.getLocalName())
						&& "item".equals(reader.getAttributeValue("", "class"))) {
					result.add(readListRef(reader));
				}
			}
		} finally {
			is.close();
		}

		return result;
	}

	public List<MubiFilmRef> readMubiFilmList(URL url)
			throws XMLStreamException, FactoryConfigurationError, IOException {
		return readMubiFilmList(url.openStream());
	}
	
	public List<MubiFilmRef> readMubiFilmList(InputStream is)
			throws XMLStreamException, FactoryConfigurationError, IOException {
		List<MubiFilmRef> result = new ArrayList<>();

		try {
			XMLStreamReader reader = inputFactory.createXMLStreamReader(is);

			while (reader.hasNext()) {
				int event = -1;
				try {
					event = reader.next();
				} catch (XMLStreamException e) {
					continue;
				}
				if (event == XMLEvent.START_ELEMENT
						&& "ul".equals(reader.getLocalName())
						&& "listomania".equals(reader.getAttributeValue("",
								"class"))) {
					break;
				}
			}

			while (reader.hasNext()) {
				int event = reader.next();
				if (event == XMLEvent.START_ELEMENT
						&& "li".equals(reader.getLocalName())) {
					String id = reader.getAttributeValue("", "id");
					if (id != null && id.startsWith("film")) {
						result.add(readFilmRef(reader));
					}
				} else if (event == XMLEvent.END_ELEMENT
						&& "ul".equals(reader.getLocalName())) {
					break;
				}
			}
		} finally {
			is.close();
		}

		return result;
	}

	private MubiFilmRef readFilmRef(XMLStreamReader reader)
			throws XMLStreamException {
		String id = reader.getAttributeValue("", "id")
				.replaceFirst("film_", "");
		String url = "";
		String title = "";
		String director = "";
		while (reader.hasNext()) {
			int event = reader.next();
			if (event == XMLEvent.START_ELEMENT
					&& "a".equals(reader.getLocalName())) {
				url = reader.getAttributeValue("", "href");
				title = reader.getElementText();
			} else if (event == XMLEvent.START_ELEMENT
					&& "div".equals(reader.getLocalName())
					&& "director".equals(reader.getAttributeValue("", "class"))) {
				director = reader.getElementText();
			} else if (event == XMLEvent.END_ELEMENT
					&& "li".equals(reader.getLocalName())) {
				break;
			}
		}
		return new MubiFilmRef(title, director, id, url);
	}

	private MubiListRef readListRef(XMLStreamReader reader)
			throws XMLStreamException {
		String id = reader.getAttributeValue("", "data-item-id");
		String title = "";
		String url = "";
		String owner = "";
		int numFans = 0;
		while (reader.hasNext()) {
			int event = reader.next();
			if (XMLEvent.START_ELEMENT == event
					&& "a".equals(reader.getLocalName())) {
				url = reader.getAttributeValue("", "href");
			} else if (XMLEvent.START_ELEMENT == event
					&& "span".equals(reader.getLocalName())
					&& "title".equals(reader.getAttributeValue("", "class"))) {
				title = reader.getElementText();
			} else if (XMLEvent.START_ELEMENT == event
					&& "span".equals(reader.getLocalName())
					&& "owner".equals(reader.getAttributeValue("", "class"))) {
				owner = reader.getElementText();
			} else if (XMLEvent.START_ELEMENT == event
					&& "div".equals(reader.getLocalName())
					&& "overlay".equals(reader.getAttributeValue("", "class"))) {
				reader.next();
				numFans = Integer.parseInt(reader.getText());
			} else if (XMLEvent.END_ELEMENT == event
					&& "a".equals(reader.getLocalName())) {
				break;
			}
		}
		return new MubiListRef(title, url, id, owner, numFans);
	}
}
