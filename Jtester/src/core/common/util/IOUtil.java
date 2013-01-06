package core.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import core.common.model.jobflow.JobConst;

public class IOUtil {
	
	public static String read(String filePath) throws IOException {
		String line = null;
		StringBuilder sb = new StringBuilder();
		
		BufferedReader in = new BufferedReader(new FileReader(filePath));
		while ((line = in.readLine()) != null) {
			sb.append(line + JobConst.ENTER);
		}
		in.close();
		return sb.toString();
	}
}
