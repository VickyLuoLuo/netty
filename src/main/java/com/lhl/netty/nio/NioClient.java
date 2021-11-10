package com.lhl.netty.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 使用NIO实现Discard客户端功能：
 *      客户端首先建立到服务器的连接，发送一些简单的数据，然后直接关闭连接。
 */
public class NioClient {

    public static void startClient() throws IOException {
        InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(),5656);
        // 获取通道
        SocketChannel socketChannel = SocketChannel.open(address);
        // 设置为非阻塞
        socketChannel.configureBlocking(false);
        // 不断自旋，等待连接完成
        while (!socketChannel.finishConnect()) {
        }
        System.out.println("客户端连接成功");
        // 分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello nio".getBytes());
//        byteBuffer.flip();
        // 发送到服务器
        socketChannel.write(byteBuffer);
        // 终止输出方法，向对方发送一个输出的结束标志
        socketChannel.shutdownOutput();
        // 关闭套接字连接
        socketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        startClient();
    }
}
