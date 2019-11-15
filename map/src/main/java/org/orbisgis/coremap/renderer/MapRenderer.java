/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ProgressMonitor;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.layerModel.MapContext;
import org.orbisgis.coremap.layerModel.model.ILayer;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.utils.progress.IProgressMonitor;
import org.orbisgis.coremap.utils.progress.NullProgressMonitor;

/**
 *
 * @author Erwan Bocher
 */
public class MapRenderer implements IRenderer{

    int width = 800;
    int height = 300;
    private MapContext mc;
    private MapTransform mt;
    private IProgressMonitor pm;

    public MapRenderer() {
        this(600, 600);
    }

    public MapRenderer(int width, int height) {
        this.width = width;
        this.height = height;
        init();
    }

    final void init() {
        mc = new MapContext();
        mt = new MapTransform();
    }

    public IProgressMonitor getProgressMonitor () {
        return pm;
    }

    public void setProgressMonitor (IProgressMonitor pm) {
        this.pm = pm;
    }  
    
    

    public boolean addLayer(ILayer layer) throws LayerException {
        return mc.getLayerModel().addLayer(layer);
    }

    public boolean removeLayer(ILayer layer) throws LayerException {
        return mc.getLayerModel().removeLayer(layer);
    }

    /**
     * Draw the map
     */
    @Override
    public void draw() {
        if(mt.getAdjustedExtent()==null){
            mt.setExtent(mc.getLayerModel().getEnvelope());       
            mt.resizeImage(width, height);
        }
        BufferedImage image = mt.getImage();
        Graphics2D g2 = image.createGraphics();
        try {        
            mc.getLayerModel().draw(g2, mt, getProgressMonitor()==null? new NullProgressMonitor():pm);
        } catch (LayerException ex) {
            Logger.getLogger(MapRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @return
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * 
     * @param filePath
     * @return
     * @throws IOException 
     */
    public boolean save(String filePath) throws IOException {
        return save(filePath, true);
    }
    
    /**
     * 
     * @param filePath
     * @param delete
     * @return
     * @throws IOException 
     */
    public boolean save(String filePath, boolean delete) throws IOException {
        File fileOut = new File(filePath);
        if (fileOut.isDirectory()) {
            return false;
        }
        if (fileOut.exists() && delete) {
            BufferedImage image = mt.getImage();
            return ImageIO.write(image, "png", fileOut);
        } else if (!fileOut.exists()) {
            return ImageIO.write(mt.getImage(), getExtension(fileOut), fileOut);
        } else {
            return false;
        }
    }

    /**
     * Return the extension of the file
     * @param file
     * @return 
     */
    public  String getExtension(File file) {
        String path = file.getAbsolutePath();
        String extension = "";
        int i = path.lastIndexOf('.');
        if (i >= 0) {
           return  extension = path.substring(i + 1).toLowerCase();
        }
        return extension;
    }
}
