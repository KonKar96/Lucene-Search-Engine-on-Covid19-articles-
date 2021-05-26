package informationretrieval.engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class SearchEngine {
	
	private int resultIndex = 0;
	private boolean[] fieldsToSearch = {true, true, true};
	private ArrayList<String> autocopleteSuggestions;
	private ScoreDoc[] hits;
	
	private Directory idirectory;
	private DirectoryReader ireader;
	private IndexSearcher isearcher;
	private MultiFieldQueryParser mqparser;
	private Formatter htmlformatter;
	private QueryScorer qscorer;
	private Highlighter highlighter;
	private Fragmenter fragmenter;
	
	private boolean tempIsNewSearch = true;

	public SearchEngine() throws IOException {
		createIndex();
	}
	
	private void createIndex() throws IOException {
		if ((new File(LuceneConstants.INDEX_PATH).listFiles().length) > 0)
				System.out.println("Index exists!");
		else
			new DatasetIndexer().createIndex();
		autocopleteSuggestions = new ArrayList<String>();
		idirectory = FSDirectory.open(FileSystems.getDefault().getPath(LuceneConstants.INDEX_PATH));
		ireader = DirectoryReader.open(idirectory);
		isearcher = new IndexSearcher(ireader);
		System.out.println("Index opened for search!");
	}
	
	public ArrayList<String> search(String queryString, boolean[] searchFields, boolean newSearch) throws IOException, ParseException, InvalidTokenOffsetsException {
		resultIndex = 0;
		tempIsNewSearch = newSearch;
		fieldsToSearch = searchFields.clone();
		for(String suggestion : queryString.toLowerCase().split(" "))
			if (!autocopleteSuggestions.contains(suggestion))
				autocopleteSuggestions.add(suggestion);
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> qfields = new ArrayList<String>();
		if (fieldsToSearch[0])
			qfields.add(LuceneConstants.TITLE);
		if (fieldsToSearch[1])
			qfields.add(LuceneConstants.CONTENTS);

		mqparser = new MultiFieldQueryParser(qfields.toArray(new String[0]), new StandardAnalyzer());
		Query query = mqparser.parse(queryString);
		hits = isearcher.search(query, LuceneConstants.MAX_SEARCH).scoreDocs;
		if (hits.length < LuceneConstants.MAX_PROJECT)
			tempIsNewSearch = true;
		htmlformatter = new SimpleHTMLFormatter();
		qscorer = new QueryScorer(query);
		highlighter = new Highlighter(htmlformatter, qscorer);
		fragmenter = new SimpleSpanFragmenter(qscorer, 10);
		highlighter.setTextFragmenter(fragmenter);
		if (hits.length != 0) {
			int projectionNum = LuceneConstants.MAX_PROJECT;
			if (hits.length < LuceneConstants.MAX_PROJECT)
				projectionNum = hits.length;
			for (int i = 0; i < projectionNum; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				String [] res = getResult(hitDoc);
				results.add("<i>" + (i+1) + ":</i> " + res[0] + "<br>" + res[1]);
			}
		} else {
			System.out.println("No top docs!");
		}
		resultIndex += LuceneConstants.MAX_PROJECT;
		return results;
	}

	public ArrayList<String> getNext() throws IOException, ParseException, InvalidTokenOffsetsException {
		ArrayList<String> results = new ArrayList<String>();
		if (!tempIsNewSearch) {
			int projectionNum = resultIndex + LuceneConstants.MAX_PROJECT;
			if (hits.length < resultIndex + LuceneConstants.MAX_PROJECT)
				projectionNum = hits.length;
			for (int i = resultIndex; i < projectionNum; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				String [] res = getResult(hitDoc);
				results.add("<i>" + (i+1) + ":</i> " + res[0] + "<br>" + res[1]);
			}
			resultIndex += LuceneConstants.MAX_PROJECT;
			if (resultIndex >= LuceneConstants.MAX_SEARCH || resultIndex >= hits.length)
				resultIndex = 0;
		}
		return results;
	}
	
	private String[] getResult(Document hitDoc) throws IOException, InvalidTokenOffsetsException {
		String resultTitle = hitDoc.get(LuceneConstants.TITLE);
		String resultContents = hitDoc.get(LuceneConstants.CONTENTS);
		String title = "";
		String contents = "";
		String[] frags;
		if (fieldsToSearch[0]) {
			frags = highlighter.getBestFragments(new StandardAnalyzer(), LuceneConstants.TITLE, resultTitle, 10);
	        for (String frag : frags)
	        	title += frag;
	        if (title.equals(""))
	        	title = resultTitle;
		}
		if (fieldsToSearch[1]) {
			frags = highlighter.getBestFragments(new StandardAnalyzer(), LuceneConstants.CONTENTS, resultContents, 10);
	        for (String frag : frags)
	        	contents += frag;
	        if (contents.equals(""))
	        	contents = resultContents;
		}
        if (title.equals(""))
        	title = resultTitle;
        if (contents.equals(""))
        	contents = resultContents;
        
		return new String[]{title , contents.substring(0, (contents.length() < 100) ? contents.length() : 100)};
	}
	
	public String[] getDocumentInfo(String docID) throws IOException, InvalidTokenOffsetsException {
		String docTitle = "";
		String docContents = "";
		int documentID = Integer.parseInt(docID) - 1;
		if (documentID >= 0 && documentID < hits.length) {
			Document hitDoc = isearcher.doc(hits[documentID].doc);
			String resultTitle = hitDoc.get(LuceneConstants.TITLE);
			String resultContents = hitDoc.get(LuceneConstants.CONTENTS).replace(",", " | ");
			String title = "";
			String contents = "";
			String[] frags;
			if (fieldsToSearch[0]) {
				frags = highlighter.getBestFragments(new StandardAnalyzer(), LuceneConstants.TITLE, resultTitle, 10);
		        for (String frag : frags)
		        	title += frag;
		        if (title.equals(""))
		        	title = resultTitle;
			}
			if (fieldsToSearch[1]) {
		        frags = highlighter.getBestFragments(new StandardAnalyzer(), LuceneConstants.CONTENTS, resultContents, 500);
		        for (String frag : frags)
		        	contents += frag;
			}
			
	        
	        if (title.equals(""))
	        	title = resultTitle;
	        if (contents.equals(""))
	        	contents = resultContents;
	       
	        docTitle = resultTitle;
	        docContents = title + "<br>" + contents;
		}
		return new String[]{docTitle, docContents};
	}
	
	public ArrayList<String> updateAutocomplete() {
		return autocopleteSuggestions;
	}

}
