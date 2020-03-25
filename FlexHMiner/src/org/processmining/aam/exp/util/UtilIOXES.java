package org.processmining.aam.exp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.deckfour.xes.in.XesXmlGZIPParser;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.out.XSerializer;
import org.processmining.log.csvexport.XesCsvSerializer;

public class UtilIOXES {
	
	public static XLog importXesLog(String logname) {
		String readlogfilename = ".\\logs\\";
		XesXmlParser parser = new XesXmlParser();
		String f = readlogfilename + logname + ".xes";
		XLog log = null;
		try {
			log = parser.parse(new File(f)).get(0);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return log;
	}
	
	public static XLog importXesGZLog(String logname) {
		String readlogfilename = ".//logs//";
		XesXmlGZIPParser parser = new XesXmlGZIPParser(); 
		

//		List<XLog> logs = new ArrayList<>();
			String f = readlogfilename + logname + ".xes.gz";
			XLog log = null;
			try {
				log = parser.parse(new File(f)).get(0);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
//			logs.add(log);
//		}
		return log;
	}
	
	
	public static void exportXesAsCsv(XLog log, String filename) {

		OutputStream out = null;
		try {
			out = new FileOutputStream(filename, false);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		XSerializer logSerializer = new XesCsvSerializer("yyyy/MM/dd HH:mm:ss.SSS");
		try {
			logSerializer.serialize(log, out);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
