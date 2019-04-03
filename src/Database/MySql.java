package Database;

import java.sql.*;
import java.util.*;

public class MySql {
	private Connection con=null;

	public MySql() {
		// TODO Auto-generated constructor stub
	}

	public Connection getConnection()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("数据库驱动加载成功");
		}catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306","crawler","crawler301");
			System.out.println("数据库连接成功");
		}catch(SQLException e)
		{
			e.printStackTrace();
		}		
		return con;
	}
	
	public void InsertUrls(String[] url,String tableName)
	{
		if(this.con==null)
		{
			System.out.println("数据库未连接");
			return;
		}
		try {
			PreparedStatement sql=con.prepareStatement("INSERT INTO "+"crawlerdata."+tableName+"(url)"+" values(?)");
			for(int i=0;i<url.length;i++)
			{
				sql.setString(1,url[i]);
				System.out.println("NO."+i+":"+url[i]);
				sql.executeUpdate();
			}
			System.out.println("成功在"+tableName+"插入"+url.length+"条记录");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String[] GetUrls(String tableName)
	{
		if(this.con==null)
		{
			System.out.println("数据库未连接");
			return new String[0];
		}
		try {
			Statement sql=con.createStatement();
			ResultSet res=sql.executeQuery("select * from " +"crawlerdata."+tableName);
			ArrayList<String> urls=new ArrayList<String>();
			while(res.next())
			{
				urls.add(res.getString("url"));
			}
			System.out.println("成功在"+tableName+"读取"+urls.size()+"条记录");
			return urls.toArray(new String[0]);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String[0];
	}
	
	
	public void ClearTable(String tableName)
	{
		if(this.con==null)
		{
			System.out.println("数据库未连接");
			return;
		}
		try
		{
			Statement sql=con.createStatement();
			sql.executeUpdate("delete from "+"crawlerdata."+tableName);
			System.out.println("成功把"+"crawlerdata."+tableName+"清空");
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void CloseConnection()
	{
		if(this.con==null)
		{
			System.out.println("数据库未连接");
			return;
		}
		try
		{
			con.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MySql db=new MySql();
		try {
		Connection c=db.getConnection();
		Statement sql=c.createStatement();
		//sql.execute("");
		c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
