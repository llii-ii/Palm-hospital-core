package com.yihu.hos.util;

import java.io.File;
import java.io.FilenameFilter;

public class FileTypeFilter implements FilenameFilter{

	@Override
	public boolean accept(File dir, String name) {
		if (name.endsWith(".inf.xml")) {
			return true;
		}
		return false;
	}
	
}
