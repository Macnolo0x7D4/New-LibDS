/*
 * ----------------------------------------------------------------------------
 *  Copyright (c) Manuel Diaz Rojo form WinT 3794. All Rights Reserved.
 *  Open Source Software - may be modified and shared by FRC teams.
 *  This code is under MIT License. Check LICENSE file at project root .
 * ----------------------------------------------------------------------------
 */

package me.macnolo.libds.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import me.macnolo.libds.enums.Alliance;
import me.macnolo.libds.enums.Mode;
import me.macnolo.libds.enums.PackageTypes;
import me.macnolo.libds.enums.Protocol;
import me.macnolo.libds.etc.Utilities;

public class Controller extends Thread {
    private int team;
    private Alliance alliance;
    private Mode mode;
    private Protocol protocol;

    private boolean isRunning = true;

    private static int robotPackagesSent = 0;
    private static int fmsPackagesSent = 0;
    private static int radioPackagesSent = 0;

    private static int robotPackagesReceived = 0;
    private static int fmsPackagesReceived = 0;
    private static int radioPackagesReceived = 0;

    private static boolean isRobotPackage;
    private static boolean isFMSPackage;

    private InetAddress robotAddress;
    private InetAddress FMSAddress;
    private InetAddress radioAddress;

    Controller(int team, Alliance alliance, Mode mode, Protocol protocol) {
        this.team = team;
        this.alliance = alliance;
        this.mode = mode;
        this.protocol = protocol;
    }

    @Override
    public void run() {
        try {
            DatagramSocket robotSocket = new DatagramSocket(Utilities.ROBOT_PORT);
            DatagramSocket fmsSocket = new DatagramSocket(Utilities.FMS_PORT);
            DatagramSocket radioSocket = new DatagramSocket(Utilities.RADIO_PORT);

            while (isRunning) {
                byte[] robotPkg = new byte[1024];
                DatagramPacket robotDataPkg = new DatagramPacket(robotPkg, robotPkg.length);

                byte[] fmsPkg = new byte[1024];
                DatagramPacket fmsDataPkg = new DatagramPacket(fmsPkg, fmsPkg.length);

                byte[] radioPkg = new byte[1024];
                DatagramPacket radioDataPkg = new DatagramPacket(radioPkg, radioPkg.length);

                robotSocket.receive(robotDataPkg);
                fmsSocket.receive(fmsDataPkg);
                radioSocket.receive(radioDataPkg);

                byte[] robotData = robotDataPkg.getData();
                byte[] fmsData = fmsDataPkg.getData();
                byte[] radioData = radioDataPkg.getData();

                if(robotData != null){
                    isRobotPackage = true;
                    processData(robotData, PackageTypes.ROBOT);
                } else{
                    isRobotPackage = false;
                }

                if(fmsData != null){
                    isFMSPackage = true;
                    processData(fmsData, PackageTypes.FMS);
                } else{
                    isFMSPackage = false;
                }

                if(radioData != null) {
                    processData(radioData, PackageTypes.RADIO);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processData(byte[] data, PackageTypes pkgType) {
        if(data != null && data.length == 1024) {
            switch (pkgType){
                case ROBOT:
                    new ProtocolController(this.protocol).getProtocol().proccessRobotData(data);
                    upgradeRobotPackagesReceived();
                    break;
                case FMS:
                    upgradeFMSPackagesReceived();
                    break;
                case RADIO:
                    upgradeRadioPackagesReceived();
                    break;
            }
        }

    }

    public Exception sendPackage(PackageTypes pkgType, byte[] netPackage) {
        switch (pkgType) {
            case ROBOT:
                try {
                    DatagramSocket udp = new DatagramSocket();
                    DatagramPacket pkg = new DatagramPacket(netPackage, netPackage.length, getRobotAddress(), Utilities.ROBOT_PORT);

                    udp.send(pkg);
                    upgradeRobotPackagesSent();
                } catch (IOException e) {
                    return e;
                }

                break;
            case FMS:
                try {
                    DatagramSocket udp = new DatagramSocket();
                    DatagramPacket pkg = new DatagramPacket(netPackage, netPackage.length, getFMSAddress(), Utilities.FMS_PORT);

                    udp.send(pkg);
                    upgradeFMSPackagesSent();
                } catch (IOException e) {
                    return e;
                }

                break;
            case RADIO:
                try {
                    DatagramSocket udp = new DatagramSocket();
                    DatagramPacket pkg = new DatagramPacket(netPackage, netPackage.length, getRadioAddress(), Utilities.RADIO_PORT);

                    udp.send(pkg);
                    upgradeRadioPackagesSent();
                } catch (IOException e) {
                    return e;
                }

                break;
        }
        return null;
    }

    public byte[] createPackage(PackageTypes pkgType){
        byte[] pkg = new byte[1024];

        switch (pkgType) {
            case ROBOT:
                pkg = new ProtocolController(this.protocol).getProtocol().createRobotPackage(robotPackagesSent,0,team, alliance, mode,null);
                break;
            case FMS:
                pkg = new ProtocolController(this.protocol).getProtocol().createFmsPackage();
                break;
            case RADIO:
                pkg = new ProtocolController(this.protocol).getProtocol().createRadioPackage();
                break;
        }

        return pkg;
    }

    public void setAlliance(Alliance alliance){
        this.alliance = alliance;
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean running) {
        isRunning = running;
    }


    public static void upgradeRobotPackagesSent() { robotPackagesSent++; }
    public static void upgradeFMSPackagesSent() { fmsPackagesSent++; }
    public static void upgradeRadioPackagesSent() { radioPackagesSent++; }

    private static void upgradeRobotPackagesReceived() { robotPackagesReceived++; }
    private static void upgradeFMSPackagesReceived() { fmsPackagesReceived++; }
    private static void upgradeRadioPackagesReceived() { radioPackagesReceived++; }

    public static int getRobotPackagesSent() {
        return robotPackagesSent;
    }

    public static int getFmsPackagesSent() {
        return fmsPackagesSent;
    }

    public static int getRobotPackagesReceived() {
        return robotPackagesReceived;
    }

    public static int getFmsPackagesReceived() {
        return fmsPackagesReceived;
    }

    public static boolean isRobotPackage() {
        return isRobotPackage;
    }

    public static boolean isFMSPackage() {
        return isFMSPackage;
    }

    public InetAddress getRobotAddress() {
        return robotAddress;
    }

    public void setRobotAddress(InetAddress robotAddress) {
        this.robotAddress = robotAddress;
    }

    public InetAddress getFMSAddress() {
        return FMSAddress;
    }

    public void setFMSAddress(InetAddress FMSAddress) {
        this.FMSAddress = FMSAddress;
    }

    public InetAddress getRadioAddress() {
        return radioAddress;
    }

    public void setRadioAddress(InetAddress radioAddress) {
        this.radioAddress = radioAddress;
    }
}
