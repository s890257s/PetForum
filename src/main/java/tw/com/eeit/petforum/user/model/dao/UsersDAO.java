package tw.com.eeit.petforum.user.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import tw.com.eeit.petforum.user.model.bean.Users;

/**
 * 只要是跟Users資料表有關的任何互動，都要寫在此DAO之中。 DAO內的所有方法都拋出錯誤，交給Service處理。
 */
public class UsersDAO {

	private Connection conn;

	/**
	 * 沒有「無參數的建構子(無參建購子)」，代表使用new建立此物件時，一定要傳入參數conn。
	 * 
	 * @param conn 外部傳入的連線物件，DAO不實作連線
	 */
	public UsersDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 根據使用者的ID取的使用者的所有資料。
	 * <p>
	 * 
	 * @param userID 使用者的ID
	 * @return Users 使用者的java bean，裡面有使用者的所有資料； 若ID不存在則回傳null。
	 */
	public Users getUsersByID(String userID) throws SQLException {
		String SQL = "SELECT * FROM Users WHERE id =?";
		PreparedStatement preState = conn.prepareStatement(SQL);
		preState.setString(1, userID);
		ResultSet rs = preState.executeQuery();

		if (rs.next()) {
			Users u = new Users();
			u.setId(rs.getString("id"));
			u.setAccount(rs.getString("account"));
			u.setPassword(rs.getString("password"));
			u.setPhoto(rs.getString("photo"));
			u.setLevel(rs.getString("level"));
			return u;
		}

		preState.close();
		return null;
	}

}
