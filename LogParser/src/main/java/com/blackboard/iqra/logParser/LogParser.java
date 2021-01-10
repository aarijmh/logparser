package com.blackboard.iqra.logParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

public class LogParser {

	static FileVisitor<Path> simpleFileVisitor = new SimpleFileVisitor<Path>() {
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			System.out.println("-------------------------------------");
			System.out.println("DIRECTORY NAME:" + dir.getFileName() + "LOCATION:" + dir.toFile().getPath());
			System.out.println("-------------------------------------");
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path visitedFile, BasicFileAttributes fileAttributes) throws IOException {
			System.out.println("FILE NAME: " + visitedFile.getFileName());
			unTarFile(visitedFile.toString(), destFile);
			parseFile(destFile);
			return FileVisitResult.CONTINUE;
		}
	};

	static String DESTINATION_PATH = "E:\\logs\\uncompressed\\aa.txt";
	static File destFile = new File(DESTINATION_PATH);
	static OutputStreamWriter writer;
	static BufferedWriter bufWriter;

	public static void main(String[] args) throws IOException {
		writer = new OutputStreamWriter(new FileOutputStream("C:\\Temp\\log\\output.txt"), "UTF-8");

		bufWriter = new BufferedWriter(writer);

		FileSystem fileSystem = FileSystems.getDefault();
		Path rootPath = fileSystem.getPath("E:\\logs\\December 2020");
		try {
			Files.walkFileTree(rootPath, simpleFileVisitor);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		finally {
			bufWriter.close();
		}

//		
//
//		parseFile(destFile);
	}

	private static void parseFile(File file) throws IOException {
		int state = 0;
		StringBuffer buffer = new StringBuffer();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String st;
			while ((st = br.readLine()) != null) {
				switch (state) {
				case 0:
					if (st.startsWith("{")) {
						buffer.append(st);
						state = 1;
					}
					break;
				case 1:
					buffer.append(st);
					if (st.endsWith("}")) {
						state = 0;
						if (buffer.toString().contains("adaptiveRelease")) {
							bufWriter.write(buffer.toString());
							bufWriter.write(System.lineSeparator());
						} 
						buffer.setLength(0);
					}

				}

			}
		}

	}

	private static void unTarFile(String tarFile, File destFile) {
		TarArchiveInputStream tis = null;
		try (FileInputStream fis = new FileInputStream(tarFile);
				GZIPInputStream gzipInputStream = new GZIPInputStream(new BufferedInputStream(fis));) {

			destFile.createNewFile();

			OutputStream out = new FileOutputStream(destFile);
			byte[] buffer = new byte[65536];
			int noRead;
			while ((noRead = gzipInputStream.read(buffer)) != -1) {
				out.write(buffer, 0, noRead);
			}

			out.close();

		} catch (IOException ex) {
			System.out.println("Error while untarring a file- " + ex.getMessage());
		} finally {

			if (tis != null) {
				try {
					tis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
