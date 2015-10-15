import java.util.ArrayList;
import java.util.List;


public class Article {
	public String mdate;
	public String key;
	public List<String> author = new ArrayList<>();
	public String title;
	public String pages;
	public String year;
	public String volume;
	public String journal;
	public String URL;
	
	public String getMdate() {
		return mdate;
	}

	public void setMdate(String mdate) {
		this.mdate = mdate;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getAuthor() {
		return author;
	}

	public void setAuthor(List<String> author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("mdate:  " + mdate + "\n");
		sb.append("key:  " + key + "\n");
		sb.append("author:  ");
		
		for(String temp : author){
			sb.append(temp + ",");
		}
		sb.append("\n");
		sb.append("title:  " + title + "\n");
		sb.append("pages:  " + pages + "\n");
		sb.append("year:  " + year + "\n");
		sb.append("volume:  " + volume + "\n");
		sb.append("journal:  " + journal + "\n");
		sb.append("URL:  " + URL + "\n");
		sb.append("------------------------------" + "\n");
		return sb.toString();
	}
}
