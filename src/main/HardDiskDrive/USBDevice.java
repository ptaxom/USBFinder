package main.HardDiskDrive;

import java.io.IOException;

public class USBDevice {
    private String systemName;
    private String name;
    private String size;
    private String usedSize;
    private  Process process;

    public static boolean isUSBDevice(String input){
        return (input.indexOf("/media/") != -1);
    }

    public USBDevice(String line) {
    this.name=line.substring(line.lastIndexOf("/")+1, line.length());
    this.systemName=line.substring(line.lastIndexOf("s"), line.indexOf(" "));
        //System.out.println(line);
    this.size = getSizeFromString(line);

    }

    private String getSizeFromString(String input){
        String output = "";
        int pos = input.indexOf("G");
        if (pos == -1)
            pos = input.indexOf("M");
        if (pos == -1)
            pos = input.indexOf("K");
        while (input.charAt(pos) != ' ')
            output = input.charAt(pos--) + output;
        return output;
    }

    private void umount(){
        try {
            String operation = " |  sudo umount /dev/"+systemName;
            process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "echo "
                    + System.getenv("PASSWORD") + operation});
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if( process != null ) process.destroy();
        }
    }

    @Override
    public String toString(){
        String s = "";
        s+="Метка тома: "+systemName+"\n";
        s+="Название устройства: "+name+"\n";
        s+="Размер памяти: "+size+"\n";
        s+="Размер использованной памяти: "+usedSize+"\n";
        return s;
    }

    public void setUsedSize(String usedSize){
        this.usedSize = usedSize;
    }

    public String getName() {
        return name;
    }
}
