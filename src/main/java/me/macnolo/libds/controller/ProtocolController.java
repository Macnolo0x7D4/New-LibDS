/*
 * ----------------------------------------------------------------------------
 *  Copyright (c) Manuel Diaz Rojo form WinT 3794. All Rights Reserved.
 *  Open Source Software - may be modified and shared by FRC teams.
 *  This code is under MIT License. Check LICENSE file at project root .
 * ----------------------------------------------------------------------------
 */

package me.macnolo.libds.controller;

import me.macnolo.libds.enums.Protocol;
import me.macnolo.libds.net.AerialAssistProtocol;

public class ProtocolController {
    private Protocol protocol;

    public ProtocolController(Protocol protocol) {
        this.protocol = protocol;
    }

    public <T> Class<T> getProtocol(){
        switch (protocol){
            case INFINITE_RECHARGE:
                //return (Class<T>)AerialAssistProtocol.class;
            case DEEP_SPACE:
                //return (Class<T>)AerialAssistProtocol.class;
            case STRONGHOLD:
                //return (Class<T>)AerialAssistProtocol.class;
            case RECYCLE_RUSH:
                //return (Class<T>)AerialAssistProtocol.class;
            case AERIAL_ASSIST:
                return (Class<T>)AerialAssistProtocol.class;
        }

        return null;
    }

    public AerialAssistProtocol getAerialAssistProtocol() {
        AerialAssistProtocol protocol = null;
        if (this.protocol == Protocol.AERIAL_ASSIST) {
            protocol = new AerialAssistProtocol();
        }
        return protocol;
    }

}
