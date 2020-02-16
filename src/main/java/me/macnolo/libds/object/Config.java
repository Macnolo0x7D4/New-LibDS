/*
 * ----------------------------------------------------------------------------
 *  Copyright (c) Manuel Diaz Rojo form WinT 3794. All Rights Reserved.
 *  Open Source Software - may be modified and shared by FRC teams.
 *  This code is under MIT License. Check LICENSE file at project root .
 * ----------------------------------------------------------------------------
 */

package me.macnolo.libds.object;

import me.macnolo.libds.enums.Alliance;
import me.macnolo.libds.enums.Mode;
import me.macnolo.libds.enums.Protocol;

public class Config {
    private int team = 0;
    private Protocol protocol = Protocol.INFINITE_RECHARGE;

    private int cpuUsage = -1;
    private int ramUsage = -1;
    private int diskUsage = -1;

    private int battery = -1;
    private int robotVoltage = -1;

    private int robotCode = 0;
    private int radioCommunication = 0;
    private int fmsCommunication = 0;
    private int joystick = 0;

    private Alliance alliance = Alliance.RED1;
    private Mode mode = Mode.TELEOP;

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(int cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public int getRamUsage() {
        return ramUsage;
    }

    public void setRamUsage(int ramUsage) {
        this.ramUsage = ramUsage;
    }

    public int getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(int diskUsage) {
        this.diskUsage = diskUsage;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getRobotVoltage() {
        return robotVoltage;
    }

    public void setRobotVoltage(int robotVoltage) {
        this.robotVoltage = robotVoltage;
    }

    public int getRobotCode() {
        return robotCode;
    }

    public void setRobotCode(int robotCode) {
        this.robotCode = robotCode;
    }

    public int getRadioCommunication() {
        return radioCommunication;
    }

    public void setRadioCommunication(int radioCommunication) {
        this.radioCommunication = radioCommunication;
    }

    public int getFmsCommunication() {
        return fmsCommunication;
    }

    public void setFmsCommunication(int fmsCommunication) {
        this.fmsCommunication = fmsCommunication;
    }

    public int getJoystick() {
        return joystick;
    }

    public void setJoystick(int joystick) {
        this.joystick = joystick;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void loadStatus(int[] status) {
        for (int i = 0; i < status.length; i++) {
            switch (i) {
                case 0:
                    setBattery(status[i]);
                    break;
                case 1:
                    setCpuUsage(status[i]);
                    break;
                case 2:
                    setDiskUsage(status[i]);
                    break;
                case 3:
                    setFmsCommunication(status[i]);
                    break;
                case 4:
                    setJoystick(status[i]);
                    break;
                case 5:
                    setRamUsage(status[i]);
                    break;
                case 6:
                    setRadioCommunication(status[i]);
                    break;
                case 7:
                    setRobotCode(status[i]);
                    break;
                case 8:
                    setRobotVoltage(status[i]);
                    break;
            }
        }
    }

    public int[] getStatus() {
        int[] status = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 9; i++) {
            switch (i) {
                case 0:
                    status[i] = getBattery();
                    break;
                case 1:
                    status[i] = getCpuUsage();
                    break;
                case 2:
                    status[i] = getDiskUsage();
                    break;
                case 3:
                    status[i] = getFmsCommunication();
                    break;
                case 4:
                    status[i] = getJoystick();
                    break;
                case 5:
                    status[i] = getRamUsage();
                    break;
                case 6:
                    status[i] = getRadioCommunication();
                    break;
                case 7:
                    status[i] = getRobotCode();
                    break;
                case 8:
                    status[i] = getRobotVoltage();
                    break;
            }
        }

        return status;
    }
}
