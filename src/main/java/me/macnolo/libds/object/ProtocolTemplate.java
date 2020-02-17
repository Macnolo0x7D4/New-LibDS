/*
 * ----------------------------------------------------------------------------
 *  Copyright (c) Manuel Diaz Rojo form WinT 3794. All Rights Reserved.
 *  Open Source Software - may be modified and shared by FRC teams.
 *  This code is under MIT License. Check LICENSE file at project root .
 * ----------------------------------------------------------------------------
 */

package me.macnolo.libds.object;

public interface ProtocolTemplate {
    void proccessRobotData(byte[] data);
    void proccessFmsData();
    void proccessRadioData();

    void resetRobot();
    void rebootRobot();
    void restartRobot ();

    NetPackage createRobotPackage();
    NetPackage createFmsPackage();
    NetPackage createRadioPackage();

    String version = "Base Protocol - Not Usable";
    String name = "v.1.0 nightly";

    int cEnabled = 0x0;
    int cTestMode = 0x0;
    int cAutonomous = 0x0;
    int cTeleoperated = 0x0;
    int cFMS_Attached = 0x0;
    int cResyncComms = 0x0;
    int cRebootRobot = 0x0;
    int cEmergencyStopOn = 0x0;
    int cEmergencyStopOff = 0x0;
    int cPosition1 = 0x0;
    int cPosition2 = 0x0;
    int cPosition3 = 0x0;
    int cAllianceRed = 0x0;
    int cAllianceBlue = 0x0;
    int cFMSAutonomous = 0x0;
    int cFMSTeleoperated = 0x0;

    int maxAxes = 0;
    int maxHats = 0;
    int maxButtons = 0;
    int maxJoysticks = 0;

    int resync = 0;
    int reboot = 0;
    int restartCode = 0;
}
