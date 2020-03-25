package org.processmining.aam.exp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class UtilIO {

	
	
	public static final String SEP = ";";

	public static PrintWriter createWriter(String fileName) {
		PrintWriter writer = null;
		try {
			File file = new File(fileName);
		
//			writer = new PrintStream(file);
			FileWriter fileWriter = new FileWriter(fileName, true);
//			writer = new FileOutputStream("c:/temp/samplefile.txt", true);
//			 BufferedWriter writer = new BufferedWriter(
//                     new FileWriter(fileName, true);
			writer = new PrintWriter(fileWriter);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writer;
	}
	
	public static PrintWriter createWriter(String fileName, String[] columnNames) {
		File file = new File(fileName);
		PrintWriter writer = createWriter(fileName);
		if(file.length() == 0) {
			writer.println(concatenate(Arrays.asList(columnNames), ";"));
		}
		
		return writer;
	}
	

	public static LineIterator readFile(String filePath) {
		// InputStreamReader isr = null;
		File file = null;
		try {
			file = new File(filePath);
			FileInputStream fileInputStream = new FileInputStream(filePath);
			// isr = new InputStreamReader(fileInputStream, "unicode");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		return readFile(file);
	}

	public static LineIterator readFile(File file) {
		LineIterator it = null;
		try {
			it = FileUtils.lineIterator(file, "UTF-8");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return it;
	}

	public static String concatenate(List<String> resStrings, String sep) {
		String res = "";
		for(String s : resStrings) {
			res += s + sep;
		}
		
		return res;
	}
	
	
	public static String simplify(String processLabel) {
		
		String s = processLabel.toString();
		s = s.replaceAll("/", "");
		s = s.replaceAll("\\\\", "");
		s = s.replace(".", "");
		s = s.replace("<", "");
		s = s.replace("?", "");
		s = s.replace(">", "");
		s = s.replace("=", "");
		System.out.println(s);
		
		return s;
	}
}
