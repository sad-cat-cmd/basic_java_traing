/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin_
 */

public class TimeController {
    private long startTime;
    public TimeController(){
        startTime = System.nanoTime();
    }
    public long getTimeIntervalNs() {
        return System.nanoTime() - startTime;
    }
    public long getTimeIntervalMs() {
        return (System.nanoTime() - startTime) / 1000000;
    }
}
