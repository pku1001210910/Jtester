package plugin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import core.common.model.jobflow.JobConst;

public class IOUtil {
	
	public static List<String> getSelectionPath(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object[] elements = ((IStructuredSelection) selection).toArray();

			IJavaElement elementToFind = null;
			if (elements.length == 1) {
				Object selected = elements[0];
				if (!(selected instanceof IJavaElement)
						&& selected instanceof IAdaptable) {
					selected = ((IAdaptable) selected)
							.getAdapter(IJavaElement.class);
				}
				if (selected instanceof IJavaElement) {
					IJavaElement element = (IJavaElement) selected;
					switch (element.getElementType()) {
					case IJavaElement.JAVA_PROJECT:
					case IJavaElement.PACKAGE_FRAGMENT_ROOT:
					case IJavaElement.PACKAGE_FRAGMENT:
					case IJavaElement.TYPE:
					case IJavaElement.METHOD:
					case IJavaElement.COMPILATION_UNIT:
						elementToFind = element;
						break;
					case IJavaElement.CLASS_FILE:
						elementToFind = ((IClassFile) element).getType();
						break;
					}
				}
				
				String elementAbsPath = getWorkspacePath() + elementToFind.getPath().toPortableString().substring(1);
				List<String> srcs = getAllSrcs(elementAbsPath);
				return srcs;
			}
		}
		System.err.println("No element selected!");
		return null;
	}
	

	public static String getWorkspacePath(){
		return Platform.getInstanceLocation().getURL().getPath().substring(1);
	}
	
	private static List<String> getAllSrcs(String path) {
		if(path == null || path.isEmpty()){
			return null;
		}
		
		File dir = new File(path);
		List<String> srcs = new ArrayList<String>();
		
		if(dir.isFile() && dir.getAbsolutePath().endsWith(".java")){
			srcs.add(path);
		} else if(dir.isDirectory()){
			File[] files = dir.listFiles();
			for (File file : files) {
				List<String> subSrcs = getAllSrcs(file.getAbsolutePath());
				if(subSrcs != null){
					srcs.addAll(subSrcs);
				}
			}
		}
		return srcs;
	}
}
