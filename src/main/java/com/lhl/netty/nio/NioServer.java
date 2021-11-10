package com.lhl.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NioServer {


    static List<SocketChannel> channelList = new ArrayList<>();

    public static void startServer() throws IOException {

        // 获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 绑定连接
        serverSocketChannel.bind(new InetSocketAddress(5252));
        System.out.println("服务器启动成功");

        // 轮询感兴趣的IO就绪事件（选择键集合）
        while (true) {

            // 获取客户端连接
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (null != socketChannel) {
                System.out.println("连接成功");
                // 设置SocketChannel为非阻塞模式。NIO的非阻塞是由操作系统内部实现的，底层调用了系统内核的accept方法
                socketChannel.configureBlocking(false);

                channelList.add(socketChannel);
            }
            // 获取选择键集合
            Iterator<SocketChannel> channels = channelList.iterator();
            while (channels.hasNext()) {
                // 获取单个的选择键并处理
                SocketChannel channel = channels.next();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                // 调用通道的read方法，从通道读取数据写入缓冲区，并返回读取到的数据
                int length = channel.read(byteBuffer);
                if (length > 0) {
                    System.out.println("接收到消息：" + new String(byteBuffer.array(), 0, length));
                } else if (length == -1) {
                    channels.remove();
                    System.out.println("客户端断开连接");
                }

            }
        }
    }

    public static void main(String[] args) throws IOException {
        startServer();
    }
}
