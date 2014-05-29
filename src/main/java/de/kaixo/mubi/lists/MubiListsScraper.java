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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MubiListsScraper {

	private final static String MUBI_BASE_URL = "https://mubi.com";
	private final static String MUBI_LISTS_BASE_URL = MUBI_BASE_URL
			+ "/lists?utf8=✓&sort=popularity";

	private final static ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	public static void main(String ars[]) throws XMLStreamException,
			FactoryConfigurationError, IOException {
		for (int page = 1; page <= 10; page++) {
			System.out.println("Fetching page " + page);
			URL url = new URL(MUBI_LISTS_BASE_URL + "&page=" + page);
			List<MubiListRef> lists = MubiListsReader.getInstance()
					.readMubiLists(url);
			for (MubiListRef list : lists) {
				System.out.println("  Fetching list " + list.getTitle());
				List<MubiFilmRef> filmList = MubiListsReader.getInstance()
						.readMubiFilmList(
								new URL(MUBI_BASE_URL + list.getUrl()));
				list.addFilms(filmList);
			}

			File outfile = new File("output", "mubi-lists-page-" + String.format("%04d", page) + ".json");
			System.out.println("Writing "+ outfile.getName());
			mapper.writeValue(outfile,
					lists);
		}
	}
}
