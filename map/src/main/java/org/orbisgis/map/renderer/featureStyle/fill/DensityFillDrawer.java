package org.orbisgis.map.renderer.featureStyle.fill;

import org.orbisgis.map.renderer.featureStyle.graphic.GraphicFillDrawer;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IFillDrawer;
import org.orbisgis.map.renderer.featureStyle.IGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.graphic.MarkGraphicDrawer;
import org.orbisgis.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.map.renderer.featureStyle.utils.ValueHelper;
import org.orbisgis.style.fill.DensityFill;
import org.orbisgis.style.fill.GraphicFill;
import org.orbisgis.style.graphic.Graphic;
import org.orbisgis.style.graphic.MarkGraphic;
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
        drawerMap.put(MarkGraphic.class, new MarkGraphicDrawer());
    }
    private Shape shape;

    @Override
    public Paint getPaint( DensityFill styleNode, Map<String, Object> properties, MapTransform mt) throws ParameterException, SQLException {
        Double percentage = ValueHelper.getAsDouble(properties, styleNode.getPercentageCovered());
        if (percentage == null) {
            percentage = 0D;
        }
        percentage = percentage * styleNode.ONE_HUNDRED;
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
                    Rectangle2D bounds = markStyle.getBounds(mt, mark, properties);
                    double ratio = Math.sqrt(styleNode.ONE_HUNDRED / percentage);
                    double gapX = bounds.getWidth() * ratio - bounds.getWidth();
                    double gapY = bounds.getHeight() * ratio - bounds.getHeight();
                    painter =  GraphicFillDrawer.getPaint(markStyle, properties, mt, mark, gapX, gapY, bounds);
                    
                }

            } else {
                throw new ParameterException("Neither marks or hatches are defined");
            }
            return painter;
        }
        return null;
    }

    @Override
    public void draw( Graphics2D g2, MapTransform mapTransform, DensityFill styleNode, Map<String, Object> properties) throws ParameterException, SQLException {
         if (shape != null) {
            if (styleNode.isHatched()) {
                Double alpha = ValueHelper.getAsDouble(properties,styleNode.getHatchesOrientation());
                double pDist;
                if (alpha==null) {
                    throw new ParameterException("The orientation parameter cannot be null");
                }
                // Stroke width in pixel
                Float sWidth = ValueHelper.getAsFloat(properties, styleNode.getHatches().getWidth());
                
                if (sWidth==null && sWidth>=0) {
                    throw new ParameterException("The hatches size parameter cannot be null");
                }
                
                float widthInPixel = UomUtils.toPixel(sWidth, styleNode.getUom(), mapTransform.getDpi(), mapTransform.getScaleDenominator());

                Double percentage = ValueHelper.getAsDouble(properties, styleNode.getPercentageCovered()) ;

                if (percentage==null) {
                    throw new ParameterException("The percentage covered parameter cannot be null");
                }
                
                double percentageNormalized = percentage* styleNode.ONE_HUNDRED;               
                

                if (percentageNormalized > styleNode.ONE_HUNDRED) {
                    percentageNormalized = styleNode.ONE_HUNDRED;
                }
                // Perpendiculat dist bw two hatches
                pDist = styleNode.ONE_HUNDRED * widthInPixel / percentageNormalized;
                HatchedFillDrawer.drawHatch(g2, properties, shape, mapTransform, alpha, pDist, styleNode.getHatches(),new PenStrokeDrawer(), 0.0);
            } else {

                Paint painter = getPaint(styleNode, properties, mapTransform);

                if (painter != null) {
                    g2.setPaint(painter);
                    g2.fill(shape);
                }
            }
        }
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
