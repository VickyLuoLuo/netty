package com.lhl.netty.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class DatagramChannelDemo {

    public static void main(String[] args) throws IOException {
        // 获取通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        // 设置为非阻塞
        datagramChannel.configureBlocking(false);
        // 绑定监听IP和端口
        datagramChannel.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(),5252));

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        SocketAddress clientAddr = datagramChannel.receive(byteBuffer);
        byteBuffer.flip();
        datagramChannel.send(byteBuffer, new InetSocketAddress(InetAddress.getLocalHost(),5252));
        byteBuffer.clear();
    }
}
