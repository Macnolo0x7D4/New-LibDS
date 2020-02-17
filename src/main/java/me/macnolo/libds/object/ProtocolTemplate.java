/*
 * ----------------------------------------------------------------------------
 *  Copyright (c) Manuel Diaz Rojo form WinT 3794. All Rights Reserved.
 *  Open Source Software - may be modified and shared by FRC teams.
 *  This code is under MIT License. Check LICENSE file at project root .
 * ----------------------------------------------------------------------------
 */

package me.macnolo.libds.object;

public abstract class ProtocolTemplate {
    public void proccessRobotData(byte[] data) {}
    public void proccessFmsData() {}
    public void proccessRadioData() {}
    public void resetRobot(){}
    public void rebootRobot(){}
    public void restartRobot (){}
    public NetPackage createRobotPackage(){return null;}
    public NetPackage createFmsPackage(){return null;}
    public NetPackage createRadioPackage(){return null;}
}
