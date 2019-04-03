package Page;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.charset.Charset;

public class RequestAndResponseTool { 

    /*
         * 读取url连接，获得页面数据，采用默认解码方式
     */
    public static OnePage  sendRequstAndGetResponseBasic(String url) {
        OnePage page = new OnePage(url);
        InputStream in;
        BufferedReader bfr;
        try {
            URL Url=new URL(url);
            HttpURLConnection connection= (HttpURLConnection)Url.openConnection();
            connection.setConnectTimeout(3000);//设置连接超时选项为3s
            connection.setRequestProperty("User-Agent", "Mozilla/31.0 (compatible; MSIE 10.0; Windows NT; DigExt)"); //防止报403错误。
            if(connection.getResponseCode()!=200)
            {
            	System.out.println(url+":"+"获取不到网页的源码，服务器响应代码为："+connection.getResponseCode());
            	return null;
            }
            in=connection.getInputStream();
            bfr=new BufferedReader(new InputStreamReader(in));
            int count=0;
            while(true)
            {              
               String line= bfr.readLine();
               if(line==null)
               {
            	   count++;
               }
               else
               {
            	   count=0;
                   page.AppendContent(line);
                   page.AppendContent("\n");
               }
               if (count>10)
            	   break; //出现10行空行则停止
            }

        }catch(Exception e)
        {
        	System.out.println("Bad:"+url);
        	e.printStackTrace();
        }
        
        return page;
    }
    
    
    /*
     * 读取url连接，获得页面数据，采用指定的解码方式
   */
    public static OnePage  sendRequstAndGetResponse(String url) {
        OnePage page = new OnePage(url);
        InputStream in;
        DataInputStream DataIn;
        int bufflen=1024*1024;
        byte[] buff=new byte[bufflen];
        try {
            URL Url=new URL(url);
            if(Url.getProtocol().matches("http")==false&&Url.getProtocol().matches("https")==false)
            {
            	System.out.println(url+":"+"网页协议错误");
            	return null;
            }
            HttpURLConnection connection= (HttpURLConnection)Url.openConnection();
            connection.setConnectTimeout(3000);//设置连接超时选项为3s
            connection.setRequestProperty("User-Agent", "Mozilla/31.0 (compatible; MSIE 10.0; Windows NT; DigExt)"); //防止报403错误。
            if(connection.getResponseCode()!=200)
            {
            	System.out.println(url+":"+"获取不到网页的源码，服务器响应代码为："+connection.getResponseCode());
            	return null;
            }
            in=connection.getInputStream();
            DataIn=new DataInputStream(in);
            while(true)
            {              
               int len=DataIn.read(buff,0, bufflen);
               page.AppendContentByData(buff,len);
               if(0>len)
            	   break;
            }

        }catch(Exception e)
        {
        	System.out.println("Bad:"+url);
        	e.printStackTrace();
        }
        
        return page;
    }
    
    
    
    
    
    
    
    
    public static void DownloadPic(String urlList,String dir,String type)
    {
    	imageDownload.downloadPicture(urlList,dir,type);
    }
    
    
}

class imageDownload{
	
	 //链接url下载图片
     public static void downloadPicture(String urlLink,String dir,String type) {
        URL url = null;
        try {
            url = new URL(urlLink);
            if(url.getProtocol().matches("http")==false&&url.getProtocol().matches("https")==false)
            {
            	System.out.println(urlLink+":"+"图片协议错误");
            	return ;
            }
            HttpURLConnection connection= (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(3000);//设置连接超时选项为3s
            String referer = url.getProtocol()+"://"+url.getHost();
            connection.setRequestProperty("Referer", referer);
            connection.setRequestProperty("User-Agent", "Mozilla/31.0 (compatible; MSIE 10.0; Windows NT; DigExt)"); //防止报403错误。
            if(connection.getResponseCode()!=200)
            {
            	System.out.println(urlLink+":"+"获取不到图片的源码，服务器响应代码为："+connection.getResponseCode());
            	return ;
            }
            InputStream in=connection.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(in);
            int startindex=urlLink.lastIndexOf('/');
            int endIndex=urlLink.indexOf(type, startindex);
            String imageName =urlLink.substring(startindex,endIndex)+"."+type;
            imageName=dir+"Pic\\"+imageName.replaceAll("[\\?/:*|<>\"\n\r]", "X");
            FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }  
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
            in.close();
        } catch (MalformedURLException e) {
        	System.out.println("Bad:"+url);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
	
}