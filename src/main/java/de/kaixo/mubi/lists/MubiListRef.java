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
