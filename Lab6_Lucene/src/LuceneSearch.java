import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class LuceneSearch implements Functions {
	// Use Hashset to avoid duplicated ones
	static HashSet<String> query = new HashSet<String>();

	public static void main(String[] args) throws Exception {
		// read keywords
		ArrayList<String> keywords = new ArrayList<String>();
		System.out.println("1.Get " + args.length + " Keywords:");
		for (int i = 0; i < args.length; i++) {
			keywords.add(args[i]);
			System.out.println("  " + i + "):" + args[i]);
		}
		SearchKeywords(keywords);
		// Search based on title&author and year separately, then merge the results
		int numResultsToSkip = 1;
		int numResultsToReturn = query.size() - numResultsToSkip;
		ArrayList<String> baseSearchResult = basicSearch(query, numResultsToSkip, numResultsToReturn);
		
		int startYear=1985;
		int endYear=1990;
		ArrayList<String> yearSearchResult = RangeQuery(startYear, endYear);
		ArrayList<String> finalresult = new ArrayList<String>();
		for (String item : baseSearchResult) {
			if (yearSearchResult.contains(item)) {
				finalresult.add(item);
			}
		}
		System.out.println("4. If range from year "+startYear+" to "+endYear+", get " + finalresult.size() + " hits as follows:");
		for (int i = 0; i < finalresult.size(); i++) {
			System.out.println("  " + i + "): " + finalresult.get(i));
		}
	}

	public static void SearchKeywords(ArrayList<String> keywords) throws Exception {
		List<Article> articles = DBLP.getDatabase();
		StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
		IndexWriter w = new IndexWriter(index, config);
		// Filter database to create all indexes for "title" and "author"
		for (int i = 0; i < articles.size(); i++) {
			Document doc = new Document();
			doc.add(new TextField("title", articles.get(i).title, Field.Store.YES));
			doc.add(new TextField("author", articles.get(i).author.toString(), Field.Store.YES));
			doc.add(new IntField("year", Integer.parseInt(articles.get(i).year), Field.Store.YES));
			w.addDocument(doc);
		}
		w.close();
		int hitsPerPage = 1000;
		// Consider relationship between keywords as OR, and search each
		for (int i = 0; i < keywords.size(); i++) {
			String keyword = keywords.get(i);
			// *Situation1: If the keyword is for title*
			// Create a query
			Query qTitle = new QueryParser("title", standardAnalyzer).parse(keyword);

			// Search for title
			IndexReader readerForTitle = DirectoryReader.open(index);
			IndexSearcher searcherForTitle = new IndexSearcher(readerForTitle);
			TopScoreDocCollector collectorForTitle = TopScoreDocCollector.create(hitsPerPage);
			searcherForTitle.search(qTitle, collectorForTitle);
			ScoreDoc[] hitsForTitle = collectorForTitle.topDocs().scoreDocs;

			// Result for title
			ArrayList<String> resultForTitle = new ArrayList<String>();
			for (int m = 0; m < hitsForTitle.length; m++) {
				int Id = hitsForTitle[m].doc;
				Document document = searcherForTitle.doc(Id);
				resultForTitle.add(document.get("title"));
			}
			readerForTitle.close();
			for (int j = 0; j < resultForTitle.size(); j++) {
				query.add(resultForTitle.get(j));
			}

			// *Situation2: If the keyword is for author*
			// Create a query
			Query qAuthor = new QueryParser("author", standardAnalyzer).parse(keyword);

			// Search for author
			IndexReader readerForAuthor = DirectoryReader.open(index);
			IndexSearcher searcherForAuthor = new IndexSearcher(readerForAuthor);
			TopScoreDocCollector collectorForAuthor = TopScoreDocCollector.create(hitsPerPage);
			searcherForAuthor.search(qAuthor, collectorForAuthor);
			ScoreDoc[] hitsForAuthor = collectorForAuthor.topDocs().scoreDocs;

			// Result for author
			ArrayList<String> resultForAuthor = new ArrayList<String>();
			for (int m = 0; m < hitsForAuthor.length; m++) {
				int Id = hitsForAuthor[m].doc;
				Document document = searcherForAuthor.doc(Id);
				resultForAuthor.add(document.get("title"));
			}
			readerForAuthor.close();
			for (int j = 0; j < resultForAuthor.size(); j++) {
				query.add(resultForAuthor.get(j));
			}
		}
		// Print all results found
		System.out.println("2. Get all " + query.size() + " hits.");
	}

	// skip first several numbers of results and return the rest as required
	private static ArrayList<String> basicSearch(HashSet<String> query, int numResultsToSkip, int numResultsToReturn) {
		ArrayList<String> querylist = new ArrayList<String>();
		ArrayList<String> results = new ArrayList<String>();
		querylist.addAll(query);
		System.out.println("3.Skip " + numResultsToSkip + " hits, and get " + numResultsToReturn
				+ " hits returned as requirements:");
		for (int i = numResultsToSkip; i < (numResultsToSkip + numResultsToReturn); i++) {
			if (i < querylist.size()) {
				results.add(querylist.get(i));
				System.out.println("  " + i + ") " + querylist.get(i));
			} else {
				break;
			}
		}
		return results;
	}

	public static ArrayList<String> RangeQuery(int start, int end) throws Exception {
		List<Article> articles = DBLP.getDatabase();
		StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
		IndexWriter w = new IndexWriter(index, config);
		// Filter database to create all indexes for "title" and "author"
		for (int i = 0; i < articles.size(); i++) {
			Document doc = new Document();
			doc.add(new TextField("title", articles.get(i).title, Field.Store.YES));
			doc.add(new TextField("author", articles.get(i).author.toString(), Field.Store.YES));
			doc.add(new IntField("year", Integer.parseInt(articles.get(i).year), Field.Store.YES));
			w.addDocument(doc);
		}
		w.close();

		Query qYear = NumericRangeQuery.newIntRange("year", start, end, true, true);

		int hitsPerPage = 1000;

		IndexReader readerForYear = DirectoryReader.open(index);
		IndexSearcher searcherForYear = new IndexSearcher(readerForYear);
		TopScoreDocCollector collectorForYear = TopScoreDocCollector.create(hitsPerPage);
		searcherForYear.search(qYear, collectorForYear);
		ScoreDoc[] hitsForYear = collectorForYear.topDocs().scoreDocs;

		ArrayList<String> resultForYear = new ArrayList<String>();
		for (int i = 0; i < hitsForYear.length; ++i) {
			int Id = hitsForYear[i].doc;
			Document document = searcherForYear.doc(Id);
			resultForYear.add(document.get("title"));
		}
		readerForYear.close();
		return resultForYear;
	}

	@Override
	public SearchResult[] basicSearch(String query, int numResultsToSkip, int numResultsToReturn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResult[] spatialSearch(String query, SearchRegion region, int numResultsToSkip,
			int numResultsToReturn) {
		// TODO Auto-generated method stub
		return null;
	}

}
