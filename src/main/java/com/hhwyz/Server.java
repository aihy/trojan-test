package com.hhwyz;

import java.util.Objects;

/**
 * @author erniu.wzh
 * @date 2022/11/15 14:30
 */
public class Server {

    private String serverAddress;
    private Long time;

    public Server(String serverAddress, Long time) {
        this.serverAddress = serverAddress;
        this.time = time;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Server{" +
                "serverAddress='" + serverAddress + '\'' +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Server)) return false;
        Server server = (Server) o;
        return getServerAddress().equals(server.getServerAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerAddress());
    }
}
