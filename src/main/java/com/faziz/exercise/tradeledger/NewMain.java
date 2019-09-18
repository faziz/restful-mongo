package com.faziz.exercise.tradeledger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.faziz.exercise.tradeledger.controller.filter.Filter;
import com.faziz.exercise.tradeledger.controller.filter.Filter.Range;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class NewMain {

    public static void main(String[] args) throws IOException {
        Filter f = new Filter();
        f.setAttribute("id");
        f.setOperator("eq");
        f.setRange(new Range("507f191e810c19729de860e1", "507f191e810c19729de860e3"));
        
        ObjectMapper obm = new ObjectMapper();

        StringWriter w = new StringWriter();
        obm.writeValue(w, f);
        
        String str = w.toString();
        System.out.println(str);
        
        String encode = URLEncoder.encode(str, Charset.forName("UTF-8").name());
        System.out.println(encode);
        
    }
}
