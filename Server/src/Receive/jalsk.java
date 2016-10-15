package Receive;

import Ps.Class1;

import com.mathworks.toolbox.javabuilder.MWException;

public class jalsk {
public static void main(String[] args) throws MWException{
		long startTime = System.currentTimeMillis();
		Class1 c1=new Class1();
		Object[] result=new Object[5];
//		String list=new ArrayList<>(14);
		
//		Object[] File=new Object[1];
		//String File= "D:\\chromedownloads\\new\\new\\measdata.txt";
		String File= "D:\\123.txt";
		result = c1.Ps(14, File);
//		System.out.print(result[7]);
//		for(int i=0;i<6;i++){
//			System.out.println(result[i]);
//		}
//		for(int i=0;i<14;i++){
//			System.out.println(result[6].getClass().getName());
//		}
//		String[] res = result[7].toString().split("   ");
//		String resFinal ="";
//		for(int m=0;m<res.length;m++){
//			resFinal += res[m];
//			resFinal += "\n";
//		}
//		System.out.print(resFinal);
		new writeDataToFile("C:\\Users\\lenovo\\Desktop\\x0.txt",result[6]).writeDataToFile();
		new writeDataToFile("C:\\Users\\lenovo\\Desktop\\y0.txt",result[7]).writeDataToFile();
		new writeDataToFile("C:\\Users\\lenovo\\Desktop\\x1.txt",result[8]).writeDataToFile();
		new writeDataToFile("C:\\Users\\lenovo\\Desktop\\y1.txt",result[9]).writeDataToFile();
		new writeDataToFile("C:\\Users\\lenovo\\Desktop\\x2.txt",result[10]).writeDataToFile();
		new writeDataToFile("C:\\Users\\lenovo\\Desktop\\y2.txt",result[11]).writeDataToFile();
		new writeDataToFile("C:\\Users\\lenovo\\Desktop\\x3.txt",result[12]).writeDataToFile();
		new writeDataToFile("C:\\Users\\lenovo\\Desktop\\y3.txt",result[13]).writeDataToFile();
		long endTime = System.currentTimeMillis();
		System.out.println((endTime-startTime)+"ms");
	}
}
