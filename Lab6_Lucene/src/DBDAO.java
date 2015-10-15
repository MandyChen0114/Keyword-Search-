
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBDAO {

	private Connection connection;
	
	private String INSERT_PUBLICATION = "INSERT INTO publication values (?, ?, ?, ?, ?, ?, ?, ?)";
	private String INSERT_PUBLICATION_AUTHOR = "INSERT INTO publication_author values (?, ?)";
	private String FIND_BY_AUTHOR = "SELECT * FROM publication_author PA, publication P WHERE PA.title = P.title AND author = ?";
	private String FIND_BY_YEAR = "SELECT * FROM publication_author PA, publication P WHERE PA.title = P.title AND year > ?";
	private String FIND_BY_JOUNRAL = "SELECT * FROM publication_author PA, publication P WHERE PA.title = P.title AND journal = ?";
	private String FIND_BY_TITLE = "SELECT * FROM publication_author PA, publication P WHERE PA.title = P.title AND P.title LIKE ?";
	private String FIND_ALL = "SELECT * FROM publication_author PA , publication P WHERE PA.title = P.title";
	
	private PreparedStatement PS_FIND_BY_AUTHOR;
	private PreparedStatement PS_FIND_BY_YEAR;
	private PreparedStatement PS_FIND_BY_JOURNAL;
	private PreparedStatement PS_FIND_BY_TITLE;
	private PreparedStatement PS_INSERT_PUBLICATION;
	private PreparedStatement PS_INSERT_PUBLICATION_AUTHOR;
	private PreparedStatement PS_FIND_ALL;
	
	public DBDAO() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/publications", "root", "");
		PS_FIND_BY_AUTHOR = connection.prepareStatement(FIND_BY_AUTHOR);
		PS_FIND_BY_YEAR = connection.prepareStatement(FIND_BY_YEAR);
		PS_FIND_BY_JOURNAL = connection.prepareStatement(FIND_BY_JOUNRAL);
		PS_FIND_BY_TITLE = connection.prepareStatement(FIND_BY_TITLE);
		PS_INSERT_PUBLICATION = connection.prepareStatement(INSERT_PUBLICATION);
		PS_INSERT_PUBLICATION_AUTHOR = connection.prepareStatement(INSERT_PUBLICATION_AUTHOR);
		PS_FIND_ALL = connection.prepareStatement(FIND_ALL);
	}
	
	private List<Article> constructArticle(ResultSet rs) throws SQLException {
		HashMap<String, Article> Articles = new HashMap<String, Article>();
		while (rs.next()) {
			String title = rs.getString("P.title");
			Article p = Articles.get(title);
			if (p == null) {
				p = new Article();
				p.setTitle(rs.getString("P.title"));
				p.setJournal(rs.getString("journal"));
				p.setYear(rs.getString("year"));
				p.setPages(rs.getString("page"));
				p.setVolume(rs.getString("volume"));
				p.setURL(rs.getString("URL"));
				p.setMdate(rs.getString("mdate"));
				p.setKey(rs.getString("keyword"));
				Articles.put(title, p);
			}
			String author = rs.getString("author");
			List<String> authors = new ArrayList<String>();
			authors.add(author);
			p.setAuthor(authors);
			//p.setAuthor(rs.getString("author"));
		}
		return new ArrayList<Article>(Articles.values());
	}
	
	public List<Article> findByAuthor(String author) throws SQLException {
		PS_FIND_BY_AUTHOR.setString(1, author);
		ResultSet rs = PS_FIND_BY_AUTHOR.executeQuery();
		List<Article> result =  this.constructArticle(rs);
		System.out.println(result);
		return result;
	}
	
	public List<Article> findByYear(Integer year) throws SQLException {
		PS_FIND_BY_YEAR.setInt(1, year);
		ResultSet rs = PS_FIND_BY_YEAR.executeQuery();
		return this.constructArticle(rs);
	}
	
	public List<Article> findByJournal(String journal) throws SQLException {
		PS_FIND_BY_JOURNAL.setString(1, journal);
		ResultSet rs = PS_FIND_BY_JOURNAL.executeQuery();
		return this.constructArticle(rs);
	}
	
	public List<Article> findByTitle(String title) throws SQLException {
		PS_FIND_BY_TITLE.setString(1, title + "%");
		ResultSet rs = PS_FIND_BY_TITLE.executeQuery();
		List<Article> result =  this.constructArticle(rs);
//		System.out.println(result);
		return result;
	}

	public List<Article> findAll() throws SQLException{
		ResultSet rs = PS_FIND_ALL.executeQuery();
		List<Article> result =  this.constructArticle(rs);
		//System.out.println(result);
		return result;
		
	}
	
	public void insertArticle(Article Article) throws SQLException {
		if (Article.getTitle() == null) {
			return;
		}
		PS_INSERT_PUBLICATION.setString(1, Article.getTitle());
		
		if (Article.getYear() != null) {
			PS_INSERT_PUBLICATION.setString(2, Article.getYear());
		} else {
			PS_INSERT_PUBLICATION.setInt(2, -1);
		}
		if (Article.getJournal() != null) {
			PS_INSERT_PUBLICATION.setString(3, Article.getJournal());
		} else {
			PS_INSERT_PUBLICATION.setString(3, "");
		}
		if (Article.getPages() != null) {
			PS_INSERT_PUBLICATION.setString(4, Article.getPages());
		}
		if (Article.getVolume() != null) {
			PS_INSERT_PUBLICATION.setString(5, Article.getVolume());
		}
		if (Article.getURL() != null) {
			PS_INSERT_PUBLICATION.setString(6, Article.getURL());
		}
		if (Article.getMdate() != null) {
			PS_INSERT_PUBLICATION.setString(7, Article.getMdate());
		}
		if (Article.getKey() != null) {
			PS_INSERT_PUBLICATION.setString(8, Article.getKey());
		}

		PS_INSERT_PUBLICATION.executeUpdate();
		
		for (String author : Article.getAuthor()) {
			PS_INSERT_PUBLICATION_AUTHOR.setString(1, Article.getTitle());
			PS_INSERT_PUBLICATION_AUTHOR.setString(2, author);
			PS_INSERT_PUBLICATION_AUTHOR.executeUpdate();
		}
		
	}
	
	public void close() throws Exception {
		connection.close();
	}

}
