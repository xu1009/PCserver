package connectDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class JDBC {

	private String user;
	private String pass;
	private String url;
	private Connection conn = null;// ���Ӷ���
	private ResultSet rs = null;// ���������
	private Statement sm = null;// ���ݿ��������

	/**
	 * ���캯��������ݿ��û���������
	 * 
	 * @param user
	 * @param pass
	 */
	public JDBC(String user, String pass) {
		this.user = user;
		this.pass = pass;
		this.url = "jdbc:oracle:thin:@10.108.170.39:1521:db1"; // url
	}

	/**
	 * �������ݿ� //����ע�ͷ��������Զ������ĵ�
	 * 
	 * @return
	 */
	public Connection createConnection() {

		String sDBDriver = "oracle.jdbc.driver.OracleDriver";// ���ݿ�����

		try {
			Class.forName(sDBDriver).newInstance();// ��������
			conn = DriverManager.getConnection(url, user, pass);// �������ݿ�
		} catch (Exception e) { // �쳣����
			System.out.println("���ݿ�����ʧ��"); // �׳�
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * �ر����ݿ�
	 * 
	 * @param conn
	 */
	public void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			System.out.println("���ݿ�ر�ʧ��");
			e.printStackTrace();
		}
	}

	/**
	 * ��������
	 * 
	 * @param insert
	 *            �������
	 * @return
	 */
	public int insert(String insert) {
		conn = createConnection();
		int re = 0;
		try {
			conn.setAutoCommit(false);// disable ���Զ��ύ��ֹ�����쳣ʱ�������������д�����ݿ�

			sm = conn.createStatement(); // �������ݿ��������
			re = sm.executeUpdate(insert);// ���µ����ݿ⣬��������������Ƚ�С�����ʹ��statement��
			if (re < 0) { // ����ʧ��
				conn.rollback(); // ʧ�ܻع�����������
				sm.close();
				closeConnection(conn);
				return re;
			}
			conn.commit(); // ��������
			sm.close();
			closeConnection(conn);
			return re;
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeConnection(conn);
		return 0;

	}

	/**
	 * ��ѯ��� ���ؽ����
	 * 
	 * @param select
	 * @return
	 */
	public ResultSet selectSql(String select) {
		conn = createConnection(); // �������ݿ�����
		try {
			sm = conn.createStatement(); // statement ��������
			rs = sm.executeQuery(select); // ��ѯ�����ؽ��
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
    
	/**
	 * ���ݽ�������
	 * 
	 * @param rs
	 */
	public void printRs(ResultSet rs) {
		int columnsCount = 0;
		boolean f = false;
		try {
			if (!rs.next()) { // rsΪ�շ���
				return;
			}
			ResultSetMetaData rsmd = rs.getMetaData(); // �ö��������ǰ�������Ϣ�����ݣ���������
			columnsCount = rsmd.getColumnCount();// ���ݼ�������
			for (int i = 0; i < columnsCount; i++) {
				System.out.print(rsmd.getColumnLabel(i + 1) + "  "); // �������
			}
			System.out.println();

			while (!f) {
				for (int i = 1; i <= columnsCount; i++) { // ���������
					System.out.print(rs.getString(i) + " ");
				}
				System.out.println();
				if (!rs.next()) {
					f = true;
				}
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeConnection(conn);
	}

	public static void readTxtFile(String filePath) {
		JDBC jDBC = new JDBC("hospital", "hospital");
		try {
               
			String encoding = "UTF-8";
			File file = new File(filePath);
			File[] numOfDic = file.listFiles();
			for (File e : numOfDic) // ����·���������ļ����ļ���
			{

				if (e.isDirectory()) // �ļ���
				{    
					String name = e.getName();					
					String new_path = filePath + "/" + name + "/";
					File file1 = new File(new_path);
					File[] numOfDic1 = file1.listFiles();
					for (File a : numOfDic1) // ��������
					{   
						int count;
						String[] patientData = a.getName().substring(0, 26).split(",");
						String patientUrl = a.getPath();
						
						String destUrl = patientUrl.replaceAll("srcdata", "desdata");
						System.out.println(destUrl);
						File destfile = new File(destUrl);
						cutFile(a,destfile);
						a.delete();
						
						
						ResultSet rs1 = jDBC.selectSql("select count(*) from PATIENTDATA");
						rs1.next();
						count=rs1.getInt(1);
						//ResultSetMetaData rsmd1 = rs1.getMetaData(); // �ö��������ǰ�������Ϣ�����ݣ���������
//						while(rs1.next())
//						{
//							count+=1;
//							System.out.println("rowcount: "+String.valueOf(count));
//						}
						System.out.print(count);
						
						//int rowNum = rsmd1.get;// ���ݼ�������
						// String insert = "insert into PATIENTDATA
						// values("+"\""+patientData[0]+"\""+","+"\""+name+"\""+","+"to_date("+patientData[2]+","+"'yyyy-mm-dd
						// hh24:mi:ss')"+","+"\""+patientData[1]+"\""+","+"\""+patientUrl+"\""+")";
						String insert_patientData = "insert into PATIENTDATA (ID,PATIENT_ID,DEVICE_TYPE,ACQUISITION_TIME,DOCTOR_ID,ORIGINAL_DATA) values("+"\'"+String.valueOf(count)+"\'"+ ","+"\'" + patientData[0] + "\'"
								+ "," + "\'" + name + "\'" + "," + "to_date(" + "\'" + patientData[2] + "\'" + ","
								+ "'yyyy-mm-dd hh24:mi:ss')" + "," + "\'" + patientData[1] + "\'" + "," + "\'"
								+ destUrl + "\'" +")";
						// String insert_tst = "insert into PATIENTDATA
						// values('1','23',to_date('2011-2-28
						// 15~42~56','yyyy-mm-dd hh24:mi:ss'),'23','56')";
						jDBC.insert(insert_patientData);// ����ɹ�
						patientData = null;// ���buff�����´�
					}
					System.out.println("�ļ��У�" + e.getName());

				}
				
				if (e.isFile()&&e.getName().equals("patientAndDoctor.txt")) // �ļ������ļ�ֻ��һ����ֻ���ȡ�ļ����ݣ���д�����ݿ�
				{
					//System.out.println("name:"+);
					InputStreamReader read = new InputStreamReader(new FileInputStream(e), encoding);// ���ǵ������ʽ
					BufferedReader bufferedReader = new BufferedReader(read);
					String lineTxt = null;
					while ((lineTxt = bufferedReader.readLine()) != null) {
						String[] patientAndDocinfo = lineTxt.split(",");// ��ȡ��ǰ�е�����

						String Flag = patientAndDocinfo[0];

						if (Flag.equals("1") == true) // �����ҽ����Ϣ��д��ҽ����
						{
							// System.out.print(Flag.equals("1"));
							String select_DocID = "select * from OPERATOR where OPERATORID=" + "\'"+patientAndDocinfo[1]+"\'";
							ResultSet get_DocID = jDBC.selectSql(select_DocID);
							if (!(get_DocID.next())) {
								String insert_DocInfo = "insert into OPERATOR (OPERATORID,SHOWNAME,DEPARTMENT,WORK_UNIT,\"LEVEL\",PHONE,EMAIL) values("
										+ "\'" + patientAndDocinfo[1] + "\'" + "," + "\'" + patientAndDocinfo[2] + "\'"
										+ "," + "\'" + patientAndDocinfo[3] + "\'" + "," + "\'" + patientAndDocinfo[4]
										+ "\'" + "," + "\'" + patientAndDocinfo[5] + "\'" + "," + "\'"
										+ patientAndDocinfo[6] + "\'" + "," + "\'" + patientAndDocinfo[7] + "\'" + ")";
								System.out.println(jDBC.insert(insert_DocInfo));
							} // ����ɹ�
						} else if (Flag.equals("2") == true) // ����
						{

							String select_PatiID = "select *from PATIENTINFO where OPERATORID = " + "\'"
									+ patientAndDocinfo[1] + "\'";
							ResultSet get_PatientID = jDBC.selectSql(select_PatiID);
							// jDBC.printRs(jDBC.selectSql(select_PatiID)); //
							// ��ӡ����Ϣ
							if (!get_PatientID.next()) // ��ǰ���ݿ�û�иò�����Ϣ����ID
							{
								String insert_patientInfo = "insert into PATIENTINFO(OPERATORID,SHOWNAME,BIRTHDAY,ID_NUMBER,ADRESS,PHONE,EMAIL,CONTACT_NAME,RELATIONSHIP,CONTACT_PHONE,CONTACT_EMAIL) values(" + "\'"
										+ patientAndDocinfo[1] + "\'" + "," + "\'" + patientAndDocinfo[2] + "\'" + ","
										+ "to_date(" + "\'" + patientAndDocinfo[3] + "\'" + ","
										+ "'yyyy-mm-dd hh24:mi:ss')" + "," + "\'" + patientAndDocinfo[4] + "\'" + ","
										+ "\'" + patientAndDocinfo[5] + "\'" + "," + "\'" + patientAndDocinfo[6] + "\'"
										+ "," + "\'" + patientAndDocinfo[7] + "\'" + "," + "\'" + patientAndDocinfo[8]
										+ "\'" + "," + "\'" + patientAndDocinfo[9] + "\'" + "," + "\'"
										+ patientAndDocinfo[10] + "\'" + "," + "\'" + patientAndDocinfo[11] + "\'"
										+ ")";
								System.out.println(jDBC.insert(insert_patientInfo));
							} // ����ɹ�
						}
						patientAndDocinfo = null;
						
					}
					read.close();

				}

			}
			
		} catch (Exception e) {
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
		}

	}

	
	public static void cutFile(File file1, File file2){
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		byte[] bytes = new byte[1024];
		int temp = 0;
		try {
			inputStream = new FileInputStream(file1);
			fileOutputStream = new FileOutputStream(file2);
			while((temp = inputStream.read(bytes)) != -1){
				fileOutputStream.write(bytes, 0, temp);
				fileOutputStream.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	
	
	
	
	
//	public static void main(String argv[]) {
//
//		String filePath = "./src/srcdata";
//
//		readTxtFile(filePath);
//		
//	}

}


