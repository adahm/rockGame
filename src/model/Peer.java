package model;

import java.io.Serializable;

public class Peer implements Serializable{
    private int portnumber;
    private String ip;

    public Peer(String ip,int portnumber) {
        this.portnumber = portnumber;
        this.ip = ip;
    }

    public int getPortnumber() {
        return portnumber;
    }

    public void setPortnumber(int portnumber) {
        this.portnumber = portnumber;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Peer{" +
                "portnumber=" + portnumber +
                ", ip='" + ip + '\'' +
                '}';
    }
}
