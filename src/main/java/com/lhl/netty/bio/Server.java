package com.lhl.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author luohongli
 * @date 2021/10/8
 */
public class Server {

    public static void main(String[] args) throws IOException {
        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(9090);
        System.out.println("服务器启动");
        while (true) {
            //监听，等待客户端连接
            System.out.println("等待连接....");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            byte[] bytes = new byte[1024];
            socket.getInputStream().read(bytes);
            System.out.println("收到客户端消息：" + new String(bytes));
        }
    }
}
