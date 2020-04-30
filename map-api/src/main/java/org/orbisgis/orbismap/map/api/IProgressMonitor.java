/**
 * MAP-API is part of the OrbisGIS platform
 * 
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285 Equipe DECIDE UNIVERSITÉ DE
 * BRETAGNE-SUD Institut Universitaire de Technologie de Vannes 8, Rue Montaigne
 * - BP 561 56017 Vannes Cedex
 *
 * MAP-API  is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * MAP-API  is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * MAP-API  is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * MAP-API. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.api;

import java.beans.PropertyChangeListener;

/**
 * Represents a way to report progress of a task.
 *
 * How to use:
 *
 * Your method receive a ProgressMonitor instance named A. You know the number
 * of task in your method then you call
 * {@link IProgressMonitor#startTask(String, long)} and you receive a new
 * instance of ProgressMonitor named B. With B you can advance in task by
 * calling {@link org.orbisgis.commons.progress.ProgressMonitor#endTask()} or by
 * passing B to the sub method parameter.
 *
 * @author Fernando GONZALEZ CORTES
 * @author Thomas LEDUC
 * @author Antoine Gourlay
 * @author Nicolas Fortin
 */
public interface IProgressMonitor {

    static final String PROP_PROGRESSION = "P";
    static final String PROP_CANCEL = "C";
    static final String PROP_TASKNAME = "T";

    /**
     * Create a new child task to this parent task.
     *
     * @param taskName Task name
     * @param end Number of task in the subtask
     * @return
     */
    IProgressMonitor startTask(String taskName, long end);

    /**
     * Create a new child task to this parent task.
     *
     * @param end Number of task in the subtask
     * @return
     */
    IProgressMonitor startTask(long end);

    /**
     * Ends the currently running task.
     */
    void endTask();

    /**
     * @return the overall process task name.
     */
    String getCurrentTaskName();

    /**
     * Set the overall process task name
     *
     * @param taskName
     */
    void setTaskName(String taskName);

    /**
     * Indicates the progress of the last added task.
     *
     * @param progress
     */
    void progressTo(long progress);

    /**
     * Gets the progress of the overall process.
     *
     * @return A value in the range [0-1]
     */
    double getOverallProgress();

    /**
     * Gets the progress of the current process.
     *
     * @return A value in the range [0-getEnd()[
     */
    long getCurrentProgress();

    /**
     * @return Number of task in this process
     */
    long getEnd();

    /**
     * Returns true if the process is canceled and should end as quickly as
     * possible.
     *
     * @return True if it should be canceled
     */
    boolean isCancelled();

    /**
     * Sets the cancel state of the process. This method call property change
     * listeners
     *
     * @param cancelled New value
     */
    void setCancelled(boolean cancelled);

    /**
     * Add a property change listener. The property change listener belongs to
     * the overall process.
     *
     * @param property PROP_* name
     * @param listener Listener instance
     */
    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    /**
     * @param listener PropertyChange listener to remove
     */
    void removePropertyChangeListener(PropertyChangeListener listener);
}
