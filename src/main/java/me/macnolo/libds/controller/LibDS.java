/*
 * ----------------------------------------------------------------------------
 *  Copyright (c) Manuel Diaz Rojo form WinT 3794. All Rights Reserved.
 *  Open Source Software - may be modified and shared by FRC teams.
 *  This code is under MIT License. Check LICENSE file at project root .
 * ----------------------------------------------------------------------------
 */

package me.macnolo.libds.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import me.macnolo.libds.enums.Alliance;
import me.macnolo.libds.enums.IpFormats;
import me.macnolo.libds.enums.Mode;
import me.macnolo.libds.enums.Protocol;
import me.macnolo.libds.etc.IpFormater;

public class LibDS {
    private int team;
    private Alliance alliance;
    private Mode mode;
    private Protocol protocol;

    private byte[] robotIp;
    private byte[] fmsIp;
    private byte[] radioIp;

    private IpFormats ipTypeSelected;

    public static InetAddress ROBOT_ADDR = null;
    public static InetAddress FMS_ADDR = null;
    public static InetAddress RADIO_ADDR = null;

    private Controller controller;

    public LibDS(int team, Alliance alliance, Mode mode, Protocol protocol, byte[] robotIp) {
        this.team = team;
        this.alliance = alliance;
        this.mode = mode;
        this.protocol = protocol;
        this.robotIp = robotIp;

        switch(protocol) {
            case AERIAL_ASSIST:
                ipTypeSelected = IpFormats.IP_1;
                if(robotIp == null || robotIp.equals("")){
                    this.robotIp = IpFormater.getAddress(ipTypeSelected, team,(byte) 1);
                }
                this.fmsIp = IpFormater.getAddress(ipTypeSelected, team, (byte) 2);
                this.radioIp = IpFormater.getAddress(ipTypeSelected, team, (byte) 3);
                break;
        }

        try {
            if(this.robotIp != null) {
                switch (ipTypeSelected) {
                    case IP_1:
                        ROBOT_ADDR = InetAddress.getByAddress(this.robotIp);
                        break;
                    default:
                        ROBOT_ADDR = InetAddress.getByName(this.robotIp.toString());
                        break;
                }
            }

            if (this.fmsIp != null) {
                FMS_ADDR = InetAddress.getByAddress(this.fmsIp);
            }

            if (this.radioIp != null) {
                RADIO_ADDR = InetAddress.getByAddress(this.radioIp);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        controller = new Controller(this.team, this.alliance, this.mode, this.protocol);

        controller.start();
    }

    public InterruptedException close(){
        try {
            controller.setIsRunning(false);
            controller.join();
        } catch (InterruptedException e) {
            return e;
        }
        return null;
    }

    public void setNewAlliance(Alliance alliance){

    }


    public void setNewMode(Mode mode){

    }

    public void setEnable(boolean enable) {
        if(enable) {

        }
    }
}
