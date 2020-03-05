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
import org.orbisgis.style.Uom;
import org.orbisgis.style.ViewBoxNode;
import org.orbisgis.style.parameter.TransformParameter;

/**
 *
 * @author Erwan Bocher, CNRS
 */
public class ExternalGraphic extends Graphic implements
        ViewBoxNode, IUom, TransformNode {

    private Uom uom;
    private OnlineResource onlineResource;
    private ViewBox viewBox;
    private TransformParameter transform;
    private String mimeType;

    /**
     * Build a default {@code ExternalGraphic}.
     */
    public ExternalGraphic() {
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }    
    
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public Uom getUom() {
        if (uom != null) {
            return uom;
        } else if (getParent() instanceof IUom) {
            return ((IUom) getParent()).getUom();
        } else {
            return Uom.PX;
        }
    }

    @Override
    public Uom getOwnUom() {
        return uom;
    }

    @Override
    public void setUom(Uom uom) {
        this.uom = uom;
    }

    /**
     * Get the online resource that defines this {@code MarkGraphic}.
     *
     * @return The online resource that defines this {@code MarkGraphic}.
     */
    public OnlineResource getOnlineResource() {
        return onlineResource;
    }

    /**
     * Set the online resource that defines this {@code MarkGraphic}.
     *
     * @param onlineResource the online resource that defines this
     * {@code MarkGraphic}.
     */
    public void setOnlineResource(OnlineResource onlineResource) {
        this.onlineResource = onlineResource;
        if (onlineResource != null) {
            onlineResource.setParent(this);
        }
    }

    @Override
    public ViewBox getViewBox() {
        return viewBox;
    }

    @Override
    public void setViewBox(ViewBox viewBox) {
        if (viewBox == null) {
            viewBox = new ViewBox();
        }
        this.viewBox = viewBox;
        viewBox.setParent(this);
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();

        if (viewBox != null) {
            ls.add(viewBox);
        }
        if (transform != null) {
            ls.add(transform);
        }
        if (onlineResource != null) {
            ls.add(onlineResource);
        }
        return ls;
    }

    @Override
    public TransformParameter getTransform() {
        return transform;
    }

    @Override
    public void setTransform(TransformParameter transform) {
        this.transform = transform;
        if (transform != null) {
            transform.setParent(this);
        }
    }
}
