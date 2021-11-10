package com.lhl.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 反应器
 */
public class Reactor implements Runnable {

    final Selector selector;
    final ServerSocketChannel serverSocketChannel;

    Reactor(int port) throws IOException {
        // 打开选择器、serverSocket，连接监听通道
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        // 注册通道到选择器，设置感兴趣的事件并返回选择键，通过选择键可以获取通道和选择器
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 相当于setter，将新连接处理器作为附件，绑定到sk选择器
        sk.attach(new AcceptorHandler());
    }

    @Override
    public void run() {
        // 选择器轮询
        while (!Thread.interrupted()) {
            try {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext()) {
                    // 反应器负责dispatch收到的事件
                    dispatch((SelectionKey) it.next());
                }
                selected.clear();
            } catch (IOException e) {
            }
        }
    }

    void dispatch(SelectionKey sk) {
        // 相当于getter，获取新连接处理器，并启动
        Runnable acceptorHandler = (Runnable) sk.attachment();
        if (acceptorHandler != null) {
            acceptorHandler.run();
        }
    }

    /**
     * 新连接处理器
     */
    class AcceptorHandler implements Runnable {

        @Override
        public void run() {
            try {
                // 接受新连接
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null) {
                    // 为新连接创建输入输出的handler处理器
                    new IOHandler(selector, socketChannel);
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 负责socket的数据输入、业务处理、结果输出
     */
    final class IOHandler implements Runnable {
        static final int READING = 0;
        static final int SENDING = 1;
        final SocketChannel socketChannel;
        final SelectionKey sk;
        ByteBuffer input = ByteBuffer.allocate(1024);
        ByteBuffer output = ByteBuffer.allocate(1024);
        int state = READING;

        IOHandler(Selector selector, SocketChannel socketChannel) throws IOException {
            this.socketChannel = socketChannel;
            socketChannel.configureBlocking(false);
            // 获取选择键
            sk = socketChannel.register(selector, 0);
            // 将handler处理器作为选择键的附件
            sk.attach(this);
            // 注册读写就绪事件
            sk.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
            selector.wakeup();
        }

        @Override
        public void run() {
            // 处理输入和输出
            try {
                if (state == READING) {
                    read();
                } else if (state == SENDING) {
                    send();
                }
            } catch (IOException e) {
            }
        }

        /**
         * 处理输入事件
         * @throws IOException
         */
        void  read() throws IOException {
            int length = 0;
            // 等待读取完成
            while ((length = socketChannel.read(input)) > 0) {
                System.out.println(new String(input.array(), 0, length));
            }
            // 切换状态，注册write就绪事件
            input.flip();
            state = SENDING;
            sk.interestOps(SelectionKey.OP_WRITE);
        }

        /**
         * 处理输出事件
         * @throws IOException
         */
        void  send() throws IOException {
            socketChannel.write(output);
            output.clear();
            state = READING;
            sk.interestOps(SelectionKey.OP_READ);
        }
    }


    public static void main(String[] args) throws IOException {
        new Reactor(5252).run();
    }
}


