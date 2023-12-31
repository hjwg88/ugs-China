/*
    Copyright 2021-2023 Will Winder

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
package com.willwinder.ugs.nbp.editor;

import com.willwinder.ugs.nbp.lib.lookup.CentralLookup;
import com.willwinder.universalgcodesender.model.BackendAPI;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;

import java.io.File;

/**
 * @author Joacim Breiler
 */
public class EditorUtils {

    private EditorUtils() {
        // Can not be instanced
    }

    /**
     * Loads the file into the backend
     *
     * @param fileObject the file object to load
     */
    public static void openFile(FileObject fileObject) {
        BackendAPI backend = CentralLookup.getDefault().lookup(BackendAPI.class);
        try {
            backend.setGcodeFile(new File((fileObject.getPath())));
        } catch (Exception e) {
            ErrorManager.getDefault().notify(ErrorManager.WARNING, e);
        }
    }

    /**
     * Unloads the gcode in the backend if all editor panes are closed
     */
    public static void unloadFile() {
        if (com.willwinder.ugs.nbp.lib.EditorUtils.getOpenEditors().size() > 1) {
            return;
        }

        try {
            BackendAPI backend = CentralLookup.getDefault().lookup(BackendAPI.class);
            backend.unsetGcodeFile();
        } catch (Exception e) {
            ErrorManager.getDefault().notify(ErrorManager.WARNING, e);
        }
    }
}
