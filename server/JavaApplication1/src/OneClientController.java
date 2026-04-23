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

public class OneClientController extends Thread {
    private double integral;
    private PrintWriter writer;
    private BufferedReader reader;
    private double minX;
    private double maxX;
    private double step;
    private Socket thisSocket;
    
    private void getStrResult (String in) {
        String parse [] = in.split("\\\\");
        integral = Double.parseDouble(parse[1]);
    }
    private String getStrForSend() {
        return Double.toString(minX ) + "\\" + Double.toString(maxX) + "\\" + Double.toString(step);
    }
    public void setValueCalculating(double MinX, double MaxX, double Step) {
        minX = MinX;
        maxX = MaxX;
        step = Step;
    }
    public OneClientController(Socket Sock){
        thisSocket = Sock;
    }
    @Override
    public void run() {
        try {
            writer = new PrintWriter(thisSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(thisSocket.getInputStream()));
            writer.print(getStrForSend());
            getStrResult(reader.readLine());
        }
        catch(IOException exc) {
            System.out.println("Ошибка при thisSocket.getOutputStream() или new InputStreamReader(thisSocket.getInputStream()) или reader.readLine()");
        }
    }
    public double getIntegral() { return integral;}
}
