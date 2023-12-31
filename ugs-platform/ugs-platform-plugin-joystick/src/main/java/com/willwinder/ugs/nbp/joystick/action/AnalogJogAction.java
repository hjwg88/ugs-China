/*
    Copyright 2020 Will Winder

    This file is part of Universal Gcode Sender (UGS).

    UGS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    UGS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with UGS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.willwinder.ugs.nbp.joystick.action;

import com.willwinder.ugs.nbp.lib.lookup.CentralLookup;
import com.willwinder.universalgcodesender.i18n.Localization;
import com.willwinder.universalgcodesender.listeners.ControllerState;
import com.willwinder.universalgcodesender.listeners.UGSEventListener;
import com.willwinder.universalgcodesender.model.Axis;
import com.willwinder.universalgcodesender.model.BackendAPI;
import com.willwinder.universalgcodesender.model.UGSEvent;
import com.willwinder.universalgcodesender.model.events.ControllerStateEvent;
import com.willwinder.universalgcodesender.utils.ContinuousJogWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AnalogJogAction extends AbstractAction implements AnalogAction, UGSEventListener {
    private final ContinuousJogWorker worker;
    private final Axis axis;
    private float value;

    public AnalogJogAction(ContinuousJogWorker worker, Axis axis) {
        this.worker = worker;
        this.axis = axis;
        this.putValue(NAME, Localization.getString("platform.plugin.joystick.action.continuousJogging") + " " + axis.name());
        setEnabled(false);

        BackendAPI backend = CentralLookup.getDefault().lookup(BackendAPI.class);
        backend.addUGSEventListener(this);
    }

    @Override
    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isEnabled()) {
            this.worker.setDirection(axis, this.value);
        }
    }

    @Override
    public void UGSEvent(UGSEvent evt) {
        if (evt instanceof ControllerStateEvent) {
            ControllerState state = ((ControllerStateEvent) evt).getState();
            setEnabled(state == ControllerState.IDLE || state == ControllerState.JOG);
        }
    }
}
