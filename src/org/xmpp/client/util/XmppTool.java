package org.xmpp.client.util;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.OrFilter;



public class XmppTool {

	private static XMPPConnection con = null;
	
	private static void openConnection() {
		try {
			//url、端口，也可以设置连接的服务器名字，地址，端口，用户。
			ConnectionConfiguration connConfig = new ConnectionConfiguration("192.168.1.137", 5222);

			con = new XMPPConnection(connConfig);
			con.connect();
			con.addConnectionListener(new ConnectionListener() {
	            @Override
				public void connectionClosed() {
	                
	            	System.out.println("关闭连接");
	            }

	            @Override
				public void connectionClosedOnError(Exception e) {
	            	System.out.println("关闭连接异常");
	            }

	            @Override
				public void reconnectingIn(int seconds) {
	            	System.out.println("重新连接"+seconds);
	            }

	            @Override
				public void reconnectionFailed(Exception e) {
	            	System.out.println("重新连接失败");
	            }

	            @Override
				public void reconnectionSuccessful() {
	            	System.out.println("重新连接成功");
	            }
	        });
			

			/*//消息监听

			PacketFilter rosterPF = new PacketTypeFilter(RosterPacket.class);
	        PacketFilter IQPF = new PacketTypeFilter(IQ.class);
	        PacketFilter MSGPF = new PacketTypeFilter(Message.class);
	        PacketFilter PresencePF = new PacketTypeFilter(Presence.class);
	        PacketFilter AMPF = new PacketTypeFilter(AuthMechanism.class);
	        PacketFilter REPF = new PacketTypeFilter(Response.class);
	        
	        OrFilter allPF = new OrFilter(rosterPF, IQPF);
	        allPF.addFilter(MSGPF);
	        allPF.addFilter(PresencePF);
	        allPF.addFilter(AMPF);
	        allPF.addFilter(REPF);
	        PacketListener myListener = new PacketListener() {
	            public void processPacket(Packet pk) {
	            	if(pk instanceof Message){
	            		Message msg=(Message) pk;
	            		System.out.println("receive message : " +msg.getFrom()+msg.getBody());

	            		//接受到消息后存储到数据库，然后刷新界面。增加未读取短信条数。
	            		//保存
	            		
	            		
	            	}
	            }
	        };
	        con.addPacketListener(myListener, allPF);*/

			
			
		}
		catch (XMPPException xe) 
		{
			xe.printStackTrace();
		}
	}

	public static XMPPConnection getConnection() {
		if (con == null) {
			openConnection();
		}
		return con;
	}

	public static void closeConnection() {
		con.disconnect();
		con = null;
	}

	public static void addPacketListener(PacketListener myListener,
			OrFilter allPF) {
		con.addPacketListener(myListener, allPF);
	}
}
