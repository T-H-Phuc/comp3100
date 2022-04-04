import java.io.*;  
import java.net.*;
public class Server{
    String type;
    int id;
    String state;
    int curStartTime;
    int cores;
    int memory;
    int disk;

    public Server(String type, int id, String state, int curStartTime, int cores, int memory, int disk){
        this.type = type;
        this.id = id;
        this.state = state;
        this.curStartTime = curStartTime;
        this.cores = cores;
        this.memory = memory;
        this.disk = disk;
    }
}