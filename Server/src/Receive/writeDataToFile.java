package Receive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class writeDataToFile {
	private static String Path="C:\\Users\\lenovo\\Desktop\\test.txt";
	private static Object Data ;
	
	public writeDataToFile(String Path,Object Data){
		this.Path=Path;
		this.Data=Data;
	}
	
	public writeDataToFile(Object Data){
		this.Data=Data;
	}
	
	public static void writeDataToFile(){
		
			File docFile = new File(Path);
			try {
			docFile.createNewFile();
			FileOutputStream txtfile = new FileOutputStream(docFile);
			PrintStream p = new PrintStream(txtfile);
			p.print(Data);
			txtfile.close();
			p.close();
			} catch (IOException e) {
			e.printStackTrace();
			}
	}
//	
//	public static void main(String[] args){
//		writeDataToFile();
//	}
}
