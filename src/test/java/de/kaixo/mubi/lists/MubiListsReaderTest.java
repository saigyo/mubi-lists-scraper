package de.kaixo.mubi.lists;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MubiListsReaderTest {

	private final static ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	@Test
	public void testReadMubiLists() throws Exception {
		InputStream is = getResourceAsStream("example-lists-page.xml");
		List<MubiListRef> readMubiLists = MubiListsReader.getInstance()
				.readMubiLists(is);
		mapper.writeValue(System.out, readMubiLists);
	}

	@Test
	public void testReadMubiListFromUrl() throws Exception {
		URL url = new URL(
				"https://mubi.com/lists?utf8=✓&sort=popularity&page=1");
		List<MubiListRef> readMubiLists = MubiListsReader.getInstance()
				.readMubiLists(url);
		mapper.writeValue(System.out, readMubiLists);
	}

	@Test
	public void testReadMubiFilmList() throws Exception {
		InputStream is = getResourceAsStream("example-list-page.xml");
		List<MubiFilmRef> readMubiFilmList = MubiListsReader.getInstance()
				.readMubiFilmList(is);
		mapper.writeValue(System.out, readMubiFilmList);
	}

	@Test
	public void testReadMubiListsAndFilms() throws Exception {
		InputStream is = getResourceAsStream("example-lists-page.xml");
		List<MubiListRef> readMubiLists = MubiListsReader.getInstance()
				.readMubiLists(is);
		MubiListRef firstList = readMubiLists.get(0);
		InputStream is1 = getResourceAsStream("example-list-page.xml");
		List<MubiFilmRef> readMubiFilmList = MubiListsReader.getInstance()
				.readMubiFilmList(is1);
		firstList.addFilms(readMubiFilmList);
		mapper.writeValue(System.out, firstList);
	}

	private InputStream getResourceAsStream(String fileName) {
		return ClassLoader.getSystemClassLoader().getResourceAsStream(
				getClass().getPackage().getName().replace('.', '/') + "/"
						+ fileName);
	}
}
