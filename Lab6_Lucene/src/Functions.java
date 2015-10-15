
public interface Functions {
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, int numResultsToReturn);
	
	public SearchResult[] spatialSearch(String query, SearchRegion region, int numResultsToSkip, int numResultsToReturn);
}
