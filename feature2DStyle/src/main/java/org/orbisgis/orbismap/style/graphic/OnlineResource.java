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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.style.graphic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.StyleNode;

/**
 * An {@code OnlineResource} is used to keep a reference to an graphic resource
 * that is stored on disk, or in a remote location, as an image.</p>
 * <p>An online resource is directly dependant on an URL that will be used to
 * retrieve the image we need.
 * 
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 * 
 * @todo implements MarkGraphicSource
 */
public class OnlineResource extends StyleNode implements ExternalGraphicSource {


    private URI uri;

    /**
     * Build a new {@code OnlineResource}
     */
    public OnlineResource() {
        uri = null;
    }

    /**
     * Build a new {@code OnlineResource} with the given String, that is supposed
     * to be an URL.
     * @param url
     * @throws URISyntaxException
     * If {@code url} can't be used to build an {@code URL} instance.
     */
    public OnlineResource(String url) throws URISyntaxException {
        this.uri = new URI(url);
    }

    
    /**
     * Get the {@code URL} contained in this {@code OnlineResource}.
     * @return
     * An {@code URL} instance, that points to the location where to find the
     * resource.
     */
    public URI getUri() {
        return uri;
    }

    /**
     * Set the {@code URL} contained in this {@code OnlineResource}.
     * @param url
     * @throws URISyntaxException
     */
    public void setUri(String url) throws URISyntaxException {
        if (url == null || url.isEmpty()) {
            this.uri = null;
        } else {
            this.uri = new URI(url);
        }
    }
    
    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ret = new ArrayList<IStyleNode>();
        return ret;
    }
}
