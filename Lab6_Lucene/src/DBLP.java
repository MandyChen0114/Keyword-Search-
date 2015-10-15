import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DBLP {

	public static List<Article> getDatabase() throws Exception {
		// Create a XML file
		File XMLfile = new File("publication.xml");
		// Create an Article Class in Article.java, and a list of Article Class
		// here
		ArrayList<Article> result = new ArrayList<Article>();
		// Get the DOM Builder Factory
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		// Get the DOM Builder
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
		// Load and Parse the XML document as a tree
		Document doc = dbBuilder.parse(XMLfile);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("article");
		// Iterating through the nodes and extracting the data
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);
			Article article = new Article();
			article.author = new ArrayList<String>();
			// Parse the HTML tags
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				article.mdate = eElement.getAttribute("mdate");
				article.key = eElement.getAttribute("key");
				for (int j = 0; j < eElement.getElementsByTagName("author").getLength(); j++) {
					article.author.add(eElement.getElementsByTagName("author").item(j).getTextContent());
				}
				article.title = eElement.getElementsByTagName("title").item(0).getTextContent();
				article.pages = eElement.getElementsByTagName("pages").item(0).getTextContent();
				article.year = eElement.getElementsByTagName("year").item(0).getTextContent();
				article.volume = eElement.getElementsByTagName("volume").item(0).getTextContent();
				article.journal = eElement.getElementsByTagName("journal").item(0).getTextContent();
				article.URL = eElement.getElementsByTagName("url").item(0).getTextContent();
			}
			result.add(article);
		}

		DBDAO dblist = new DBDAO();
		// if it's not the first time to create database and tables, no need to run the following three lines
//		for (int i = 0; i < result.size(); i++) {
//			dblist.insertArticle(result.get(i));
//		}

		return dblist.findAll();
	}
}
