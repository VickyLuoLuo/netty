package com.lhl.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioSelectorServer {
    public static void startServer() throws IOException {

        // 获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 绑定连接
        serverSocketChannel.bind(new InetSocketAddress(5656));
        System.out.println("服务器启动成功");

        // 获取选择器
        Selector selector = Selector.open(); // epoll
        // 将通道注册的“接收新连接”IO事件注册到选择器上
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 轮询感兴趣的IO就绪事件（选择键集合）
        while (selector.select() > 0) { // epoll-wait
            // 获取选择键集合
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                // 获取单个的选择键并处理
                SelectionKey selectedKey = selectedKeys.next();
                if (selectedKey.isAcceptable()) {
                    // 若选择键的IO事件是“连接就绪”，就获取客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 切换为非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 将新连接的通道可读事件注册到选择器上
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("连接成功");
                } else if (selectedKey.isReadable()) {
                    // 若选择键的IO事件是“可读”，就读取数据
                    SocketChannel socketChannel = (SocketChannel) selectedKey.channel();
                    // 读取数据，然后丢弃
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    // 调用通道的read方法，从通道读取数据写入缓冲区，并返回读取到的数据
                    int length = socketChannel.read(byteBuffer);
                    if (length > 0) {
                        System.out.println("接收到消息：" + new String(byteBuffer.array(), 0, length));
                    } else if (length == -1) {
                        System.out.println("客户端断开连接");
                        socketChannel.close();
                    }
                }
                // 移除选择键
                selectedKeys.remove();
            }
        }
        // 关闭连接
        serverSocketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        startServer();
    }
}
