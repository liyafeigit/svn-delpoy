package com.delpoy;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileHelper {
	private String message;
	private final long KB = 1024;
	private final long MB = 1024 * KB;
	private final long GB = 1024 * MB;
	// bt字节参考量
	public static final long SIZE_BT = 1024L;
	// KB字节参考量
	public static final long SIZE_KB = SIZE_BT * 1024L;
	// MB字节参考量
	public static final long SIZE_MB = SIZE_KB * 1024L;
	// GB字节参考量
	public static final long SIZE_GB = SIZE_MB * 1024L;
	// TB字节参考量
	public static final long SIZE_TB = SIZE_GB * 1024L;

	public static final int SACLE = 2;

	// 文件大小属性
	private long longSize;

	// 递归遍历文件目录计算文件大小
	private void getFileSize(File file) {
		// 获得文件目录下文件对象数组
		File[] fileArray = file.listFiles();
		// 如果文件目录数组不为空或者length!=0,即目录为空目录
		if (fileArray != null && fileArray.length != 0) {
			// 遍历文件对象数组
			for (int i = 0; i < fileArray.length; i++) {
				File fileSI = fileArray[i];
				// 如果是目录递归遍历
				if (fileSI.isDirectory()) {
					// 递归遍历
					getFileSize(fileSI);
				}
				// 如果是文件
				if (fileSI.isFile()) {
					longSize += fileSI.length();
				}
			}
		} else {
			// 如果文件目录数组为空或者length==0,即目录为空目录
			longSize = 0;
		}
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 读取文本文件内容
	 * 
	 * @param filePathAndName
	 *            带有完整绝对路径的文件名
	 * @param encoding
	 *            文本文件打开的编码方式
	 * @return 返回文本文件的内容
	 */
	public String readTxt(String filePathAndName, String encoding)
			throws IOException {
		encoding = encoding.trim();
		StringBuffer str = new StringBuffer("");
		String st = "";
		try {
			FileInputStream fs = new FileInputStream(filePathAndName);
			InputStreamReader isr;
			if (encoding.equals("")) {
				isr = new InputStreamReader(fs);
			} else {
				isr = new InputStreamReader(fs, encoding);
			}
			BufferedReader br = new BufferedReader(isr);
			try {
				String data = "";
				while ((data = br.readLine()) != null) {
					str.append(data + "\n");
				}
			} catch (Exception e) {
				str.append(e.toString());
			}
			st = str.toString();
		} catch (IOException es) {
			st = "";
		}
		return st;
	}

	/**
	 * 新建目录
	 * 
	 * @param folderPath
	 *            目录
	 * @return 返回目录创建后的路径
	 */
	public String createFolder(String folderPath) {
		String txt = folderPath;
		try {
			File myFilePath = new File(txt);
			txt = folderPath;
			if (!myFilePath.exists()) {
				myFilePath.mkdirs();
			}
		} catch (Exception e) {
			message = "创建目录操作出错";
		}
		return txt;
	}

	/**
	 * 多级目录创建
	 * 
	 * @param folderPath
	 *            准备要在本级目录下创建新目录的目录路径 例如 c:myf
	 * @param paths
	 *            无限级目录参数，各级目录以单数线区分 例如 a|b|c
	 * @return 返回创建文件后的路径 例如 c:myfa c
	 */
	public String createFolders(String folderPath, String paths) {
		String txts =

		folderPath;
		try {
			String txt;
			txts = folderPath;
			StringTokenizer st = new StringTokenizer(paths, "|");
			for (int i = 0; st.hasMoreTokens(); i++) {
				txt = st.nextToken().trim();
				if (txts.lastIndexOf("/") != -1) {
					txts = createFolder(txts + txt);
				} else {
					txts = createFolder(txts + txt + "/");
				}
			}
		} catch (Exception e) {
			message = "创建目录操作出错！";
		}
		return txts;
	}

	/**
	 * 新建文件
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @param fileContent
	 *            文本文件内容
	 * @return
	 */
	public void createFile(String filePathAndName, String fileContent) {

		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			FileWriter resultFile = new FileWriter(myFilePath);
			PrintWriter myFile = new PrintWriter(resultFile);
			String strContent = fileContent;
			myFile.println(strContent);
			myFile.close();
			resultFile.close();
		} catch (Exception e) {
			message = "创建文件操作出错";
		}
	}

	/**
	 * 有编码方式的文件创建
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @param fileContent
	 *            文本文件内容
	 * @param encoding
	 *            编码方式 例如 GBK 或者 UTF-8
	 * @return
	 */
	public void createFile(String filePathAndName, String fileContent,
			String encoding) {

		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			PrintWriter myFile = new PrintWriter(myFilePath, encoding);
			String strContent = fileContent;
			myFile.println(strContent);
			myFile.close();
		} catch (Exception e) {
			message = "创建文件操作出错";
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @return Boolean 成功删除返回true,遇异常返回false
	 */
	public boolean delFile(String filePathAndName) {
		boolean bea = false;
		try {
			String filePath = filePathAndName;
			File myDelFile = new File(filePath);
			if (myDelFile.exists()) {
				myDelFile.delete();
				bea = true;
			} else {
				bea = false;
				message = (filePathAndName + "删除文件操作出错");
			}
		} catch (Exception e) {
			message = e.toString();
		}
		return bea;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹完整绝对路径
	 * @return
	 */
	public void delFolder(String folderPath) {
		LinkedList<String> folderList = new LinkedList<String>();
		folderList.add(folderPath);
		while (folderList.size() > 0) {
			File file = new File((String) folderList.poll());
			File[] files = file.listFiles();
			ArrayList<File> fileList = new ArrayList<File>();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					folderList.add(files[i].getPath());
				} else {
					fileList.add(files[i]);
				}
			}
			for (File f : fileList) {
				f.delete();
			}
		}
		folderList = new LinkedList<String>();
		folderList.add(folderPath);
		while (folderList.size() > 0) {
			File file = new File((String) folderList.getLast());
			if (file.delete()) {
				folderList.removeLast();
			} else {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					folderList.add(files[i].getPath());
				}
			}
		}
	}

	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹完整绝对路径
	 * @return
	 * @return
	 */
	public boolean delAllFile(String path) {
		boolean bea = false;
		File file = new File(path);
		if (!file.exists()) {
			return bea;
		}
		if (!file.isDirectory()) {
			return bea;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				bea = true;
			}
		}
		return bea;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPathFile
	 *            准备复制的文件源
	 * @param newPathFile
	 *            拷贝到新绝对路径带文件名
	 * @return
	 */
	public void copyFile(String oldPathFile, String newPathFile) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPathFile);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPathFile); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPathFile);
				byte[] buffer = new byte[10240];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
//					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}else{
				throw new RuntimeException("没有找到"+oldPathFile);
			}
		} catch (Exception e) {
			message = ("复制单个文件操作出错");
		}
	}

	/**
	 * 复制整个文件夹的内容
	 * 
	 * @param oldPath
	 *            准备拷贝的目录
	 * @param newPath
	 *            指定绝对路径的新目录
	 * @return
	 */
	public void copyFolder(String oldPath, String newPath) {
		try {
			new File(newPath).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[10240];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			message = "复制整个文件夹内容操作出错";
		}
	}

	/**
	 * 移动文件
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public void moveFile(String oldPath, String newPath) {
		copyFile(oldPath, newPath);
		delFile(oldPath);
	}

	/**
	 * 移动目录
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public void moveFolder(String oldPath, String newPath) {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	/**
	 * 清空文件夹
	 * 
	 * @param folderPath
	 * @return
	 */
	public void cleanFolder(String folderPath) {
		File delfilefolder = new File(folderPath);
		if (!delfilefolder.exists() && !delfilefolder.delete()) {
			LinkedList<String> folderList = new LinkedList<String>();
			folderList.add(delfilefolder.getAbsolutePath());
			while (folderList.size() > 0) {
				File file = new File((String) folderList.poll());
				File[] files = file.listFiles();
				ArrayList<File> fileList = new ArrayList<File>();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						folderList.add(files[i].getPath());
					} else {
						fileList.add(files[i]);
					}
				}
				for (File f : fileList) {
					f.delete();
				}
			}
			folderList = new LinkedList<String>();
			folderList.add(delfilefolder.getAbsolutePath());
			while (folderList.size() > 0) {
				File file = new File((String) folderList.getLast());
				if (file.delete()) {
					folderList.removeLast();
				} else {
					File[] files = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						folderList.add(files[i].getPath());
					}
				}
			}
		}
		delfilefolder.mkdir();
	}

	/**
	 * 计算文件大小
	 * 
	 * @param filePath
	 * @return String
	 */
	public String fileSize(String filePath) {
		// 文件属性
		File file = new File(filePath);
		// 如果文件存在而且是文件，直接返回文件大小
		if (file.exists() && file.isFile()) {
			long filesize = file.length();
			String showsize;
			if (filesize >= GB)
				showsize = filesize / GB + " GB";
			else if (filesize >= MB)
				showsize = filesize / MB + " MB";
			else if (filesize >= KB)
				showsize = filesize / KB + " KB";
			else if (filesize > 1)
				showsize = filesize / GB + " Bytes";
			else
				showsize = "1 Byte";
			return showsize;
		}
		return null;
	}

	/**
	 * 计算文件夹的大小
	 * 
	 * @param folderPath
	 * @return String
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public String folderSize(String folderPath) {
		// 文件存在而且是目录，递归遍历文件目录计算文件大小
		File file = new File(folderPath);
		if (file.exists() && file.isDirectory()) {
			getFileSize(file);// 递归遍历
		}
		if (longSize == 1) {
			return "1 Byte";
		} else if (longSize >= 2 && longSize < SIZE_BT) {
			return longSize + " Bytes";
		} else if (longSize >= SIZE_BT && longSize < SIZE_KB) {
			return longSize / SIZE_BT + " KB";
		} else if (longSize >= SIZE_KB && longSize < SIZE_MB) {
			return longSize / SIZE_KB + " MB";
		} else if (longSize >= SIZE_MB && longSize < SIZE_GB) {
			BigDecimal longs = new BigDecimal(Double.valueOf(longSize + "")
					.toString());
			BigDecimal sizeMB = new BigDecimal(Double.valueOf(SIZE_MB + "")
					.toString());
			String result = longs.divide(sizeMB, SACLE,
					BigDecimal.ROUND_HALF_UP).toString();
			// double result=longSize/(double)SIZE_MB;
			return result + " GB";
		} else {
			BigDecimal longs = new BigDecimal(Double.valueOf(longSize + "")
					.toString());
			BigDecimal sizeMB = new BigDecimal(Double.valueOf(SIZE_GB + "")
					.toString());
			String result = longs.divide(sizeMB, SACLE,
					BigDecimal.ROUND_HALF_UP).toString();
			return result + " TB";
		}
	}

	/**
	 * 以一个文件夹的框架在另一个目录创建文件夹和空文件
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public void buildPath(String oldPath, String newPath) {
		boolean b = false;// 不创建空文件
		List<String> folderList = new ArrayList<String>();
		folderList.add(oldPath);
		List<String> folderList2 = new ArrayList<String>();
		folderList2.add(newPath);
		for (int j = 0; j < folderList.size(); j++) {
			(new File(folderList2.get(j))).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File folders = new File(folderList.get(j));
			String[] file = folders.list();
			File temp = null;
			try {
				for (int i = 0; i < file.length; i++) {
					if (folderList.get(j).endsWith(File.separator)) {
						temp = new File(folderList.get(j), file[i]);
					} else {
						temp = new File(folderList.get(j), file[i]);
					}
					if (temp.isFile()) {
						if (b)
							temp.createNewFile();
					} else if (temp.isDirectory()) {// 如果是子文件夹
						folderList.add(folderList.get(j) + File.separator
								+ file[i]);
						folderList2.add(folderList2.get(j) + File.separator
								+ file[i]);
					}
				}
			} catch (IOException e) {
				// 复制整个文件夹内容操作出错
				e.printStackTrace();
			}
		}
	}

	/**
	 * 对目标压缩文件解压缩到指定文件夹
	 * 
	 * @param filePath
	 * @param folderPath
	 * @return
	 */
	public void unzipFolder(String filePath, String folderPath) {
		File myFolderPath = new File(folderPath);
		try {
			if (!myFolderPath.exists()) {
				myFolderPath.mkdir();
			}
		} catch (Exception e) {
			// 新建目录操作出错
			e.printStackTrace();
			return;
		}
		try {
			ZipInputStream in = new ZipInputStream(
					new FileInputStream(filePath));
			ZipEntry entry = null;
			while ((entry = in.getNextEntry()) != null) {
				String entryName = entry.getName();
				File file = new File(folderPath, entryName);
				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					FileOutputStream os = new FileOutputStream(file);
					// Transfer bytes from the ZIP file to the output
					// file
					byte[] buf = new byte[10240];
					int len;
					while ((len = in.read(buf)) > 0) {
						os.write(buf, 0, len);
					}
					os.close();
					in.closeEntry();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提取扩展名
	 * 
	 * @param filePath
	 *            目录
	 * @return
	 */
	public String GetExt(String filePath) {
		if (filePath.indexOf('.') > 0)
			return filePath.substring(filePath.lastIndexOf('.'));
		else
			return "";
	}

	/**
	 * 提取文件名
	 * 
	 * @param filePath
	 *            目录
	 * @return
	 */
	public String GetFileName(String filePath) {
		if (filePath.indexOf('\\') > 0)
			return filePath.substring(filePath.lastIndexOf("\\") + 1);
		else
			return "";
	}

	/**
	 * 提取文件路径
	 * 
	 * @param filePath
	 *            目录
	 * @return
	 */
	public String GetDirName(String filePath) {
		if (filePath.indexOf('\\') > 0)
			return filePath.substring(0, filePath.lastIndexOf("\\"));
		else
			return "";
	}

	/**
	 * 磁盘剩余空间计算
	 * 
	 * @param disk
	 * @return long
	 */
	public long diskFreeSpace(String disk) {
		File file = new File(disk);
		return file.getFreeSpace();
	}

	public String getMessage() {
		return this.message;
	}
	
	public static void main(String[] args) {
		FileHelper h = new FileHelper();
		h.createFolder("E:\\svnlocal\\deploy\\2014-03-10");
	}
}
