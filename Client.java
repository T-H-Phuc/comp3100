import java.io.*;  
import java.net.*;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Client{

    public static Socket connectionSocket;
    public static BufferedReader DIS;
    public static DataOutputStream DOUT;
    public static ArrayList<Server> listOfServer;
    public static ArrayList<Server> listOfLargestType;
    public static int largerType;
    public static int startingServer;
    public static String receiveMessage;
    public static boolean typeFound;
    public static String firstType;

    
    public static void main(String[] args) {
        Client c = new Client();
        c.connectServer();
        c.scheduleJob();;
    }

    public void connectServer(){
        try{
            this.connectionSocket = new Socket("localhost", 50000);
            this.DIS = new BufferedReader(new InputStreamReader(this.connectionSocket.getInputStream()));
            this.DOUT = new DataOutputStream(this.connectionSocket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void sendHELO(){
        try{
            DOUT.write(("HELO\n").getBytes());
            DOUT.flush();
        }catch (Exception e){
            System.out.println(e);
        }
        
    }

    public void sendAUTH(){
        try{
            DOUT.write(("AUTH phuc\n").getBytes());
            DOUT.flush();
        }catch (Exception e){
            System.out.println(e);
        }
        
    }

    public void sendREDY(){
        try{
            DOUT.write(("REDY\n").getBytes());
            DOUT.flush();
        }catch (Exception e){
            System.out.println(e);
        }
        
    }

    public void sendOK(){
        try{
            DOUT.write(("OK\n").getBytes());
            DOUT.flush();
        }catch (Exception e){
            System.out.println(e);
        }
        
    }

    public void sendQUIT(){
        try{
            DOUT.write(("QUIT\n").getBytes());
            DOUT.flush();
            DOUT.close();
            connectionSocket.close();
        }catch (Exception e){
            System.out.println(e);
        }
        
    }

    public void setAllServer(){
        try{
                typeFound = false;
                firstType = "";
                DOUT.write(("GETS All" + "\n").getBytes());
                DOUT.flush();

                receiveMessage = (String) DIS.readLine();
                System.out.println(receiveMessage);
                
                String[] time = receiveMessage.split(" ");
                Integer number =  Integer.parseInt(time[1]);
                sendOK();

                
                for(int i = 0; i < number; i++){
                    receiveMessage = (String) DIS.readLine();
                    
                    String[] server = receiveMessage.split(" ");
                    System.out.println(receiveMessage);
                    Server s = new Server(server[0], Integer.parseInt(server[1]), server[2], Integer.parseInt(server[3]), Integer.parseInt(server[4]), Integer.parseInt(server[5]), Integer.parseInt(server[6]));
                    listOfServer.add(s);
                    if(Integer.parseInt(server[4]) > largerType){
                        largerType = Integer.parseInt(server[4]);
                    }
                    
                }
                for(int i = 0; i < listOfServer.size(); i++){
                    if(listOfServer.get(i).cores == largerType){
                        if(typeFound == false){
                            listOfLargestType.add(listOfServer.get(i));
                            typeFound = true;
                            firstType = listOfServer.get(i).type;
                        }else{
                            if(listOfServer.get(i).type.equals(firstType)){
                                listOfLargestType.add(listOfServer.get(i));
                            }
                        }
                        
                    }
                }
                sendOK();

        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void scheduleJob(){
        try{
            largerType = 0;
            startingServer = 0;
            listOfServer = new ArrayList<>();
            listOfLargestType = new ArrayList<>();
            sendHELO();

            receiveMessage = (String) DIS.readLine();
            System.out.println(receiveMessage);

            sendAUTH();

            receiveMessage = (String) DIS.readLine();
            System.out.println(receiveMessage);

            sendREDY();
            receiveMessage = (String) DIS.readLine();
            System.out.println(receiveMessage);

            String[] jobsplit = receiveMessage.split(" ");
            System.out.println(jobsplit[0]);

            setAllServer();


            
            while(!jobsplit[0].equals("NONE")){
                
                
                System.out.println(jobsplit[0]);

                if(jobsplit[0].equals("JOBN")){
                    String serverType = listOfLargestType.get(startingServer).type;
                    String serverId = listOfLargestType.get(startingServer).id+"";
                    DOUT.write(("SCHD" + " " + jobsplit[2] + " " + serverType + " " + serverId +"\n").getBytes());
                    DOUT.flush();
                    System.out.println(("SCHD" + " " + jobsplit[2] + " " + serverType + " " + serverId +"\n").getBytes());
                    startingServer++;
                    if(startingServer > listOfLargestType.size()-1){
                        startingServer=0;
                    }
                }
                else if(jobsplit[0].equals("OK") || jobsplit[0].equals("JCPL")){
                    sendREDY();
                }

                receiveMessage = (String) DIS.readLine();
                jobsplit = receiveMessage.split(" ");
                
        }
        
        sendQUIT();

        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
        }
    }

    

}