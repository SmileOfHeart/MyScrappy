package Main;

/*
 * 
 * 文章主要参考： 1： 自己动手写网络爬虫；
 *2： https://github.com/CrawlScript/WebCollector  
 * WebCollector是一个无须配置、便于二次开发的JAVA爬虫框架（内核），它提供精简的的API，
 * 只需少量代码即可实现一个功能强大的爬虫。WebCollector-Hadoop是WebCollector的Hadoop版本，
 * 支持分布式爬取。
 */



import Page.*;
import Link.*;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.Charsets;

public class MyCrawler {

	public static String HomeDir= "E:\\eclipse-workspace\\CrawlDownLoad\\";
	public static int MaxUrlNum=500;   //最多爬取url数量
	public MyCrawler(String startUrl,String dirHome) {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		CrawlerNewBeginning();
		CrawlerContinue();
	}
	
	public static void CrawlerNewBeginning()
	{
		// TODO Auto-generated method stub
		System.out.println("爬取开始");
		String startURL="https://www.liaoxuefeng.com/";
		OnePage page=RequestAndResponseTool.sendRequstAndGetResponse(startURL);
		//page.DecodePage(Charsets.UTF_8);
		page.AutoDecodePage();
		page.SavePage(HomeDir);
		List<String> urlList=PageParserTool.ParsePage(page.getHtml(),HomeDir);
		for(int i=0;i<urlList.size();i++)
			PageLinks.addUnvisitedUrlQueue(urlList.get(i));
		while(!PageLinks.unVisitedUrlQueueIsEmpty()&&PageLinks.getVisitedUrlNum()<MaxUrlNum)
		{
			String url=PageLinks.removeHeadOfUnVisitedUrlQueue();
			System.out.println(PageLinks.getVisitedUrlNum()+":"+url);
			PageLinks.addVisitedUrlSet(url);
			page=RequestAndResponseTool.sendRequstAndGetResponse(url);
			if(page==null)
				continue;
			page.DecodePage(Charsets.UTF_8);
			page.SavePage(HomeDir);
			//解析获得页面子链接
			urlList=PageParserTool.ParsePage(page.getHtml(),HomeDir);
			for(int i=0;i<urlList.size();i++)
				PageLinks.addUnvisitedUrlQueue(urlList.get(i));
		}		
		System.out.println("爬取结束");
		//保存爬取断点
		PageLinks.SaveNotVisitedUrlSet();
	}
	
	public static void CrawlerContinue()
	{
		// TODO Auto-generated method stub
		OnePage page=null;
		List<String> urlList=null;
		PageLinks.LoadNotVisitedUrlSet();
		while(!PageLinks.unVisitedUrlQueueIsEmpty()&&PageLinks.getVisitedUrlNum()<MaxUrlNum)
		{
			String url=PageLinks.removeHeadOfUnVisitedUrlQueue();
			System.out.println(PageLinks.getVisitedUrlNum()+":"+url);
			PageLinks.addVisitedUrlSet(url);
			page=RequestAndResponseTool.sendRequstAndGetResponse(url);
			if(page==null)
				continue;
			page.DecodePage(Charsets.UTF_8);
			page.SavePage(HomeDir);
			//解析获得页面子链接
			urlList=PageParserTool.ParsePage(page.getHtml(),HomeDir);
			for(int i=0;i<urlList.size();i++)
				PageLinks.addUnvisitedUrlQueue(urlList.get(i));
		}		
		System.out.println("爬取结束");
		//保存爬取断点
		PageLinks.SaveNotVisitedUrlSet();
	}

}
