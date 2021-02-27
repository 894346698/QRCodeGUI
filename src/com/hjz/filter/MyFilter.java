package com.hjz.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MyFilter extends FileFilter {
	private String[] filterString = null;

	public MyFilter(String[] filStrings) {
		this.filterString = filStrings;
	}
	/**
	 * 判断是否接受
	 * 
	 * @param file 文件
	 * @return
	 */
	public boolean accept(File file) {
		if (file.isDirectory())
			return true;
		for (int i = 0; i < filterString.length; ++i)
			if (file.getName().endsWith(filterString[i]))
				return true;
		return false;
	}
	
	public String getDescription() {
		String ss = "";
		for (int i = 0; i < filterString.length; ++i)
			ss += " *" + filterString[i];
		return ("Txt Files(" + ss + ")"); 
	}
}
