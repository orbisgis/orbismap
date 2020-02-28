/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.layerModel;

import org.locationtech.jts.geom.Envelope;
import org.orbisgis.map.api.IMapEnvelope;

/**
 * Envelope
 * 
 * @author ebocher
 */
public class MapEnvelope extends Envelope implements IMapEnvelope{

       
    
    public MapEnvelope(Envelope envelope){
        super(envelope);
    }   
   

    public MapEnvelope() {
        super();
    }
    
}
