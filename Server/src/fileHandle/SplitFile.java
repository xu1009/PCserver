package fileHandle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class SplitFile {
	
	/* 
	 * 解析measdatainfo.txt文件，将measdata.txt分割到不同的文件中
	 */
	public static void SplitFileToDifferentDir() throws IOException
	{
		File fileReadInfo = new File("src/srcdata/measdatainfo.txt");
		File fileReadData = new File("src/srcdata/measdata.txt");
		File fileWrite;
		// 两个文件都存在
		if(fileReadInfo.exists() && fileReadData.exists()){
			// 读取info的值
			BufferedReader inInfo = new BufferedReader(new FileReader(fileReadInfo));
			BufferedReader inData = new BufferedReader(new FileReader(fileReadData));
			String lineInfo = inInfo.readLine();
			while(lineInfo != null) // 能从信息文件中读取到行
			{
				// 用逗号分隔字符串
				// 1,162,128,11,1,2016-06-20 15:32:21,1,5890
				// 0  1   2   3 4          5          6  7 
				// 3 是医生id，4是病人id
				lineInfo =  lineInfo.replaceAll(":", "~");
 				String[] arrInfoSplit = lineInfo.split(",");
 				String fileName = arrInfoSplit[3]+","+arrInfoSplit[4]+","+arrInfoSplit[5]+".txt";
				switch(arrInfoSplit[1]) { // 根据标识符决定写入的文件
				case "162":fileWrite = new File("src/srcdata/ECG/" + fileName); break;// 心电
				case "166":fileWrite = new File("src/srcdata/SpO2/"+ fileName); break;// 血氧
				default:lineInfo = inInfo.readLine();continue;	// 直接进入下次读取
				}
				long startLine = Long.parseLong(arrInfoSplit[6])-1;
				long endLine = Long.parseLong(arrInfoSplit[7])+startLine;
				// 判断目录是否存在，不存在则创建
				if(!fileWrite.getParentFile().exists())
					fileWrite.getParentFile().mkdirs();
				// 写入数据
				FileOutputStream fos =  new FileOutputStream(fileWrite); 
 				for(long i = startLine; i < endLine; ++i)
 				{
 					String lineData = inData.readLine();
 					 // 写入文件
	       			fos.write(lineData.getBytes());
	       			fos.write("\r\n".getBytes());
 				}
       			fos.close();
 				// 读取下一行数据
 				lineInfo = inInfo.readLine();
				
			}
			inInfo.close();
			inData.close();
		}		
	}
}
