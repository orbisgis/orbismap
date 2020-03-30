package org.orbisgis.map.renderer.featureStyle.fill;

import org.orbisgis.map.renderer.featureStyle.graphic.GraphicFillDrawer;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import org.orbisgis.map.layerModel.MapTransform;
import org.orbisgis.map.renderer.featureStyle.IFillDrawer;
import org.orbisgis.map.renderer.featureStyle.IGraphicCollectionDrawer;
import org.orbisgis.map.renderer.featureStyle.graphic.GraphicCollectionDrawer;
import org.orbisgis.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.style.fill.DensityFill;
import org.orbisgis.style.graphic.GraphicCollection;
import org.orbisgis.style.parameter.ParameterException;
import org.orbisgis.style.utils.UomUtils;

/**
 *
 * @author ebocher
 */
public class DensityFillDrawer implements IFillDrawer<DensityFill> {

    final static Map<Class, IGraphicCollectionDrawer> drawerMap = new HashMap<>();

    static {
        drawerMap.put(GraphicCollection.class, new GraphicCollectionDrawer());
    }
    private Shape shape;
    private AffineTransform affineTransform;

    @Override
    public Paint getPaint( DensityFill styleNode, MapTransform mt) throws ParameterException {
        Float percentage = (Float) styleNode.getPercentageCovered().getValue();
        if (percentage == null) {
            percentage = 0f;
        }
        percentage = percentage * styleNode.ONE_HUNDRED;
        if (percentage > styleNode.ONE_HUNDRED) {
            percentage = styleNode.ONE_HUNDRED;
        }

        if (percentage > styleNode.ONE_HALF) {
            Paint painter = null;

            if (styleNode.isHatched() && styleNode.getHatches() != null) {
                return null;
            } else if (styleNode.getGraphics() != null) {
                GraphicCollection mark = styleNode.getGraphics();
                if (drawerMap.containsKey(mark.getClass())) {
                    IGraphicCollectionDrawer markStyle = drawerMap.get(mark.getClass());
                    Rectangle2D bounds = markStyle.getBounds(mt, mark);
                    double ratio = Math.sqrt(styleNode.ONE_HUNDRED / percentage);
                    double gapX = bounds.getWidth() * ratio - bounds.getWidth();
                    double gapY = bounds.getHeight() * ratio - bounds.getHeight();
                    painter =  GraphicFillDrawer.getPaint(markStyle, mt, mark, gapX, gapY, bounds);                    
                }

            } else {
                throw new ParameterException("Neither marks or hatches are defined");
            }
            return painter;
        }
        return null;
    }

    @Override
    public void draw( Graphics2D g2, MapTransform mapTransform, DensityFill styleNode) throws ParameterException {
         if (shape != null) {
            if (styleNode.isHatched()) {
                Float alpha = (Float) styleNode.getHatchesOrientation().getValue();
                float pDist;
                if (alpha==null) {
                    throw new ParameterException("The orientation parameter cannot be null");
                }
                // Stroke width in pixel
                Float sWidth = (Float) styleNode.getHatches().getWidth().getValue();
                
                if (sWidth==null) {
                    throw new ParameterException("The hatches size parameter cannot be null");
                }
                
                float widthInPixel = UomUtils.toPixel(sWidth, styleNode.getUom(), mapTransform.getDpi(), mapTransform.getScaleDenominator());

                Float percentage = (Float) styleNode.getPercentageCovered().getValue() ;

                if (percentage==null) {
                    throw new ParameterException("The percentage covered parameter cannot be null");
                }
                
                float percentageNormalized = percentage* styleNode.ONE_HUNDRED;               
                

                if (percentageNormalized > styleNode.ONE_HUNDRED) {
                    percentageNormalized = styleNode.ONE_HUNDRED;
                }
                // Perpendiculat dist bw two hatches
                pDist = styleNode.ONE_HUNDRED * widthInPixel / percentageNormalized;
                HatchedFillDrawer.drawHatch(g2, shape, mapTransform, alpha, pDist, styleNode.getHatches(),new PenStrokeDrawer(), 0.0);
            } else {

                Paint painter = getPaint(styleNode, mapTransform);

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
    @Override
    public AffineTransform getAffineTransform() {
        return affineTransform;
    }

    @Override
    public void setAffineTransform(AffineTransform affineTransform) {
        this.affineTransform = affineTransform;
    }
}
