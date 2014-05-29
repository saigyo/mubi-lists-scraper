package de.kaixo.mubi.lists;

public class MubiFilmRef {
	private String title;
	private String director;
	private int id;
	private String url;
	private int position;

	public MubiFilmRef(String title, String director, int id, String url, int position) {
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
