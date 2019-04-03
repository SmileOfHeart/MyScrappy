package Page;


//import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageParserTool {

	/*
	  *  分析页面的url,将指向html的url摘出，图片的url直接下载
	 */
	public static List<String> ParsePage(String content,String HomeDir)
	{
		List<String> urlList= new ArrayList<String>();
		//获取url
		Pattern pattern = Pattern.compile("(href=\"(.+?)\")|(src=\"(.+?)\")");
		Matcher matcher=pattern.matcher(content);
		while(matcher.find())
		{
			    String url=matcher.group();
			    if(url.charAt(0)=='h')
			        url=url.substring(6,url.length()-1); //href
			    else
			        url=url.substring(5,url.length()-1); //src
			    if(url.contains("http"))
			    	urlList.add(url);
		}
		//对url进行处理
		Pattern Fiter=Pattern.compile("(css)|(xml)|(.js)|(#comments)|(rss)");
		Pattern img=Pattern.compile("(png)|(jpg)");
		for(int i=0;i<urlList.size();i++)
		{
			matcher=Fiter.matcher(urlList.get(i));
			if(matcher.find())
			{
				urlList.remove(i--);
				continue;
			}
			matcher=img.matcher(urlList.get(i));
			if(matcher.find())
			{				
				RequestAndResponseTool.DownloadPic(urlList.get(i), HomeDir,matcher.group());
				urlList.remove(i--);
			}
		}
		return urlList;
	}
	
	
	

//    /* 通过选择器来选取页面的 */
//    public static Elements select(OnePage page , String cssSelector) {
//        return page.getDoc().select(cssSelector);
//    }
//
//    /*
//     *  通过css选择器来得到指定元素;
//     *
//     *  */
//    public static Element select(Page page , String cssSelector, int index) {
//        Elements eles = select(page , cssSelector);
//        int realIndex = index;
//        if (index < 0) {
//            realIndex = eles.size() + index;
//        }
//        return eles.get(realIndex);
//    }
//
//
//    /**
//     * 获取满足选择器的元素中的链接 选择器cssSelector必须定位到具体的超链接
//     * 例如我们想抽取id为content的div中的所有超链接，这里
//     * 就要将cssSelector定义为div[id=content] a
//     *  放入set 中 防止重复；
//     * @param cssSelector
//     * @return
//     */
//    public static  Set<String> getLinks(Page page ,String cssSelector) {
//        Set<String> links  = new HashSet<String>() ;
//        Elements es = select(page , cssSelector);
//        Iterator iterator  = es.iterator();
//        while(iterator.hasNext()) {
//            Element element = (Element) iterator.next();
//            if ( element.hasAttr("href") ) {
//                links.add(element.attr("abs:href"));
//            }else if( element.hasAttr("src") ){
//                links.add(element.attr("abs:src"));
//            }
//        }
//        return links;
//    }
//
//
//
//    /**
//     * 获取网页中满足指定css选择器的所有元素的指定属性的集合
//     * 例如通过getAttrs("img[src]","abs:src")可获取网页中所有图片的链接
//     * @param cssSelector
//     * @param attrName
//     * @return
//     */
//    public static ArrayList<String> getAttrs(Page page , String cssSelector, String attrName) {
//        ArrayList<String> result = new ArrayList<String>();
//        Elements eles = select(page ,cssSelector);
//        for (Element ele : eles) {
//            if (ele.hasAttr(attrName)) {
//                result.add(ele.attr(attrName));
//            }
//        }
//        return result;
//    }
}