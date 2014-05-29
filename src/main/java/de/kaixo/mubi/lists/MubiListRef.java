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

import java.util.ArrayList;
import java.util.List;

public class MubiListRef {
	private final String title;
	private final String url;
	private final int id;
	private final String owner;
	private final int numFans;
	private final List<MubiFilmRef> films = new ArrayList<>();

	public MubiListRef(String title, String url, int id, String owner,
			int numFans) {
		this.title = title;
		this.url = url;
		this.id = id;
		this.owner = owner;
		this.numFans = numFans;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getOwner() {
		return owner;
	}

	public String getUrl() {
		return url;
	}

	public int getNumFans() {
		return numFans;
	}

	public List<MubiFilmRef> getFilms() {
		return films;
	}

	public void addFilms(List<MubiFilmRef> films) {
		this.films.addAll(films);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MubiListRef [title=");
		builder.append(title);
		builder.append(", url=");
		builder.append(url);
		builder.append(", id=");
		builder.append(id);
		builder.append(", owner=");
		builder.append(owner);
		builder.append(", numFans=");
		builder.append(numFans);
		builder.append("]");
		return builder.toString();
	}
}
