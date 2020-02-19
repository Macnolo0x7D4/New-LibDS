/*
 * ----------------------------------------------------------------------------
 *  Copyright (c) Manuel Diaz Rojo form WinT 3794. All Rights Reserved.
 *  Open Source Software - may be modified and shared by FRC teams.
 *  This code is under MIT License. Check LICENSE file at project root .
 * ----------------------------------------------------------------------------
 */

package me.macnolo.libds.net;

import java.util.zip.CRC32;

import me.macnolo.libds.controller.Controller;
import me.macnolo.libds.enums.Alliance;
import me.macnolo.libds.enums.Mode;
import me.macnolo.libds.etc.Utilities;
import me.macnolo.libds.object.JoystickData;
import me.macnolo.libds.object.ProtocolTemplate;

public class AerialAssistProtocol implements ProtocolTemplate {

    static final String version = "v.1.0 nightly.";
    static final String name = "Aerial Assist Protocol.";

    static final int cEnabled = 0x20;
    static final int cTestMode = 0x02;
    static final int cAutonomous = 0x10;
    static final int cTeleoperated = 0x00;
    static final int cFMS_Attached = 0x08;
    static final int cResyncComms = 0x04;
    static final int cRebootRobot = 0x80;
    static final int cEmergencyStopOn = 0x00;
    static final int cEmergencyStopOff = 0x40;
    static final int cPosition1 = 0x31;
    static final int cPosition2 = 0x32;
    static final int cPosition3 = 0x33;
    static final int cAllianceRed = 0x52;
    static final int cAllianceBlue = 0x42;
    static final int cFMSAutonomous = 0x53;
    static final int cFMSTeleoperated = 0x43;

    static final int maxAxes = 6;
    static final int maxHats = 0;
    static final int maxButtons = 10;
    static final int maxJoysticks = 4;

    private static boolean resync = true;
    private static boolean reboot = false;
    private static boolean restartCode = false;

    private static boolean isEnable = false;
    private static boolean isEmergencyStopped = false;
    private static boolean isFmsExisting = false;

    @Override
    public void proccessRobotData(byte[] data) {
        int upper = (data[1] * 12) / 0x12;
        int lower = (data[2] * 12) / 0x12;
        float voltage = ((float) upper) + ((float) lower / 0xff);

        boolean eStopped = data[0] == cEmergencyStopOn;
    }

    @Override
    public void proccessFmsData() {

    }

    @Override
    public void proccessRadioData() {

    }

    @Override
    public void resetRobot(){
        resync = true;
        reboot = false;
        restartCode = false;
    }

    @Override
    public void rebootRobot(){
        reboot = true;
    }

    @Override
    public void restartRobot (){
        restartCode = true;
    }

    @Override
    public byte[] createRobotPackage(int robotPackageSent, int digitalInput, int team, Alliance alliance, Mode mode, JoystickData joystick) {
        byte[] pkg = new byte[1024];
        CRC32 crc32 = new CRC32();

        byte allianceCode = 0;
        byte positionCode = 0;

        int controlCode = 0;
        int enabled = 0;

        long checksum;

        switch (alliance){
            case RED1:
                allianceCode = cAllianceRed;
                positionCode = cPosition1;
                break;
            case RED2:
                allianceCode = cAllianceRed;
                positionCode = cPosition2;
                break;
            case RED3:
                allianceCode = cAllianceRed;
                positionCode = cPosition3;
                break;
            case BLUE1:
                allianceCode = cAllianceBlue;
                positionCode = cPosition1;
                break;
            case BLUE2:
                allianceCode = cAllianceBlue;
                positionCode = cPosition2;
                break;
            case BLUE3:
                allianceCode = cAllianceBlue;
                positionCode = cPosition3;
                break;
        }

        controlCode = cEmergencyStopOff;
        enabled = isEnable ? cEnabled : 0x00;

        switch (mode){
            case AUTO:
                controlCode |= enabled + cAutonomous;
                break;
            case TELEOP:
                controlCode |= enabled + cTeleoperated;
                break;
            case TEST:
                controlCode |= enabled + cTestMode;
                break;
            case PRACTICE:
                controlCode |= enabled + cTestMode;
                break;
            default:
                controlCode = cEmergencyStopOff;
        }

        if (resync) {
            controlCode |= cResyncComms;
        }

        if (isFmsExisting) {
            controlCode |= cFMS_Attached;
        }

        if (isEmergencyStopped) {
            controlCode = cEmergencyStopOn;
        }

        if (reboot) {
            controlCode = cRebootRobot;
        }

        pkg[0] = (byte) ((robotPackageSent & 0xff00) >> 8);
        pkg[1] = (byte) ((robotPackageSent & 0xff));

        pkg[2] = (byte) (controlCode);
        pkg[3] = (byte) (digitalInput);

        pkg[4] = (byte) ((team & 0xff00) >> 8);
        pkg[5] = (byte) ((team & 0xff));

        pkg[6] = (allianceCode);
        pkg[7] = (positionCode);

        for(int i = 0; i < Utilities.DRIVER_STATION_VERSION.length; i++){
            pkg[72 + i] = Utilities.DRIVER_STATION_VERSION[i];
        }

        crc32.update(pkg);
        checksum = crc32.getValue();

        pkg[1020] = (byte)((checksum & 0xff000000) >> 24);
        pkg[1021] = (byte)((checksum & 0xff0000) >> 16);
        pkg[1022] = (byte)((checksum & 0xff00) >> 8);
        pkg[1023] = (byte)(checksum & 0xff);

        Controller.upgradeRobotPackagesSent();
        return pkg;
    }

    @Override
    public byte[] createFmsPackage() {
        return null;
    }

    @Override
    public byte[] createRadioPackage() {
        return null;
    }

    @Override
    public void setResync(boolean push) {
        resync = push;
    }

    @Override
    public void setFmsExisting(boolean push) {
        isFmsExisting = push;
    }

    @Override
    public void setEmergencyStopped(boolean push) {
        isEmergencyStopped = push;
    }
}
