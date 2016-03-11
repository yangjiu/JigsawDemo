package com.newtonker.jigsawdemo.utils;

import android.content.Context;
import android.util.SparseArray;

import com.newtonker.jigsawdemo.model.TemplateEntity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParserHelper
{
    private static SparseArray<List<TemplateEntity>> mEntityList;

    private static ParserHelper instance;

    private ParserHelper(Context context)
    {
        init(context);
    }

    public static ParserHelper getInstance(Context context)
    {
        if(null == instance)
        {
            instance = new ParserHelper(context.getApplicationContext());
        }

        return instance;
    }

    public static void init(Context context)
    {
        try
        {
            mEntityList = parseXml(context.getAssets().open("templates.xml"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public List<TemplateEntity> getEntityList(int type)
    {
        if(null == mEntityList)
        {
            return null;
        }

        return mEntityList.get(type);
    }

    /**
     * 解析xml
     * @param is
     * @return
     */
    private static SparseArray<List<TemplateEntity>> parseXml(InputStream is)
    {
        List<TemplateEntity> entityList = new ArrayList<>();
        try
        {
            TemplateEntity entity = null;

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "utf-8");
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                case XmlPullParser.START_TAG:
                    String tagName = parser.getName();
                    if (null != tagName && tagName.equals("layout"))
                    {
                        entity = new TemplateEntity();
                        int numOfSlots = Integer.parseInt(parser.getAttributeValue(null, "numOfSlots"));
                        entity.setNumOfSlots(numOfSlots);
                    }

                    if(null != tagName && tagName.equals("id") && null != entity)
                    {
                        String id = parser.nextText();
                        entity.setId(Integer.parseInt(id));
                    }

                    if(null != tagName && tagName.equals("points") && null != entity)
                    {
                        String points = parser.nextText();
                        entity.setPoints(points);
                    }

                    if(null != tagName && tagName.equals("polygons") && null != entity)
                    {
                        String polygons = parser.nextText();
                        entity.setPolygons(polygons);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(parser.getName().equals("layout"))
                    {
                        entityList.add(entity);
                    }
                    break;
                default:
                    break;
                }
                eventType = parser.next();
            }
        }
        catch (XmlPullParserException | IOException e)
        {
            e.printStackTrace();
            return null;
        }

        // 对list解析，并放到SparseArray中
        SparseArray<List<TemplateEntity>> sparseArray = new SparseArray<>();

        List<TemplateEntity> tempList0 = new ArrayList<>();
        List<TemplateEntity> tempList1 = new ArrayList<>();
        List<TemplateEntity> tempList2 = new ArrayList<>();
        List<TemplateEntity> tempList3 = new ArrayList<>();

        for(TemplateEntity temp : entityList)
        {
            switch(temp.getNumOfSlots())
            {
            case 1:
                tempList0.add(temp);
                break;
            case 2:
                tempList1.add(temp);
                break;
            case 3:
                tempList2.add(temp);
                break;
            case 4:
                tempList3.add(temp);
                break;
            default:
                break;
            }
        }

        // 在循环结束后将模版的集合按照键值对方式存放
        sparseArray.append(0, tempList0);
        sparseArray.append(1, tempList1);
        sparseArray.append(2, tempList2);
        sparseArray.append(3, tempList3);

        return sparseArray;
    }
}
