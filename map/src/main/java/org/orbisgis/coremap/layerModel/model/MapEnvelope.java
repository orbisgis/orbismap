/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.layerModel.model;

import org.locationtech.jts.geom.Envelope;

/**
 * 
 * @author ebocher
 */
public class MapEnvelope extends Envelope{

    private final int srid;
    
    public MapEnvelope(Envelope envelope, int srid){
        super(envelope);
        this.srid=srid;        
    }
    
}
