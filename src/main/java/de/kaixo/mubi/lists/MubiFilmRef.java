package de.kaixo.mubi.lists;

public class MubiFilmRef {
	private String title;
	private String director;
	private String id;
	private String url;

	public MubiFilmRef(String title, String director, String id, String url) {
		this.title = title;
		this.director = director;
		this.id = id;
		this.url = url;
	}

	public String getDirector() {
		return director;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
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
		builder.append("]");
		return builder.toString();
	}
}
