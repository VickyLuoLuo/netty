package com.lhl.netty.chat.handler;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("somethingChannelInitializer")
public class NettyWebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private TextWebSocketFrameHandler textWebSocketFrameHandler;

    @Override
    public void initChannel(SocketChannel ch) throws Exception {//2
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new HttpServerCodec()); // http解编码器
        pipeline.addLast(new HttpObjectAggregator(64*1024)); // 补充http解编码器
        pipeline.addLast(new ChunkedWriteHandler()); // 保证队列中的每一个元素是一次性发送
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws")); //
        pipeline.addLast(textWebSocketFrameHandler); // 自定义ChannelHandler

    }
}