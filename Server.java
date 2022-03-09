import java.io.*;  
import java.net.*;
public class Server{
    public static void main(String[] args) {
        try{  
            ServerSocket server = new ServerSocket(8080);
            Socket socket = server.accept();
            int messageCount = 0;
            DataInputStream dis=new DataInputStream(socket.getInputStream());
            DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
            String str=(String)dis.readUTF();  
            System.out.println("message= "+str);

            dout.writeUTF("G’DAY");
            dout.flush();
            String str2 = (String)dis.readUTF();
            System.out.println("message= "+str2);
            dout.writeUTF("BYE");
            dout.flush();
            server.close();     
        }catch (Exception e){
            System.out.println(e);
        }
    }  
}