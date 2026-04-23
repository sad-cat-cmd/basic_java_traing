/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.*;
import java.net.*;
import java.util.LinkedList;
/**
 *
 * @author admin_
 */
public class NetworkControlerServ {
    private double integral = 0.0;
    private boolean flagServerWork = false;
    private int countClient = 0;
    private int port = 8080;
    private ServerSocket serverSocket;
    private LinkedList <OneClientController> listClients;
    
    private final Object clientsLock = new Object();
    
    private void acceptClient() {
        try {
            while (flagServerWork) {
            Socket newSocket = serverSocket.accept();
            OneClientController newOneClientController = new OneClientController(newSocket);
            synchronized (clientsLock) {
                listClients.add(newOneClientController);
                countClient++;
            }
        }
            
        }
        catch (IOException exc) {
            //
        }
    }
    private double calculatingIntegral(double maxX, double minX, double step) throws ServerException {
        TimeController newTimeController = new TimeController();
        if (countClient == 0) {
            throw new ServerException("NetworkController.calculatingIntegral()", "Error: count client");
        }
        synchronized (clientsLock) {
            double arrMinX [] = new double [countClient];
            double arrMaxX [] = new double [countClient];
            double difCalculating = (maxX - minX) / countClient;
            double localMinX = minX;
            double localMaxX = minX;
            
            for (int i = 0; i < countClient; i++) {
                localMaxX += difCalculating;
                arrMinX[i] = localMinX;
                arrMaxX[i] = localMaxX;
                localMinX = localMaxX;
            }
            
            for (int i = 0; i < listClients.size(); i++) {
                listClients.get(i).setValueCalculating(arrMinX[i], arrMaxX[i], step);
                listClients.get(i).start();
            }
            
            try {
                for (int i = 0; i < listClients.size(); i++) {
                    listClients.get(i).join();
                    integral += listClients.get(i).getIntegral();
                }
            }
            catch (InterruptedException exc) {
                System.out.println("Ошибка ожидания потока");
                throw new ServerException("NetworkController.calculatingIntegral()", "Error with join thread");
                //
            }
            finally {
                System.out.println("Время выполнения подсчета: " + newTimeController.getTimeIntervalNs());
                return integral;
            }
        }
        
    }
    public void startServer () throws ServerException {
        if (flagServerWork == false) {
            System.out.println("Сервер уже запущен");
            return;
        }
        try {
            serverSocket = new ServerSocket(port); 
        }
        catch (IOException exc) {
            throw new ServerException("NetworkControler.startServer()", exc.getMessage());
        }
        flagServerWork = true;
        Thread acceptThread = new Thread(this::acceptClient);
        System.out.println("Сервер запущен на порту" + port);
        acceptThread.start();
    }
    void NetworkController() {
        listClients = new LinkedList();
    }
}
