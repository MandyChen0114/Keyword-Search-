
public class SearchResult {
	private String title;
	private String author;
    
	public SearchResult() {}
    
	public SearchResult(String itemId, String name) {
		this.title = itemId;
		this.author = name;
	}
    
	public String getItemId() {
		return title;
	}
	
	public void setItemId(String itemId) {
		this.title = itemId;
	}
	
	public String getName() {
		return author;
	}
	
	public void setName(String name) {
		this.author = name;
	}
}
