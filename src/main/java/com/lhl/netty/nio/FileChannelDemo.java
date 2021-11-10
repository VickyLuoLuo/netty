package com.lhl.netty.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo {

    public static void main(String[] args) throws IOException {
        String srcFile = "demo.txt";
        String destFile = "demo.txt";
        // 创建文件输入流
        FileInputStream fis = new FileInputStream(srcFile);
        // 获取文件流的通道
        FileChannel inChannel = fis.getChannel();

        // 创建文件输出流
        FileOutputStream fos = new FileOutputStream(destFile);
        // 获取文件流的通道
        FileChannel outChannel = fis.getChannel();

        // 创建RandomAccessFile随机访问对象
        RandomAccessFile rafile = new RandomAccessFile(srcFile, "rw");
        // 获取文件流的通道
        FileChannel raFileChannel = rafile.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int length = -1;
        // 调用通道的read方法，从通道读取数据写入缓冲区，并返回数据。
        while (-1 != (length = raFileChannel.read(byteBuffer))) {
            // TODO
        }
        // 刚写完要翻转成读取模式
        byteBuffer.flip();
        int outlength = 0;
        // 调用通道的write方法，读取缓冲区的数据写入通道，并返回写入的字节数
        while (0 != (outlength = raFileChannel.write(byteBuffer))) {
            System.out.println("写入字节数：" + outlength);
        }
        inChannel.close();
        inChannel.force(true);
    }
}
