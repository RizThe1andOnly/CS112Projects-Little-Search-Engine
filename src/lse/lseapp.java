package lse;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class lseapp {
	
	public static void main(String[] args) throws FileNotFoundException
	{
		LittleSearchEngine lse = new LittleSearchEngine();
		lse.makeIndex("docs.txt", "noisewords.txt");
		//printSpecificWord("elf",lse.keywordsIndex);
		//ArrayList<String> s = lse.top5search("null", "null");
		//printArrylist(s);
		printHash(lse.keywordsIndex);
//		if(lse.keywordsIndex.containsKey(""))
//			System.out.println("why is this here");
	}
	
	
	public static void testIndxArray()
	{
		LittleSearchEngine lse = new LittleSearchEngine();
		
	}
	
	private static void printHash(HashMap<String,ArrayList<Occurrence>> p)
	{
		Set<String> st = p.keySet();
		String[] sta = st.toArray(new String[0]);
		for(int i=0;i<sta.length;i++)
		{
			String key = sta[i];
			System.out.print(key+": ");
			ArrayList<Occurrence> ol = p.get(sta[i]);
			for(int j=0;j<ol.size();j++)
			{
				Occurrence o = ol.get(j);
				String name = o.document;
				int freq = o.frequency;
				System.out.print(name+": "+freq+" --> ");
			}
			System.out.print("/");
			System.out.println();
		}
	}
	
	public static void printHashO(HashMap<String,Occurrence> hash)
	{
		Set<String> st = hash.keySet();
		String[] keys = st.toArray(new String[0]);
		for(int i=0;i<keys.length;i++)
		{
			Occurrence trgt = hash.get(keys[i]);
			String bk = trgt.document;
			int freq = trgt.frequency;
			System.out.println(keys[i] + "--> "+bk+": "+freq);
		}
	}
	

	private static void printArrylist(ArrayList<String> in)
	{
		if(in == null || in.size()==0)
		{
			System.out.println("empty, words never appears");
			return;
		}
		
		for(int i=0;i<in.size()-1;i++)
		{
			System.out.print(in.get(i)+" , ");
		}
		
		System.out.print(in.get(in.size()-1));
	}
	
	private static void printSpecificWord(String word,HashMap<String,ArrayList<Occurrence>> h)
	{
		word = word.toLowerCase();
		if(h.containsKey(word))
		{
			ArrayList<Occurrence> ol = h.get(word);
			for(int j=0;j<ol.size();j++)
			{
				Occurrence o = ol.get(j);
				String name = o.document;
				int freq = o.frequency;
				System.out.print(name+":"+freq+" --> ");
			}
			System.out.print("\\");
		}
	}
	
}
