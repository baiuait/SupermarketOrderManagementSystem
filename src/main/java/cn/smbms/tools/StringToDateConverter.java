package cn.smbms.tools;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDateConverter implements Converter<String, Date> {
    private String datePattern;
    public StringToDateConverter(String datePattern){
        this.datePattern = datePattern;
        System.out.println("StringToDateConverter convert:"+datePattern);
    }
    @Override
    public Date convert(String source) {
        try {
            return new SimpleDateFormat(datePattern).parse(source);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
