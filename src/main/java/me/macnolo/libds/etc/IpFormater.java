/*
 * ----------------------------------------------------------------------------
 *  Copyright (c) Manuel Diaz Rojo form WinT 3794. All Rights Reserved.
 *  Open Source Software - may be modified and shared by FRC teams.
 *  This code is under MIT License. Check LICENSE file at project root .
 * ----------------------------------------------------------------------------
 */

package me.macnolo.libds.etc;

import me.macnolo.libds.enums.IpFormats;

public class IpFormater {
    public static byte[] getAddress(IpFormats format, int team, byte host ) {
        byte[] finalAddress = {0,0,0,0};
        String tmp = "";

        switch (format) {
            case IP_1:
                byte te = (byte) (team / 100);
                byte am = (byte) (team - (te * 100));
                finalAddress[0] = 10;
                finalAddress[1] = te;
                finalAddress[2] = am;
                finalAddress[3] = host;
                return finalAddress;
            case M_DNS_1:
                tmp = "roboRIO-" + team + "-FRC.local";
                finalAddress = tmp.getBytes();
                return finalAddress;
            case M_DNS_2:
                tmp = "roboRIO-" + team + ".local";
                finalAddress = tmp.getBytes();
                return finalAddress;
        }
        return null;
    }
}
