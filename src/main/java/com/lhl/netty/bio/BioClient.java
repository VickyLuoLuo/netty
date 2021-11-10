package com.lhl.netty.bio;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author luohongli
 * @date 2021/10/8
 */
public class BioClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9090);
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        socket.getOutputStream().write(s.getBytes());
        socket.close();
    }
}
