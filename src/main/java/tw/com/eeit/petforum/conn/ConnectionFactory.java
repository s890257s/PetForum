package tw.com.eeit.petforum.conn;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import tw.com.eeit.petforum.user.model.dao.UsersDAO;

/**
 * 產生連線的工廠。
 */
public class ConnectionFactory {

	/**
	 * 使用JNDI尋找DataSource，取得連線物件後回傳
	 * 
	 * @return Connection 連線物件
	 */
	public static Connection getConnection() throws Exception {
		InitialContext context = new InitialContext();
		DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/MSSQL");
		Connection conn = ds.getConnection();
		
		return conn;
	}

}
