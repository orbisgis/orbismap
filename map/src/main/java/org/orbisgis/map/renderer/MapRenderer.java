/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.map.renderer;

import java.awt.Color;
import org.orbisgis.map.api.IRenderer;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.locationtech.jts.geom.Envelope;
import org.orbisgis.map.layerModel.MapContext;
import org.orbisgis.map.layerModel.MapEnvelope;
import org.orbisgis.map.api.ILayer;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.utils.progress.NullProgressMonitor;
import org.orbisgis.map.api.LayerException;

/**
 *
 * @author Erwan Bocher, CNRS
 */
public class MapRenderer implements IRenderer{

    int width = 800;
    int height = 800;
    private MapContext mc;
    private MapTransform mt;
    private BufferedImage image;

    public MapRenderer() {
        this(800, 800);
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
    
    public void addLayer(ILayer layer) throws LayerException {
         mc.add(layer);
    }

    public void removeLayer(ILayer layer) throws LayerException {
         mc.remove(layer);
    }
    
    void setExtend(Envelope envelope){
        mt.setExtent(new MapEnvelope(envelope));
        mt.resizeImage(width, height);
    }

    /**
     * Draw the map
     * @throws org.orbisgis.map.api.LayerException
     */
    @Override
    public void draw() throws LayerException{
        if(mt.getAdjustedExtent()==null){
            mt.setExtent((MapEnvelope) mc.getLayerModel().getEnvelope());       
            mt.resizeImage(width, height);
        }
        image = mt.getImage();
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        g2.addRenderingHints(mt.getRenderingHints());                
        mc.getLayerModel().draw(g2, mt, new NullProgressMonitor());
    }

    public BufferedImage getImage() {
        return image;
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
            return ImageIO.write(mt.getImage(), getExtension(fileOut), fileOut);
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
    private String getExtension(File file) {
        String path = file.getAbsolutePath();
        String extension = "";
        int i = path.lastIndexOf('.');
        if (i >= 0) {
           return  path.substring(i + 1).toLowerCase();
        }
        return extension;
    }
    
    public void show(){
        Icon icon = new ImageIcon(mt.getImage());
        JLabel label = new JLabel(icon);
        final JFrame f = new JFrame("Display map");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(label);
        f.pack();
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }

    public void setEnvelope(MapEnvelope envelope) {
        mt.setExtent(envelope);
        mt.resizeImage(width, height);
    }
}
