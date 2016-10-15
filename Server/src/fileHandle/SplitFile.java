package fileHandle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class SplitFile {
	
	/* 
	 * ����measdatainfo.txt�ļ�����measdata.txt�ָ��ͬ���ļ���
	 */
	public static void SplitFileToDifferentDir() throws IOException
	{
		File fileReadInfo = new File("src/srcdata/measdatainfo.txt");
		File fileReadData = new File("src/srcdata/measdata.txt");
		File fileWrite;
		// �����ļ�������
		if(fileReadInfo.exists() && fileReadData.exists()){
			// ��ȡinfo��ֵ
			BufferedReader inInfo = new BufferedReader(new FileReader(fileReadInfo));
			BufferedReader inData = new BufferedReader(new FileReader(fileReadData));
			String lineInfo = inInfo.readLine();
			while(lineInfo != null) // �ܴ���Ϣ�ļ��ж�ȡ����
			{
				// �ö��ŷָ��ַ���
				// 1,162,128,11,1,2016-06-20 15:32:21,1,5890
				// 0  1   2   3 4          5          6  7 
				// 3 ��ҽ��id��4�ǲ���id
				lineInfo =  lineInfo.replaceAll(":", "~");
 				String[] arrInfoSplit = lineInfo.split(",");
 				String fileName = arrInfoSplit[3]+","+arrInfoSplit[4]+","+arrInfoSplit[5]+".txt";
				switch(arrInfoSplit[1]) { // ���ݱ�ʶ������д����ļ�
				case "162":fileWrite = new File("src/srcdata/ECG/" + fileName); break;// �ĵ�
				case "166":fileWrite = new File("src/srcdata/SpO2/"+ fileName); break;// Ѫ��
				default:lineInfo = inInfo.readLine();continue;	// ֱ�ӽ����´ζ�ȡ
				}
				long startLine = Long.parseLong(arrInfoSplit[6])-1;
				long endLine = Long.parseLong(arrInfoSplit[7])+startLine;
				// �ж�Ŀ¼�Ƿ���ڣ��������򴴽�
				if(!fileWrite.getParentFile().exists())
					fileWrite.getParentFile().mkdirs();
				// д������
				FileOutputStream fos =  new FileOutputStream(fileWrite); 
 				for(long i = startLine; i < endLine; ++i)
 				{
 					String lineData = inData.readLine();
 					 // д���ļ�
	       			fos.write(lineData.getBytes());
	       			fos.write("\r\n".getBytes());
 				}
       			fos.close();
 				// ��ȡ��һ������
 				lineInfo = inInfo.readLine();
				
			}
			inInfo.close();
			inData.close();
		}		
	}
}
