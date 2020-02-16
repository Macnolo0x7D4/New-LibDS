/*
 * ----------------------------------------------------------------------------
 *  Copyright (c) Manuel Diaz Rojo form WinT 3794. All Rights Reserved.
 *  Open Source Software - may be modified and shared by FRC teams.
 *  This code is under MIT License. Check LICENSE file at project root .
 * ----------------------------------------------------------------------------
 */

package me.macnolo.libds.etc;

import java.net.InetAddress;
import java.net.UnknownHostException;

import me.macnolo.libds.enums.IpFormats;

public class IpFormater {
    private int team;
    private IpFormats format;
    private int host;

    public IpFormater(int team, IpFormats format, int host) {
        this.team = team;
        this.format = format;
        this.host = host;
    }

    public String getAddress() {
        String finalAddress;

        switch (format) {
            case IP_1:
                int te = team / 100;
                int am = team - (te * 100);
                finalAddress = "10." + te + "." + am + "." + host;
                return finalAddress;
            case M_DNS_1:
                finalAddress = "roboRIO-" + team + "-FRC.local";
                return finalAddress;
            case M_DNS_2:
                finalAddress = "roboRIO-" + team + ".local";
                return finalAddress;
        }
        return "";
    }

    public InetAddress getInetAddress(){
        InetAddress addr = null;
        try {
            addr = InetAddress.getByAddress(getAddress().getBytes());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return addr;
    }
}
