package lse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class lseTesting {

	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File("AliceCh1.txt"));
		while(sc.hasNext())
		{	
			System.out.println(sc.next());
		}
		
	
//		
		Scanner read = new Scanner(System.in);
//		boolean repeat = true;
//		while(repeat == true)
//		{
//			System.out.print("Enter Letter: ");
//			String x = read.next();
//			if(Character.isLetter(x.charAt(0)))
//			{
//				System.out.println(x.charAt(0)+" is a Letter!");
//			}
//			else
//			{
//				System.out.println(x.charAt(0) + " not a Letter, exiting loop");
//				repeat = false;
//			}
//		}
		
		//boolean y = true;
		boolean y = false;
		while(y==true)
		{
			System.out.print("Enter word: ");
			String word = read.next();
			String clean = checkNCleanWord(word);
			if(clean != null) System.out.println(clean);
			else System.out.println("null");
			System.out.print("Continue? (y for yes): ");
			String y2 = read.next();
			if(!y2.equals("y")) y=false;
		}
		
	//	getstuff();
		
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
		return cleanedWord;
		
	}
	
	
	public static void hashTesting()
	{
		
		HashMap<String,Occurrence> keywordsIndex;
		keywordsIndex = new HashMap<String,Occurrence>(1000,2.0f);
		Occurrence first = new Occurrence("Alice",0);
		keywordsIndex.put("hello",first);
		keywordsIndex.put("hi",first);
		
		if(keywordsIndex.containsKey("hello"))
		{
			keywordsIndex.get("hello").frequency++;
		}
		
		printHash(keywordsIndex);
		
	}
	
	private static void printHash(HashMap<String,Occurrence> hash)
	{
		Set<String> st = hash.keySet();
		String[] keys = st.toArray(new String[0]);
		for(int i=0;i<keys.length;i++)
		{
			Occurrence trgt = hash.get(keys[i]);
			String bk = trgt.document;
			int freq = trgt.frequency;
			System.out.println(keys[i] + "--> "+bk+":"+freq);
		}
	}
	
	private static void getstuff()
	{
		HashMap<String,Occurrence> k = new HashMap<String,Occurrence>();
		Scanner readd = new Scanner(System.in);
		System.out.print("Enter textline: ");
		String words = readd.nextLine();
		String[] wordset = words.split(" ");
		System.out.println("lengthOFWordset: "+ wordset.length);
		for(int i=0;i<wordset.length;i++)
		{
			String cleann = checkNCleanWord(wordset[i]);
			if(cleann != null)
			{
				System.out.println("testing " + cleann);
				if(k.containsKey(cleann))
				{
					k.get(cleann).frequency++;
				}
				else
				{
					k.put(cleann, new Occurrence("Riz",1));
				}
			}
		}
		
		printHash(k);
		
	}
	
}
