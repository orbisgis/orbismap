/**
 * Map is part of the OrbisGIS platform
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
 * Map is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.layerModel;

import org.locationtech.jts.geom.Envelope;
import org.orbisgis.orbismap.map.api.IMapEnvelope;

/**
 * Extend the JTS envelope to manage SRID identifier
 * 
 * @author Erwan Bocher, CNRS, 2020
 */
public class MapEnvelope extends Envelope implements IMapEnvelope{

    
    private int srid = 0;
    
    public MapEnvelope(Envelope envelope){
        super(envelope);
    } 
    public MapEnvelope(Envelope envelope, int srid){
        super(envelope);
        this.srid=srid;
    } 
   

    public MapEnvelope() {
        super();
    }

    
    @Override
    public int getSrid() {
        return srid;
    }

    /**
     * Identifier to find CRS parameters
     * @param srid 
     */
    public void setSrid(int srid) {
        this.srid = srid;
    }
    
    
    
    
}
