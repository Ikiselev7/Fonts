package com.company;

/**
 * Created by User on 30.08.2015.
 */


public class Template1 {

    private static String header = "dx = 0\n" +
            "dy = 0\n" +
            "k = 10\n" +
            "n1 = 100\n" +
            "n2 = 30\n";
    private static String template1 = "S = \"%s\"\n" +
            "for i = 1 to k\n" +
            "{\n" +
            "scale 1,1\n" +
            "beginpolygon S+i\n" +
            "scale %s,%s\n" +
            "amove -%s"+"+("+"%s+dx"+")"+"/"+"%s, "+"("+"%s+dy"+")"+"/"+"%s\n" +
            "ttfont \"%s\", %d,0,3\n" +
            "ttstring \"%s\"\n" +
            "endpolygon\n" +
            "\n" +
            "lamp n1\n" +
            "speed 100,10\n" +
            "fill S+i,40,45*i\n" +
            "fill S+i,0,0\n" +
            "\n" +
            "lamp n2\n" +
            "speed 100,15\n" +
            "fill S+i,20,45*i+45\n" +
            "fill S+i,0,0\n" +
            "\n" +
            "}\n\n";

    private static String result;

    public Template1() {
        result = "";
        result = result.concat(header);
        result = result.concat("\n");
        System.out.println(result);
    }

    public void addCycle(String AA, double length, double x, double y, String ttfont, int h, double k, String text) {
        System.out.println(x+" " +y+" " +k+"flag");
        result = result.concat(String.format(template1,AA,convertDoubleToString(k),convertDoubleToString(k),convertDoubleToString(length),convertDoubleToString(x),convertDoubleToString(k),convertDoubleToString(y),convertDoubleToString(k),ttfont,h,text));
    }

    private String convertDoubleToString(double chislo) {
        String res="";
        double chis = chislo;
        String str = String.valueOf(chis);
        int index = str.lastIndexOf(".");
        System.out.println(str.substring(0,index)+str.substring(index+1,str.length()));
        res = res.concat(str.substring(0,index) + "." + str.substring(index+1,str.length()));
        return res;
    }

    public String getTemplate1() {
        return result;
    }
}
