/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.net.*;
import java.io.*;

/**
 *
 * @author admin_
 */

public class OneClientController {
    public PrintWriter out;
    public BufferedReader in;
    private double integral;
    public double minX;
    public double maxX;
    public double step;
    public Socket thisSocket;
    public int idClient;
    
    public void setValueCalculating(double MinX, double MaxX, double Step) {
        minX = MinX;
        maxX = MaxX;
        step = Step;
    }
    public OneClientController(Socket Sock, int numberClient){
        thisSocket = Sock;
        idClient = numberClient;
        try {
            out = new PrintWriter(thisSocket.getOutputStream());
        }
        catch (IOException exc) {
            System.out.println(idClient + " : Ошибка. Не удалось выполнить получение потока для вывода (OutputStream).\n\t" +
                               "Сокет не подключен или возникла ошибка ввода вывода");
            //System.out.println("При socketClient.getOutputStream() в одном из клиентских потоков произошла ошибка. Сокет не подключен или возникла ошибка ввода вывода");
            //throw new ServerException("ClientThread.ClientThread()", "Ошибка при PrintWriter(socketClient.getOutputStream())" );
        }
        try {
            in = new BufferedReader(new InputStreamReader(thisSocket.getInputStream()));
        }
        catch (IOException exc) {
            System.out.println(idClient + " : Ошибка: не удалось выполнить получение потока для ввода (getInputStream).\n\t" +
                               "Сокет не подключен или возникла ошибка ввода вывода");
            //throw new ServerException("ClientThread.ClientThread()", "BufferedReader(new InputStreamReader(socketClient.getInputStream()));");
        }
    }
    public int close() {
        if (out != null) {
            out.close();
        }
        if (in != null) {
            try {
                in.close();
            }
            catch (IOException excIO) {
                return 1;
            }
        }
        return 0;
    }
    
    public double getIntegral() { return integral;}
}
