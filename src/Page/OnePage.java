package Page;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Util.CharsetDetector;

import org.apache.commons.codec.Charsets;
import org.apache.http.util.CharsetUtils;

import java.util.*;
import java.nio.ByteBuffer;
import java.nio.charset.*;

public class OnePage {
	private ArrayList<Byte> Data;
	private StringBuffer content;
    private String html ;  //网页源码字符串
    private String url ;//url路径


    public OnePage(String url){
        this.url = url ;
        this.content=new StringBuffer();
        this.Data=new ArrayList<Byte>();
    }

    public String getUrl(){return url ;}

    /**
     * 返回网页的源码字符串
     *
     * @return 网页的源码字符串
     */
    public String getHtml() {
        if (html != null) {
            return html;
        }
        if (content == null) {
            return null;
        }
        try {
            this.html = content.toString();
            return html;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

	  public void AppendContent(String Line)
	  {
		  content.append(Line);
	  }
	  
	  /*
	     *   添加字符数组
	   */
	  public void AppendContentByData(byte[] HtmlData,int len)
	  {
		  for(int i=0;i<len;i++)
			  this.Data.add(HtmlData[i]);
	  }
	  
	  /*
	   *  直接将content中的内容转换成字符串放入html中
	   */
	  public void DecodePage()
	  {
		  this.html=this.content.toString();
	  }
	  
	  /*
	   * 将Data中的字节数值按照一定的方式解码，放到html中
	   */
	  public void DecodePage(Charset codeStyle)
	  {
		  byte[] ba=new byte[this.Data.size()];
		  for(int i=0;i<this.Data.size();i++)
			  ba[i]=this.Data.get(i);
		  this.html=new String(ba,codeStyle);
	  }
	  
	  
	  public void AutoDecodePage()
	  {
		  byte[] ba=new byte[this.Data.size()];
		  for(int i=0;i<this.Data.size();i++)
			  ba[i]=this.Data.get(i);
		  try {
			  String codeStyle=CharsetDetector.guessEncoding(ba);
			  ByteBuffer bbuff=ByteBuffer.wrap(ba);
			  Charset cs=Charset.forName(codeStyle);
			  CharsetDecoder decoder=cs.newDecoder();
			  this.html= decoder.decode(bbuff).toString();
		  }
		  catch(Exception e)
		  {
			  e.printStackTrace();
		  }
	  }
	  
	  
	  
	  /*
	   * 将Data中的字节数值按照一定的方式解码，放到html中
	   */
	  public void DecodePage(String codeStyle)
	  {
		  try {
			      byte[] ba=new byte[this.Data.size()];
				  for(int i=0;i<this.Data.size();i++)
					  ba[i]=this.Data.get(i);
			      ByteBuffer bbuff=ByteBuffer.wrap(ba);
				  Charset cs=Charset.forName(codeStyle);
				  CharsetDecoder decoder=cs.newDecoder();
				  this.html= decoder.decode(bbuff).toString();
		  }catch(Exception e)
		  {
			  e.printStackTrace();
		  }
	  }
	  
	  public void SavePage(String dir)
	  {
		//写入到html文件中
		   try {
              if(this.html==null)
              {
            	  System.out.println("Page:html为空");
            	  return;
              }
			  //正则表达式带？表示非贪婪匹配
			    //避免堆栈溢出
				Pattern pattern = Pattern.compile("<title>(\\S|\\s)*?</title>");
				Matcher matcher=pattern.matcher(this.html);
				if(matcher.find())
				{
					String temp=matcher.group();
					String fileName=temp.substring(7,temp.length()-8);
					fileName=fileName.replaceAll("[\\?/:*|<>\"\n\r]", "X");
					System.out.println(fileName);
					File file=new File(dir+"HTML\\"+fileName+".html");
					PrintStream out=new PrintStream(new FileOutputStream(file));
					out.print(this.html);
					out.close();
				}
		   }catch(Exception e)
		   {
			   e.printStackTrace();
		   }
	  }
}
