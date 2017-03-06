package com.company;

import com.company.Template1;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by User on 28.08.2015.
 */
public class PaintPanel extends JPanel {

    private double coefficient = 0.0;
    private static final double puncts = 12;
    private double c = 0.327;
    private static final String size110x110 = "110x110";
    private static final String size180x180 = "180x180";
    private static int temp;
    private static int scale;
    private static int type_input;
    private static int difference;

    private Template1 template1;
    private Template2 template2;
    /*Input parameters of infile*/

    private class Polygon {
        /*Input parameters*/
        double x, y;
        String text;
        double h;
        String ttfont;

        /*Output parameters*/
        double x_left,y_left;
        double length;
        double k_out;
        int h_out;
        double x_polygon,y_polygon;

        public Polygon(double _x, double _y, String _text, double _h, String _ttfont) {
            x = _x;
            y = _y;
            text = _text;
            h = _h;
            ttfont = _ttfont;
        }

        public void setNewParam(double _x_left, double _y_left, double _length, double _k_out, int _h_out) {
            x_left = _x_left;
            y_left = _y_left;
            length = _length;
            k_out = _k_out;
            h_out = _h_out;
        }

        public void setSecondsParam(double _x_polygon, double _y_polygon) {
            x_polygon = _x_polygon;
            y_polygon = _y_polygon;
        }

    }

    /*Output parameters of outfile*/
    private static double x_left, y_left;

    private Font font;
    private ArrayList<Polygon> polygonses;

    public PaintPanel(int template, double scale) {
        super();
        temp = template;
        polygonses = new ArrayList<>();
        template1 = new Template1();
        c = scale;
        repaint();
    }

    public void addPolygon(int num_temp, int scale_type, int _type_input,double _x, double _y, String _text, double _h, String _ttfont,double _k) {
        temp = num_temp;
        scale = scale_type;
        type_input = _type_input;
        Polygon polygon = new Polygon(_x,_y,_text,_h,_ttfont);
        polygonses.add(polygon);
    }
    public void removeTemp() {
        polygonses.remove(polygonses.size() - 1);
    }
    private Rectangle getStringBounds(Graphics2D g2, String str,
                                      float x, float y)
    {
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, x, y);
    }
    private int size;
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        System.out.println(temp);
        if(scale == 0) {
            size = 110;
            difference = 110 / 2;
        }
        else {
            size = 180;
            difference = 180 / 2;
        }

        RenderingHints rhc = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rhc.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rhc);

        double max = 0.0;
        Polygon p_max = new Polygon(0,0,"",0,"");
        for (Polygon p : polygonses) {
            if(p.h > max) {
                max = p.h;
                p_max = p;
            }
        }
        int h_out = (int)(max/c);
        font = new Font(p_max.ttfont, Font.PLAIN, h_out);
        g2d.setFont(font);

        FontRenderContext frch = g2d.getFontRenderContext();
        // Draw the ascent.
        LineMetrics lmh = font.getLineMetrics(p_max.text, frch);
        coefficient = c*(double)(h_out/lmh.getAscent());
        System.out.println("coefficient: "+coefficient);

        g2d.drawLine(0, 0, (int)((double)size/coefficient),0);
        g2d.drawLine(0, 0, 0, (int) ((double) size / coefficient));
        g2d.drawLine((int)((double)size/coefficient),0,(int)((double)size/coefficient),(int)((double)size/coefficient));
        g2d.drawLine(0, (int) ((double) size / coefficient), (int) ((double) size / coefficient),(int)((double)size/coefficient));

        g2d.drawLine(0, (int) ((double) size / coefficient) / 2, (int) ((double) size / coefficient), (int) ((double) size / coefficient) / 2);
        g2d.drawLine((int) ((double) size / coefficient) / 2, 0, (int) ((double) size / coefficient) / 2, (int) ((double) size / coefficient));

        for(Polygon p : polygonses) {
            int _h_out = (int)(p.h/c);
            font = new Font(p.ttfont, Font.PLAIN, _h_out);
            g2d.setFont(font);

            FontRenderContext frc = g2d.getFontRenderContext();
            float width = (float) font.getStringBounds(p.text, frc).getWidth();

            // Draw the ascent.
            LineMetrics lm = font.getLineMetrics(p.text, frc);

            System.out.println("Width: " + width + "; ascent: " + lm.getAscent() + "; descent: " + lm.getDescent() + "; leading: " + lm.getLeading());

            double _length = c* width * (p.h_out/lm.getAscent());
            double _k_out = (p.h / (c * _h_out));

            if(temp == 0)
                paintTemplate1(g2d,p,_length,_k_out,_h_out,(lm.getAscent()),(int)width);
            else {
                if (type_input == 0)
                    paintTemplate2_1(g2d, p, _length, _k_out, _h_out);
                else
                    paintTemplate2_2(g2d, p, _length, _k_out, _h_out);
            }
        }
    }

    private void paintTemplate1(Graphics2D g2d, Polygon p, double _length, double _k_out, int _h_out, float asc, int adv) {
        x_left = p.x - _length / 2;
        y_left = p.y;
        p.setNewParam(x_left, y_left, _length, _k_out, _h_out);

        System.out.println("Parameters:  x - " + x_left + " y - " + y_left + " length -  " + _length + " c - " + c + " h_puncts - " + _h_out);
        Rectangle bounds = getStringBounds(g2d, p.text,((int) ((p.x_left+difference)  /coefficient)), (int) (2 * difference / coefficient) - (int) (((p.y_left / coefficient) + (int) (difference / coefficient))));
        g2d.draw(bounds);
        System.out.println("Coordinates: x - " + ((int) ((p.x_left + difference) / coefficient)) + " y - " + ((int) (2 * difference / coefficient) - (int) (((p.y_left / coefficient) + (int) (difference / coefficient)))));

        g2d.drawString(p.text, ((int) ((p.x_left+difference) / coefficient)), (int) (2 * difference / coefficient) - (int) (((p.y_left / coefficient) + (int) (difference / coefficient))));
    }

    private void paintTemplate2_1(Graphics2D g2d, Polygon p, double _length, double _k_out, int _h_out) {
        x_left = (int) (-_length/2);
        y_left = 0;
        p.setNewParam(x_left, y_left, _length, _k_out, _h_out);
        p.setSecondsParam((-(p.x / 2)), (-(p.y - p.h) / 2));
        g2d.drawString(p.text, (int) (p.x_left / coefficient) + (int) (difference / coefficient), (int) (2 * difference / coefficient) - (int) (((p.y_left / coefficient) + (int) (difference / coefficient))));
        g2d.drawRect((int) (p.x_polygon / coefficient) + (int) (difference / coefficient), (int) (2 * difference / coefficient) - (int) (((p.y_polygon / coefficient + (int) (p.y / coefficient)) + (int) (difference / coefficient))), (int) (p.x / coefficient), (int) (p.y / coefficient));
    }

    private void paintTemplate2_2(Graphics2D g2d, Polygon p, double _length, double _k_out, int _h_out) {
        x_left = (int) (-_length/2);
        y_left = 0;
        p.setNewParam(x_left, y_left, _length, _k_out, _h_out);
        p.setSecondsParam((-p.h-(_length*_k_out / 2)), -p.h);
        g2d.drawString(p.text, (int) (p.x_left / coefficient) + (int) (difference / coefficient), (int) (2 * difference / coefficient) - (int) (((p.y_left / coefficient) + (int) (difference / coefficient))));
        g2d.drawRect((int) (p.x_polygon / coefficient) + (int) (difference / coefficient), (int) (2 * difference / coefficient) - (int) (((p.y_polygon / coefficient + (int) (p.y / coefficient)) + (int) (difference / coefficient))), (int) ((2*p.h + _length*_k_out)/ coefficient), (int) (2*p.h / coefficient));

    }


    public void generateTemplate1() {
        template1 = new Template1();
        char A = 'A';
        char B = 'A';
        int i = 0;
        for(Polygon p : polygonses) {
            String AA = String.valueOf(A);
            AA = AA.concat(String.valueOf(B));
            template1.addCycle(AA, p.length/2,p.x, p.y_left, p.ttfont, p.h_out, p.k_out, p.text);
            i++;
            B = (char)(B + 1);
            if( i == 26 ) {
                i = 0;
                A = (char)(A+1);
                B = 'A';
            }
        }
        System.out.println(template1.getTemplate1());
    }

    public void generateTemplate2() {
        template2 = new Template2();
        for(Polygon p : polygonses) {
            template2.addCycle(p.x,p.y,p.x_left,p.y_left,p.x_polygon,p.y_polygon,p.ttfont,p.text,p.h_out,p.k_out);
        }
        System.out.println(template1.getTemplate1());
    }

    public String getTemplate() {
        if(temp == 0)
        {
            repaint();
            generateTemplate1();
            return template1.getTemplate1();
        }
        else
        {
            repaint();
            generateTemplate2();
            return template2.getTemplate2();
        }
    }

}
