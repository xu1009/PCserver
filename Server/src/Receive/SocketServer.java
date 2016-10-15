package Receive;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

import com.mathworks.toolbox.javabuilder.MWException;

import Ps.Class1;

public class SocketServer {

	private final static Logger logger = Logger.getLogger(SocketServer.class.getName());
	  
	  public static void main(String[] args) {
		  
//		  Thread data = new Thread("data"){
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				//处理数据
//				while(true){
//			      try {
//			    	  Class1 c1=new Class1();
//				      	Object[] result=new Object[5];
//				      	String File= "D:\\chromedownloads\\new\\new\\measdata.txt";
//						result = c1.Ps(14, File);
//						new writeDataToFile("E:\\x0.txt",result[6]).writeDataToFile();
//						new writeDataToFile("E:\\y0.txt",result[7]).writeDataToFile();
//						new writeDataToFile("E:\\x1.txt",result[8]).writeDataToFile();
//						new writeDataToFile("E:\\y1.txt",result[9]).writeDataToFile();
//						new writeDataToFile("E:\\x2.txt",result[10]).writeDataToFile();
//						new writeDataToFile("E:\\y2.txt",result[11]).writeDataToFile();
//						new writeDataToFile("E:\\x3.txt",result[12]).writeDataToFile();
//						new writeDataToFile("E:\\y3.txt",result[13]).writeDataToFile();
//						
//						System.out.println("已处理！");
//						
////						Thread.sleep(10000);
//					} catch (MWException | InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//			      
//				}
//			}
//			  	
//		  };
	    
	    Thread t9 = new Thread("mobile2PC"){

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
			        	  logger.log(Level.INFO, "已连接");
			        	  //接收文件，储存在D盘，文件名为123.txt
			        	  receiveFile(socketChannel, new File("src/newdata/measdata.txt"));
			        	  //发送文件，储存在E盘，文件名为456.txt
			        	  //使用时取消注释即可，与receiveFile不能同时用
//			        	  sendFile(socketChannel, new File("E:/456.txt"));
			          }catch(Exception ex){
			            logger.log(Level.SEVERE, "re1", ex);
			          } finally {
			            try {
			              socketChannel.close();
			            } catch(Exception ex) {
			              logger.log(Level.SEVERE, "re2", ex);
			            }
			          }
			        }
			        try {
				    	  Class1 c1=new Class1();
					      	Object[] result=new Object[5];
					      	//String File= "D:\\chromedownloads\\new\\new\\measdata.txt";
					      	String File = "D:\\123.txt";
							result = c1.Ps(14, File);
							new writeDataToFile("E:\\x0.txt",result[6]).writeDataToFile();
							new writeDataToFile("E:\\y0.txt",result[7]).writeDataToFile();
							new writeDataToFile("E:\\x1.txt",result[8]).writeDataToFile();
							new writeDataToFile("E:\\y1.txt",result[9]).writeDataToFile();
							new writeDataToFile("E:\\x2.txt",result[10]).writeDataToFile();
							new writeDataToFile("E:\\y2.txt",result[11]).writeDataToFile();
							new writeDataToFile("E:\\x3.txt",result[12]).writeDataToFile();
							new writeDataToFile("E:\\y3.txt",result[13]).writeDataToFile();
							
							System.out.println("已处理！");
							
//							Thread.sleep(10000);
						} catch (MWException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
	    
	    Thread t0 = new Thread("PC2mobileY0"){

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
				      serverSocketChannel.socket().bind(new InetSocketAddress(1900));
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
			        	  logger.log(Level.INFO, "已连接");
			        	  //接收文件，储存在D盘，文件名为123.txt
//			        	  receiveFile(socketChannel, new File("D:/123.txt"));
			        	  //发送文件，储存在E盘，文件名为456.txt
			        	  //使用时取消注释即可，与receiveFile不能同时用
			        	  sendFile(socketChannel, new File("E:/y0.txt"));
			          }catch(Exception ex){
			            logger.log(Level.SEVERE, "re1", ex);
			          } finally {
			            try {
			              socketChannel.close();
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
	    
	    Thread t1 = new Thread("PC2mobileY1"){

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
				      serverSocketChannel.socket().bind(new InetSocketAddress(1901));
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
			        	  logger.log(Level.INFO, "已连接");
			        	  //接收文件，储存在D盘，文件名为123.txt
//			        	  receiveFile(socketChannel, new File("D:/123.txt"));
			        	  //发送文件，储存在E盘，文件名为456.txt
			        	  //使用时取消注释即可，与receiveFile不能同时用
			        	  sendFile(socketChannel, new File("E:/y1.txt"));
			          }catch(Exception ex){
			            logger.log(Level.SEVERE, "re1", ex);
			          } finally {
			            try {
			              socketChannel.close();
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
	    
	    Thread t2 = new Thread("PC2mobileY2"){

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
				      serverSocketChannel.socket().bind(new InetSocketAddress(1902));
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
			        	  logger.log(Level.INFO, "已连接");
			        	  //接收文件，储存在D盘，文件名为123.txt
//			        	  receiveFile(socketChannel, new File("D:/123.txt"));
			        	  //发送文件，储存在E盘，文件名为456.txt
			        	  //使用时取消注释即可，与receiveFile不能同时用
			        	  sendFile(socketChannel, new File("E:/y2.txt"));
			          }catch(Exception ex){
			            logger.log(Level.SEVERE, "re1", ex);
			          } finally {
			            try {
			              socketChannel.close();
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
	    
	    Thread t3 = new Thread("PC2mobileY3"){

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
				      serverSocketChannel.socket().bind(new InetSocketAddress(1903));
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
			        	  logger.log(Level.INFO, "已连接");
			        	  //接收文件，储存在D盘，文件名为123.txt
//			        	  receiveFile(socketChannel, new File("D:/123.txt"));
			        	  //发送文件，储存在E盘，文件名为456.txt
			        	  //使用时取消注释即可，与receiveFile不能同时用
			        	  sendFile(socketChannel, new File("E:/y3.txt"));
			          }catch(Exception ex){
			            logger.log(Level.SEVERE, "re1", ex);
			          } finally {
			            try {
			              socketChannel.close();
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
	    
	    t0.start();
//	    data.start();
	    t1.start();
	    t2.start();
	    t3.start();
	    t9.start();
	  }

	  private static String receiveData(SocketChannel socketChannel) throws IOException {
	    String string = null;
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ByteBuffer buffer = ByteBuffer.allocate(1024);
	    
	    try {
	      byte[] bytes;
	      int size = 0;
	      while ((size = socketChannel.read(buffer)) >= 0) {
	        buffer.flip();
	        bytes = new byte[size];
	        buffer.get(bytes);
	        baos.write(bytes);
	        buffer.clear();
	      }
	      bytes = baos.toByteArray();
	      string = new String(bytes);
	    }catch(Exception ex){
	      logger.log(Level.SEVERE, "7", ex);
	    }finally {
	      try {
	        baos.close();
	      } catch(Exception ex) {
	        logger.log(Level.SEVERE, "8", ex);
	      }
	    }
	    return string;
	  }

	  private static void sendData(SocketChannel socketChannel, String string) throws IOException {
	    byte[] bytes = string.getBytes();
	    ByteBuffer buffer = ByteBuffer.wrap(bytes);
	    socketChannel.write(buffer);
	    socketChannel.socket().shutdownOutput();
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

	  private static void sendFile(SocketChannel socketChannel, File file) throws IOException {
	    FileInputStream fis = null;
	    FileChannel channel = null;
	    try {
	      fis = new FileInputStream(file);
	      channel = fis.getChannel();
	      ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
	      int size = 0;
	      while ((size = channel.read(buffer)) != -1) {
	        buffer.rewind();
	        buffer.limit(size);
	        socketChannel.write(buffer);
	        buffer.clear();
	      }
	      socketChannel.socket().shutdownOutput();
	    }catch(Exception ex){
	      logger.log(Level.SEVERE, "12", ex);
	    } finally {
	      try {
	        channel.close();
	      } catch(Exception ex) {
	        logger.log(Level.SEVERE, "13", ex);
	      }
	      try {
	        fis.close();
	      } catch(Exception ex) {
	        logger.log(Level.SEVERE, "14", ex);
	      }
	    }
	  }

}
