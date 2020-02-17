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
import java.net.SocketException;

import me.macnolo.libds.enums.Alliance;
import me.macnolo.libds.enums.Mode;
import me.macnolo.libds.enums.PackageTypes;
import me.macnolo.libds.enums.Protocol;
import me.macnolo.libds.etc.Utilities;
import me.macnolo.libds.object.NetPackage;

public class Controller extends Thread {
    private int team;
    private Alliance alliance;
    private Mode mode;
    private Protocol protocol;

    private boolean isRunning = true;

    private ProtocolController protocolController;

    private static int robotPackagesSent = 0;
    private static int fmsPackagesSent = 0;
    private static int radioPackagesSent = 0;

    private static int robotPackagesReceived = 0;
    private static int fmsPackagesReceived = 0;
    private static int radioPackagesReceived = 0;

    Controller(int team, Alliance alliance, Mode mode, Protocol protocol) {
        this.team = team;
        this.alliance = alliance;
        this.mode = mode;
        this.protocol = protocol;

        ProtocolController protocolController = new ProtocolController(this.protocol);
    }

    @Override
    public void run() {
        try {
            DatagramSocket robotSocket = new DatagramSocket(Utilities.ROBOT_PORT);
            DatagramSocket fmsSocket = new DatagramSocket(Utilities.FMS_PORT);
            DatagramSocket radioSocket = new DatagramSocket(Utilities.RADIO_PORT);

            while (isRunning) {
                NetPackage robotPkg = new NetPackage(PackageTypes.ROBOT);
                DatagramPacket robotDataPkg = new DatagramPacket(robotPkg.getPackage(), robotPkg.getLength());

                NetPackage fmsPkg = new NetPackage(PackageTypes.FMS);
                DatagramPacket fmsDataPkg = new DatagramPacket(fmsPkg.getPackage(), fmsPkg.getLength());

                NetPackage radioPkg = new NetPackage(PackageTypes.RADIO);
                DatagramPacket radioDataPkg = new DatagramPacket(radioPkg.getPackage(), radioPkg.getLength());

                robotSocket.receive(robotDataPkg);
                fmsSocket.receive(fmsDataPkg);
                radioSocket.receive(radioDataPkg);

                byte[] robotData = robotDataPkg.getData();
                byte[] fmsData = fmsDataPkg.getData();
                byte[] radioData = radioDataPkg.getData();

                processData(robotData, this.protocol, PackageTypes.ROBOT);
                processData(fmsData, this.protocol, PackageTypes.FMS);
                processData(radioData, this.protocol, PackageTypes.RADIO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processData(byte[] data, Protocol protocol, PackageTypes pkgType) {
        if(data != null && data.length == 1024) {
            switch (pkgType){
                case ROBOT:
                    protocolController.getProtocol().proccessRobotData(data);
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

    public Exception sendPackage(PackageTypes pkgType, NetPackage netPackage) {
        switch (pkgType) {
            case ROBOT:
                try {
                    DatagramSocket udp = new DatagramSocket();
                    DatagramPacket pkg = new DatagramPacket(netPackage.getPackage(), netPackage.getLength(), LibDS.ROBOT_ADDR, Utilities.ROBOT_PORT);

                    udp.send(pkg);
                    upgradeRobotPackagesSent();
                } catch (IOException e) {
                    return e;
                }

                break;
            case FMS:
                try {
                    DatagramSocket udp = new DatagramSocket();
                    DatagramPacket pkg = new DatagramPacket(netPackage.getPackage(), netPackage.getLength(), LibDS.FMS_ADDR, Utilities.FMS_PORT);

                    udp.send(pkg);
                    upgradeFMSPackagesSent();
                } catch (IOException e) {
                    return e;
                }

                break;
            case RADIO:
                try {
                    DatagramSocket udp = new DatagramSocket();
                    DatagramPacket pkg = new DatagramPacket(netPackage.getPackage(), netPackage.getLength(), LibDS.RADIO_ADDR, Utilities.RADIO_PORT);

                    udp.send(pkg);
                    upgradeRadioPackagesSent();
                } catch (IOException e) {
                    return e;
                }

                break;
        }
        return null;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean running) {
        isRunning = running;
    }


    private static void upgradeRobotPackagesSent() { robotPackagesSent++; }
    private static void upgradeFMSPackagesSent() { fmsPackagesSent++; }
    private static void upgradeRadioPackagesSent() { radioPackagesSent++; }

    private static void upgradeRobotPackagesReceived() { robotPackagesReceived++; }
    private static void upgradeFMSPackagesReceived() { fmsPackagesReceived++; }
    private static void upgradeRadioPackagesReceived() { radioPackagesReceived++; }
}
