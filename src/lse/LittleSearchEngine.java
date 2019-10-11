package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		
		HashMap<String,Occurrence> indxOfdoc = new HashMap<String,Occurrence>();
		Scanner sc = new Scanner(new File(docFile));
		while(sc.hasNext())
		{	
			String wordToBeIndxd = getKeyword(sc.next());
			if(wordToBeIndxd != null)
			{
				if(indxOfdoc.containsKey(wordToBeIndxd))
				{
					indxOfdoc.get(wordToBeIndxd).frequency++;
				}
				else
				{
					indxOfdoc.put(wordToBeIndxd, new Occurrence(docFile,1));
				}
			}
		}
		
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		sc.close();
		return indxOfdoc;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		
		//gets an array of all the keys in kws
		Set<String> setOfKeys = kws.keySet();
		String[] keysArray = setOfKeys.toArray(new String[0]);
		
		//add to master index list
		for(int i=0;i<keysArray.length;i++)
		{
			if(this.keywordsIndex.containsKey(keysArray[i]))
			{
				keywordsIndex.get(keysArray[i]).add(kws.get(keysArray[i]));
				insertLastOccurrence(keywordsIndex.get(keysArray[i]));
			}
			else
			{
				keywordsIndex.put(keysArray[i], new ArrayList<Occurrence>());
				keywordsIndex.get(keysArray[i]).add(kws.get(keysArray[i]));
				insertLastOccurrence(keywordsIndex.get(keysArray[i]));
			}
			
		}
		
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		
		String cleanedWord = checkNCleanWord(word);
		if(cleanedWord !=null)
		{
			if(this.noiseWords.contains(cleanedWord))
				return null;
			else
				return cleanedWord;
		}
		
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return null;
	}
	
	
	//will recieve a term from the getIndex method then will check to see whether it is a...
	//legitimate term, if it is then it will return the properly formatted version of it 
	private static String checkNCleanWord(String word)
	{
		String cleanedWord = "";
		String acceptablePunctuations = ".,?:;!";
			
			
		//Look thru word to see if any non-letter in the middle of the word, if yes then null
		for(int i=0;i<word.length();i++)
		{
			if(Character.isLetter(word.charAt(i)))
			{
				continue;
			}
			else
			{
				int nxt = i+1;
				if((nxt<word.length()) && (Character.isLetter(word.charAt(nxt))))
					return null;
				else
					break;
			}
		}
			
			
		//if here then word has no non-letter in the middle of the word, and this for loop...
		//will get rid of any trailing punctuation, also if any illegal punctuation found then...
		//the whole function will return null because then the word is illegal
		for(int i=0;i<word.length();i++)
		{
			if(Character.isLetter(word.charAt(i)))
			{
				cleanedWord = cleanedWord + word.charAt(i);
			}
			else
			{
				boolean properPunctuation = false;
				for(int j=0;j<acceptablePunctuations.length();j++)
				{
					if(word.charAt(i)==acceptablePunctuations.charAt(j)) properPunctuation = true;
				}
					
				if(properPunctuation == false)
					return null;
			}
		}
			
			
		cleanedWord = cleanedWord.toLowerCase();
		if(cleanedWord.equals("")) return null;
		return cleanedWord;
			
	}	
	
	
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		
		//gets size of array list and return null if there is only one element in it
		int arrlstSize = occs.size();
		if(arrlstSize == 1) return null;
		
		//if more than one elelement
		//first take out last element from the arraylist
		Occurrence toBeInserted = occs.get(occs.size()-1);
		occs.remove(occs.size()-1);
		
		//set up and execute binary search for proper position to insert last item
		ArrayList<Integer> indices = new ArrayList<Integer>();
		Occurrence focus = null;
		int l=0;
		int r = occs.size() - 1; 
		int m=0;
		
		while(l<=r)
		{
			m = (l+r)/2;
			focus = occs.get(m);
			indices.add(m);
			
			if(toBeInserted.frequency==focus.frequency)
			{
				occs.add(m, toBeInserted);
				break;
			}
			
			if(toBeInserted.frequency<focus.frequency)
			{
				l = m+1;
				
				continue;
			}
			
			if(toBeInserted.frequency>focus.frequency)
			{
				r = m-1;
				continue;
			}
			
		}
	
		if(occs.size()<arrlstSize)
		{
			if(toBeInserted.frequency<occs.get(m).frequency)
			{
				occs.add(m+1, toBeInserted);
			}
			else
				occs.add(m,toBeInserted);
		}
		
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return indices;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		ArrayList<String> docs = new ArrayList<String>();
		ArrayList<Occurrence> l1 = this.keywordsIndex.get(kw1);
		ArrayList<Occurrence> l2 = this.keywordsIndex.get(kw2);
		
		int tl1 = 0;
		int tl2 = 0;
		
		if(l1 == null)
		{
			tl1 = 1;
			l1 = new ArrayList<Occurrence>();
		}
		
		if(l2 == null)
		{
			tl2 = 1;
			l2 = new ArrayList<Occurrence>();
		}
		
		while( ( (tl1<l1.size()) || (tl2<l2.size()) ) && (docs.size()<5) )
		{
			//if l1 larger than l2:
			if( (tl2>=l2.size()) && (tl1<l1.size()))
			{
				boolean check = chkForDupes(docs,l1.get(tl1).document);
				if(check==false)
				{
					docs.add(l1.get(tl1).document);
				}
				tl1++;
				continue;
			}
			
			//if l2 larger than l1
			if( (tl2<l2.size()) && (tl1>=l1.size()))
			{
				boolean check = chkForDupes(docs,l2.get(tl2).document);
				if(check==false)
				{
					docs.add(l2.get(tl2).document);
				}
				tl2++;
				continue;
				
			}
			
			int l1f = l1.get(tl1).frequency;
			int l2f = l2.get(tl2).frequency;
			boolean check = false;
			if(l1f>=l2f) //frequency of l1's element higher or equal to l2's element
			{
				check = chkForDupes(docs,l1.get(tl1).document);
				if(check==false)
				{
					docs.add(l1.get(tl1).document);
				}
				tl1++;
				continue;
			}
			else
			{
				check = chkForDupes(docs,l2.get(tl2).document);
				if(check==false)
				{
					docs.add(l2.get(tl2).document);
				}
				tl2++;
				continue;
			}
		}
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return docs;
	
	}
	
	
	//checks if there are duplicates in the document names list for top5 search
	//returns true if dupe found false otherwise //fin
	private static boolean chkForDupes(ArrayList<String> docs, String target)
	{
		for(int i=0;i<docs.size();i++)
		{
			if(docs.get(i).equals(target)) return true;
		}
		
		return false;
	}
	
}
