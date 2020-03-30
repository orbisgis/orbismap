/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.style.graphic;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.Uom;

/**
 *
 * @author ebocher
 */
public class GraphicCollection extends StyleNode implements IUom{

    ArrayList<Graphic> graphics;
    
    public GraphicCollection(){
        graphics = new ArrayList<>();
    }

    public GraphicCollection(Graphic graphic) {
         graphics = new ArrayList<>();
         graphics.add(graphic);
     }
    
     /**
     * Add a graphic in this collection, at index i if<code>i &lt;= getNumGraphics()-1 &amp;&amp; i &gt;= 0 </code>,
     * or in the end of the collection (ie at index n+1, if the collection contains
     * n elements before the insertion) if this condition is no satisfied.
     * @param graphic
     * @param index
     */
    public void addGraphic(Graphic graphic, int index) {
        if (graphic != null) {
            if (index >= 0 && index < graphics.size()) {
                graphics.add(index, graphic);
            } else {
                graphics.add(graphic);
            }
            graphic.setParent(this);
        }
    }
    
    /**
     * Get the number of inner graphic symbols.
     * @return 
     */
    public int getNumGraphics() {
        return graphics.size();
    }

    public ArrayList<Graphic> getGraphics() {
        return graphics;
    }    
    
    
    public boolean add(Graphic graphic){
        if (graphic != null) {
            graphics.add(graphic);
            graphic.setParent(this);
            return true;
        }
        return false;
    }
    
    public Graphic get(int index){
        return graphics.get(index);
    }
    
    public boolean remove(Graphic graphic){
        return graphics.remove(graphic);
    }
    
    public Graphic remove(int index){
        return  graphics.remove(index);
    }
    
     /**
     * Move the graphic at index i (if any) down in the collection, ie at position
     * i+1. If <code> i >= n-1 </code>, where n is the size of the collection, or 
     * if <code> 0 > i </code>, nothing is done.
     * 
     * @param index
     * @return 
     */
    public boolean moveGraphicDown(int index) {
        if (index >= 0 && index < graphics.size() - 1) {
            Graphic g = graphics.get(index);
            graphics.set(index, graphics.get(index+1));
            graphics.set(index + 1, g);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Move the graphic at index i (if any) up in the collection, ie at position i-1.
     * If <code>0 >= i</code> or <code>i > n-1 </code>, where n is the size of the collection,
     * nothing is done.
     * @param index
     * @return 
     */
    public boolean moveGraphicUp(int index) {
        if (index > 0 && index < graphics.size()) {
            Graphic g = graphics.get(index);
            graphics.set(index, graphics.get(index-1));
            graphics.set(index - 1, g);
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        ls.addAll(graphics);
        return ls;
    }

 /**
     * Won't do anything : graphic collections are not intended to have a
     * dedicated unit of measure.
     * @param u
     */
    @Override
    public void setUom(Uom u) {
    }

    /**
     * Returns {@link Uom#PX}, as it is the default value for {@code Uom}
     * instances.
     * @return
     */
    @Override
    public Uom getOwnUom() {
            return Uom.PX;
    }

    @Override
    public Uom getUom() {
            if(getParent() instanceof IUom){
                    return ((IUom)getParent()).getUom();
            } else {
                    return Uom.PX;
            }
    }
}
