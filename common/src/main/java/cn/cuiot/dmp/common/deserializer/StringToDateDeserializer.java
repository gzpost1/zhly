package cn.cuiot.dmp.common.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author wuyongchong
 * @date 2020/3/25
 */
@JacksonStdImpl
public class StringToDateDeserializer extends JsonDeserializer<Date> {

    private final Lock lock = new ReentrantLock();

    protected final List<String> formarts;

    public StringToDateDeserializer() {
        formarts = new ArrayList<>(7);
        formarts.add("yyyy-MM");
        formarts.add("yyyy-MM-dd");
        formarts.add("yyyy-MM-dd HH:mm");
        formarts.add("yyyy-MM-dd HH:mm:ss");
        formarts.add("yyyy-MM-dd'T'HH:mm:ss");
        formarts.add("HH:mm:ss");
        formarts.add("HH:mm");
    }

    @Override
    public Class<?> handledType() {
        return Date.class;
    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        if (StringUtils.isNotBlank(jsonParser.getText())) {
            String str = jsonParser.getText().trim();
            if (str.length() == 0) {
                return (Date) this.getEmptyValue(context);
            } else {
                Date val = null;
                lock.lock();
                try {
                    if (str.matches("^\\d{4}-\\d{1,2}$")) {
                        val = parseDate(str, formarts.get(0));
                    } else if (str.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
                        val = parseDate(str, formarts.get(1));
                    } else if (str.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
                        val = parseDate(str, formarts.get(2));
                    } else if (str.matches(
                            "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                        val = parseDate(str, formarts.get(3));
                    } else if (str.contains("T")) {
                        val = parseDate(str, formarts.get(4));
                    } else if (str.matches("^\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                        val = parseDate(str, formarts.get(5));
                    } else if (str.matches("^\\d{1,2}:\\d{1,2}$")) {
                        val = parseDate(str, formarts.get(6));
                    } else {
                        val = parseDate(str, formarts.get(3));
                    }
                } catch (ParseException e) {
                    return (Date) context.handleWeirdStringValue(this.handledType(), str,
                            "expected format \"%s\"", new Object[]{this.formarts.toString()});
                } finally {
                    lock.unlock();
                }
                return val;
            }
        }
        return null;
    }

    private Date parseDate(String dateStr, String format) throws ParseException {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw e;
        }
        return date;
    }
}