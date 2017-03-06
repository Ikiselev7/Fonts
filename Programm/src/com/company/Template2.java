package com.company;

/**
 * Created by User on 30.08.2015.
 */


public class Template2 {
    private static String header = "dx = 0\n" +
            "dy = 0\n" +
            "scale 1,1\n" +
            "lamp 100\n" +
            "speed 100,10\n" +
            "amove %s+dx,%s+dy\n" +
            "box %s,%s,0.3,100\n";

    private static String template2 = "\n" +
            "lamp 26\n" +
            "speed 100,30\n" +
            "ttfont \"%s\", %s,20,3\n" +
            "scale %s,%s\n" +
            "amove (%s+dx)/%s,(%s+dy)/%s\n" +
            "ttstring \"%s\\n";

    private static String result;

    public Template2() {
        result = "";
        result = result.concat("\n");
    }

    public void addCycle(double h_x, double h_y,double x, double y, double x1, double y1,String ttfont, String text,double h_out, double k_out) {
        result = result.concat(String.format(header,convertDoubleToString(x),convertDoubleToString(y),convertDoubleToString(h_x),convertDoubleToString(h_y)));
        result = result.concat(String.format(template2,ttfont,convertDoubleToString(h_out),convertDoubleToString(k_out),convertDoubleToString(k_out),convertDoubleToString(x1),convertDoubleToString(k_out),convertDoubleToString(k_out),convertDoubleToString(y1),text));
        result = result.concat("\n\n");
    }

    private String convertDoubleToString(double chislo) {
        String res="";
        String str = String.valueOf(chislo);
        int index = str.lastIndexOf(".");
        System.out.println(str.substring(0, index) + str.substring(index + 1, str.length()));
        res = res.concat(str.substring(0,index) + "." + str.substring(index+1,str.length()));
        return res;
    }

    public String getTemplate2() {
        return result;
    }
}
