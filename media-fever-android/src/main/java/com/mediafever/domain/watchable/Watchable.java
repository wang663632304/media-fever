package com.mediafever.domain.watchable;

import java.util.Date;
import java.util.List;
import com.jdroid.android.domain.Entity;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.domain.UriFileContent;
import com.jdroid.java.utils.DateUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class Watchable extends Entity {
	
	private String name;
	private FileContent image;
	private String overview;
	private List<String> actors;
	private List<String> genres;
	private Date releaseDate;
	
	public Watchable(Long id, String name, String imageURL, String overview, List<String> actors, List<String> genres,
			Date releaseDate) {
		super(id);
		this.name = name;
		image = new UriFileContent(imageURL);
		this.overview = overview;
		this.actors = actors;
		this.genres = genres;
		this.releaseDate = releaseDate;
	}
	
	public String getName() {
		return name;
	}
	
	public FileContent getImage() {
		return image;
	}
	
	public String getOverview() {
		return overview;
	}
	
	public List<String> getActors() {
		return actors;
	}
	
	public List<String> getGenres() {
		return genres;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	
	public Integer getReleaseYear() {
		return releaseDate != null ? DateUtils.getYear(releaseDate) : null;
	}
}
