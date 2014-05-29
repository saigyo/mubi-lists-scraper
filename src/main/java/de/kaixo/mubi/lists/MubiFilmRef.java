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

public class MubiFilmRef {
	private String title;
	private String director;
	private int id;
	private String url;
	private int position;

	public MubiFilmRef(String title, String director, int id, String url,
			int position) {
		this.title = title;
		this.director = director;
		this.id = id;
		this.url = url;
		this.position = position;
	}

	public String getDirector() {
		return director;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public int getPosition() {
		return position;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MubiFilmRef [title=");
		builder.append(title);
		builder.append(", director=");
		builder.append(director);
		builder.append(", id=");
		builder.append(id);
		builder.append(", url=");
		builder.append(url);
		builder.append(", position=");
		builder.append(position);
		builder.append("]");
		return builder.toString();
	}
}
