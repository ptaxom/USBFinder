package main.HardDiskDrive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class USBDevicesInfo {

    private Process process = null;
    private BufferedReader reader = null;
    private List<USBDevice> devices = new ArrayList<>();

    public USBDevicesInfo(){
        findUSBDevices();
        findUsedSize();
    }


    public void findUSBDevices() {
        try {
            process = Runtime.getRuntime().exec( new String[]{"/bin/bash","-c","echo "
                    + System.getenv("PASSWORD") + " |  lsblk"});
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String parseLine;
            while((parseLine = reader.readLine())!= null) {
                if (USBDevice.isUSBDevice(parseLine))
                    devices.add(new USBDevice(parseLine));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if( process != null ) process.destroy();
        }
    }

    public void findUsedSize() {
        try {
            process = Runtime.getRuntime().exec( new String[]{"/bin/bash","-c","echo "
                    + System.getenv("PASSWORD") + " |  df -h"});
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String parseLine;
            while((parseLine = reader.readLine())!= null) {
                for(USBDevice device: devices){
                    if (parseLine.indexOf(device.getName()) != -1)
                    {
                        device.setUsedSize(parseUsedSize(parseLine));
                    }
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if( process != null ) process.destroy();
        }
    }

    public String parseUsedSize(String input){
        String output = "";
        int lastKey = 0;
        int pos = 0;
        for(int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == ' ' && input.charAt(i - 1) != ' ')
                lastKey++;
            if (lastKey == 3)
            {
                pos = i-1;
                break;
            }
        }
        while (input.charAt(pos) != ' ')
            output = input.charAt(pos--) + output;
        return output;
    }


    @Override
    public String toString(){
        String output = "";
        for (USBDevice s : devices)
            output+="\n"+s;
        return output;
    }



}









