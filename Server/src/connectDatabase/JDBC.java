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
	private Connection conn = null;// 连接对象
	private ResultSet rs = null;// 结果集对象
	private Statement sm = null;// 数据库操作对象

	/**
	 * 构造函数获得数据库用户名和密码
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
	 * 连接数据库 //这种注释方法可以自动生成文档
	 * 
	 * @return
	 */
	public Connection createConnection() {

		String sDBDriver = "oracle.jdbc.driver.OracleDriver";// 数据库驱动

		try {
			Class.forName(sDBDriver).newInstance();// 加载驱动
			conn = DriverManager.getConnection(url, user, pass);// 连接数据库
		} catch (Exception e) { // 异常处理
			System.out.println("数据库连接失败"); // 抛出
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭数据库
	 * 
	 * @param conn
	 */
	public void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			System.out.println("数据库关闭失败");
			e.printStackTrace();
		}
	}

	/**
	 * 插入数据
	 * 
	 * @param insert
	 *            插入语句
	 * @return
	 */
	public int insert(String insert) {
		conn = createConnection();
		int re = 0;
		try {
			conn.setAutoCommit(false);// disable 非自动提交防止出现异常时还将错误的数据写入数据库

			sm = conn.createStatement(); // 创建数据库操作对象
			re = sm.executeUpdate(insert);// 更新到数据库，这里更新数据量比较小，因此使用statement，
			if (re < 0) { // 插入失败
				conn.rollback(); // 失败回滚，撤销更改
				sm.close();
				closeConnection(conn);
				return re;
			}
			conn.commit(); // 插入正常
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
	 * 查询语句 返回结果集
	 * 
	 * @param select
	 * @return
	 */
	public ResultSet selectSql(String select) {
		conn = createConnection(); // 创建数据库连接
		try {
			sm = conn.createStatement(); // statement 操作对象
			rs = sm.executeQuery(select); // 查询并返回结果
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
    
	/**
	 * 根据结果集输出
	 * 
	 * @param rs
	 */
	public void printRs(ResultSet rs) {
		int columnsCount = 0;
		boolean f = false;
		try {
			if (!rs.next()) { // rs为空返回
				return;
			}
			ResultSetMetaData rsmd = rs.getMetaData(); // 该对象包含当前表的列信息，数据，数据类型
			columnsCount = rsmd.getColumnCount();// 数据集的列数
			for (int i = 0; i < columnsCount; i++) {
				System.out.print(rsmd.getColumnLabel(i + 1) + "  "); // 输出列名
			}
			System.out.println();

			while (!f) {
				for (int i = 1; i <= columnsCount; i++) { // 输出列内容
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
			for (File e : numOfDic) // 遍历路径下所有文件及文件夹
			{

				if (e.isDirectory()) // 文件夹
				{    
					String name = e.getName();					
					String new_path = filePath + "/" + name + "/";
					File file1 = new File(new_path);
					File[] numOfDic1 = file1.listFiles();
					for (File a : numOfDic1) // 病人数据
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
						//ResultSetMetaData rsmd1 = rs1.getMetaData(); // 该对象包含当前表的列信息，数据，数据类型
//						while(rs1.next())
//						{
//							count+=1;
//							System.out.println("rowcount: "+String.valueOf(count));
//						}
						System.out.print(count);
						
						//int rowNum = rsmd1.get;// 数据集的列数
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
						jDBC.insert(insert_patientData);// 插入成功
						patientData = null;// 清空buff接收下次
					}
					System.out.println("文件夹：" + e.getName());

				}
				
				if (e.isFile()&&e.getName().equals("patientAndDoctor.txt")) // 文件，该文件只有一个，只需读取文件内容，并写入数据库
				{
					//System.out.println("name:"+);
					InputStreamReader read = new InputStreamReader(new FileInputStream(e), encoding);// 考虑到编码格式
					BufferedReader bufferedReader = new BufferedReader(read);
					String lineTxt = null;
					while ((lineTxt = bufferedReader.readLine()) != null) {
						String[] patientAndDocinfo = lineTxt.split(",");// 读取当前行的内容

						String Flag = patientAndDocinfo[0];

						if (Flag.equals("1") == true) // 如果是医生信息就写入医生表
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
							} // 插入成功
						} else if (Flag.equals("2") == true) // 病人
						{

							String select_PatiID = "select *from PATIENTINFO where OPERATORID = " + "\'"
									+ patientAndDocinfo[1] + "\'";
							ResultSet get_PatientID = jDBC.selectSql(select_PatiID);
							// jDBC.printRs(jDBC.selectSql(select_PatiID)); //
							// 打印表信息
							if (!get_PatientID.next()) // 当前数据库没有该病人信息主键ID
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
							} // 插入成功
						}
						patientAndDocinfo = null;
						
					}
					read.close();

				}

			}
			
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
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


