/* 
   Copyright 2014 Markus Ackermann <markus@kaixo.de>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package de.kaixo.mubi.lists;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.junit.Ignore;
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

	@Ignore
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
