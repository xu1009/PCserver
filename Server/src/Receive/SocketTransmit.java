package Receive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import fileHandle.SplitFile;
import connectDatabase.JDBC;
public class SocketTransmit {

	private final static Logger logger = Logger.getLogger(SocketTransmit.class.getName());
	  
	  public static void main(String[] args) {
		  

		  Thread t9 = new Thread("mobile2PC"){
			    int fileFlag = 1;
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Selector selector = null;
				    ServerSocketChannel serverSocketChannel = null;
					try {
				    	//创建选择器
					      selector = Selector.open();
					      //打开监听信道
					      serverSocketChannel = ServerSocketChannel.open();
					      //设为非阻塞模式
					      serverSocketChannel.configureBlocking(false);
					      //
					      serverSocketChannel.socket().setReuseAddress(true);
					      //与本地端口绑定
					      serverSocketChannel.socket().bind(new InetSocketAddress(1991));
					      //将选择器绑定到监听信道,只有非阻塞信道才可以注册选择器.并在注册过程中指出该信道可以进行Accept操作  
					      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
				      while (selector.select() > 0) {
				        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				        while (it.hasNext()) {
				          SelectionKey readyKey = it.next();
				          it.remove();
				          SocketChannel socketChannel = null;
				          try {
				        	  socketChannel = ((ServerSocketChannel) readyKey.channel()).accept();
				        	  logger.log(Level.INFO, "------------已连接--------------");
				        	  if(fileFlag == 1)
				        	  {   //1.接收info文件，文件名为measdatainfo.txt
					        	  logger.log(Level.INFO, "measdatainfo.txt开始接收");
					        	  receiveFile(socketChannel, new File("src/srcdata/measdatainfo.txt"));
					        	  fileFlag = 2;
					        	  logger.log(Level.INFO, "measdatainfo.txt接收完毕");

				        	  }
				        	  else if(fileFlag == 2) 
				        	  {   //2.接收data文件，文件名为measdata.txt
					         	  logger.log(Level.INFO, "measdata.txt开始接收");
					        	  receiveFile(socketChannel, new File("src/srcdata/measdata.txt"));
					        	  fileFlag = 3;
					        	  logger.log(Level.INFO, "measdata.txt接收完毕");
				        	  }
				        	  else if(fileFlag == 3)
				        	  {   
				        		  //3.接受医生和病人信息
				        		  logger.log(Level.INFO, "patientAndDoctor.txt开始接收");
					        	  receiveFile(socketChannel, new File("src/srcdata/patientAndDoctor.txt"));
					        	  fileFlag = 1;
					        	  SplitFile.SplitFileToDifferentDir();
					        	  JDBC.readTxtFile("./src/srcdata");
					        	  logger.log(Level.INFO, "patientAndDoctor.txt接收完毕");				        		  
				        	  }
				          }catch(Exception ex){
				            logger.log(Level.SEVERE, "re1", ex);
				          } finally {
				            try {
				              socketChannel.close();
				              logger.log(Level.INFO, "--------------断开连接-------------");
				            } catch(Exception ex) {
				              logger.log(Level.SEVERE, "re2", ex);
				            }
				          }
				        }
				      }
				      
				      
				    } catch (ClosedChannelException ex) {
				      logger.log(Level.SEVERE, "3", ex);
				    } catch (IOException ex) {
				      logger.log(Level.SEVERE, "4", ex);
				    } finally {
				      try {
				        selector.close();
				      } catch(Exception ex) {
				        logger.log(Level.SEVERE, "5", ex);
				      }
				      try {
				        serverSocketChannel.close();
				      } catch(Exception ex) {
				        logger.log(Level.SEVERE, "6", ex);
				      }
				    }
				}
		    	
		    };
		    
		  t9.start();
	  }
	  
	  
	  private static void receiveFile(SocketChannel socketChannel, File file) throws IOException {
	    FileOutputStream fos = null;
	    FileChannel channel = null;
	    
	    try {
	      fos = new FileOutputStream(file);
	      channel = fos.getChannel();
	      ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

	      int size = 0;
	      while ((size = socketChannel.read(buffer)) != -1) {
	        buffer.flip();
	        if (size > 0) {
	          buffer.limit(size);
	          channel.write(buffer);
	          buffer.clear();
	        }
	      }
	    }catch(Exception ex){
	      logger.log(Level.SEVERE, "9", ex);
	    } finally {
	      try {
	        channel.close();
	      } catch(Exception ex) {
	        logger.log(Level.SEVERE, "10", ex);
	      }
	      try {
	        fos.close();
	      } catch(Exception ex) {
	        logger.log(Level.SEVERE, "11", ex);
	      }
	    }
	  }

	 
}
