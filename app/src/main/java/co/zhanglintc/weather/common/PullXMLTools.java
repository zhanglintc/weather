package co.zhanglintc.weather.common;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by yanbin on 2015/11/04.
 */
public final class PullXMLTools {

    public PullXMLTools() {
    }

    /**
     * 获取XML文件，返回数据流
     *
     * @param inputStream 获取XML文件
     * @param encoding 编码格式
     * @param language 系统语言
     * @return 画面显示文字
     * @throws XmlPullParserException
     */
    public static Map<String,Object> parseXML(InputStream inputStream, String encoding, String language) throws Exception {

        Map<String,Object> cityMap = new HashMap();

        // 创建XML解析工厂
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

        // 获取XML解析类的引用
        XmlPullParser parser = factory.newPullParser();

        parser.setInput(inputStream, encoding);

        // 取得的语言ID
        String id = null;

        // 获取事件类型
        int eventType = parser.getEventType();

        while(eventType!=XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
//                    if ("language".equals((parser.getName()))) {
//                        // 取出属性值
//                        id = parser.getAttributeValue(0);
//                    }
                    break;
                case XmlPullParser.START_TAG:
                    if ("language".equals((parser.getName()))) {
                        // 取出属性值
                        id = parser.getAttributeValue(0);
                    } else
                    if (language.equals(id) && "city".equals((parser.getName()))) {
                        cityMap.put("city", parser.getText());
                    } else if (language.equals(id) && "today".equals((parser.getName()))) {
                        cityMap.put("today", parser.getText());
                    } else if (language.equals(id) && "tomorrow".equals((parser.getName()))) {
                        cityMap.put("tomorrow", parser.getText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = parser.next();
        }
        inputStream.close();

        return cityMap;
    }
}
