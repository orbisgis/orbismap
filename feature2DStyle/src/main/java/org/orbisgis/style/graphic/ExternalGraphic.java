/**
 * Feature2DStyle is part of the OrbisGIS platform
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
 * Feature2DStyle is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.style.graphic;

import org.orbisgis.style.graphic.graphicSize.GraphicSize;
import org.orbisgis.style.ITransformNode;
import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.Uom;
import org.orbisgis.style.transform.Transform;
import org.orbisgis.style.ITransform;

/**
 *
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class ExternalGraphic extends Graphic implements
        IUom, ITransformNode {

    private Uom uom;
    private OnlineResource onlineResource;
    private GraphicSize graphicSize;
    private Transform transform;
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
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (graphicSize != null) {
            ls.add(graphicSize);
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
    public Transform getTransform() {
        return transform;
    }

    @Override
    public void setTransform(Transform transform) {
        this.transform = transform;
        if (transform != null) {
            transform.setParent(this);
        }
    }

    @Override
    public GraphicSize getGraphicSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setGraphicSize(GraphicSize graphicSize) {
        this.graphicSize = graphicSize;
        if (this.graphicSize != null) {
            graphicSize.setParent(this);
        }
    }

    @Override
    public void addTransform(ITransform transformation) {
        if (transform != null) {
            transform.addTransformation(transformation);
        }
    }
}
