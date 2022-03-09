import java.io.*;  
import java.net.*;
public class Client{
    public static void main(String[] args) {
        try{  
            Socket socket = new Socket("localhost", 8080);
            DataInputStream dis=new DataInputStream(socket.getInputStream());
            DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
            dout.writeUTF("HELO");
            dout.flush();
            String str2 = (String)dis.readUTF();
            System.out.println("message= "+str2);
            dout.writeUTF("BYE");
            dout.flush();
            String str3 = (String)dis.readUTF();
            System.out.println("message= "+str3);
            socket.close();     
        }catch (Exception e){
            System.out.println(e);
        }
    }  
}