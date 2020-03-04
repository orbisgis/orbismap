package org.orbisgis.coremap.renderer.featureStyle.fill;

import org.orbisgis.coremap.renderer.featureStyle.graphic.GraphicFillDrawer;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.featureStyle.IFillDrawer;
import org.orbisgis.coremap.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.coremap.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.orbisdata.datamanager.jdbc.JdbcSpatialTable;
import org.orbisgis.style.fill.DensityFill;
import org.orbisgis.style.fill.GraphicFill;
import org.orbisgis.style.fill.HatchedFill;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class DensityFillDrawer implements IFillDrawer<DensityFill> {

    final static Map<Class, IGraphicDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(GraphicFill.class, new GraphicFillDrawer());
    }

    @Override
    public Paint getPaint(JdbcSpatialTable sp, DensityFill styleNode, Map<String, Object> properties, MapTransform mt) throws ParameterException, SQLException {
        double percentage = 0.0;
        if (styleNode.getPercentageCovered() != null) {
            percentage = styleNode.getPercentageCovered().getValue(properties) * styleNode.ONE_HUNDRED;
        }
        if (percentage > styleNode.ONE_HUNDRED) {
            percentage = styleNode.ONE_HUNDRED;
        }

        if (percentage > styleNode.ONE_HALF) {
            Paint painter = null;

            if (styleNode.isHatched() && styleNode.getHatches() != null) {
                return null;
            } else if (styleNode.getGraphic() != null) {
                Graphic mark = styleNode.getGraphic();
                if (drawerMap.containsKey(mark.getClass())) {
                    IGraphicDrawer markStyle = drawerMap.get(mark.getClass());
                    Rectangle2D bounds = markStyle.getBounds(sp, mt, mark, properties);
                    double ratio = Math.sqrt(styleNode.ONE_HUNDRED / percentage);
                    double gapX = bounds.getWidth() * ratio - bounds.getWidth();
                    double gapY = bounds.getHeight() * ratio - bounds.getHeight();
                    painter =  GraphicFillDrawer.getPaint(sp, markStyle, properties, mt, mark, gapX, gapY, bounds);
                    
                }

            } else {
                throw new ParameterException("Neither marks or hatches are defined");
            }
            return painter;
        }
        return null;
    }

    @Override
    public void draw(JdbcSpatialTable sp, Graphics2D g2, MapTransform mapTransform, DensityFill styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
        Shape shp = (Shape) properties.get("shape");
        if (shp != null) {
            if (styleNode.isHatched()) {
                double alpha = HatchedFill.DEFAULT_ALPHA;
                double pDist;

                if (styleNode.getHatchesOrientation() != null) {
                    alpha = styleNode.getHatchesOrientation().getValue(properties);
                }
                // Stroke width in pixel
                double sWidth = sp.getDouble(styleNode.getHatches().getWidth().getIdentifier());
                
                double widthInPixel = UomUtils.toPixel(sWidth, styleNode.getUom(), mapTransform.getDpi(), mapTransform.getScaleDenominator(), null);

                double percentage = 0.0;

                if (styleNode.getPercentageCovered() != null) {
                    percentage = styleNode.getPercentageCovered().getValue(properties) * styleNode.ONE_HUNDRED;
                }

                if (percentage > styleNode.ONE_HUNDRED) {
                    percentage = styleNode.ONE_HUNDRED;
                }
                // Perpendiculat dist bw two hatches
                pDist = styleNode.ONE_HUNDRED * widthInPixel / percentage;
                HatchedFillDrawer.drawHatch(sp, g2, properties, shp, mapTransform, alpha, pDist, styleNode.getHatches(),new PenStrokeDrawer(), 0.0);
            } else {

                Paint painter = getPaint(sp, styleNode, properties, mapTransform);

                if (painter != null) {
                    g2.setPaint(painter);
                    g2.fill(shp);
                }
            }
        }
    }

}
