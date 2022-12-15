package tw.com.eeit.petforum.init;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tw.com.eeit.petforum.conn.ConnectionFactory;
import tw.com.eeit.petforum.user.model.bean.Pet;
import tw.com.eeit.petforum.user.model.bean.Users;

@WebListener
public class Initialize implements ServletContextListener {

	private String realPath = "";

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		realPath = sce.getServletContext().getRealPath("");

		try (Connection conn = ConnectionFactory.getConnection()) {

			createDB(conn); //建立資料庫
			createUsersTableAndInsertData(conn); //建立Users資料表並加入預設值
			createPetTableAndInsertData(conn); //建立Pet資料表並加入預設值

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createDB(Connection conn) throws SQLException {
		// 如果PetForum資料庫不存在，則建立PetForum資料庫
		String SQL = "IF DB_ID('PetForum') IS NULL CREATE DATABASE PetForum";

		Statement state = conn.createStatement();
		state.execute(SQL);
		state.close();
	}

	private void createUsersTableAndInsertData(Connection conn) throws Exception {
		String SQL = "IF OBJECT_ID('[PetForum].[dbo].[Users]') IS NULL CREATE TABLE [PetForum].[dbo].[Users](id nvarchar(10) PRIMARY KEY NOT NULL, account NVARCHAR(50) NOT NULL, password NVARCHAR(50) NOT NULL, photo NVARCHAR(MAX), level NVARCHAR(10) NOT NULL)";
		Statement state = conn.createStatement();
		state.execute(SQL);
		state.close();

		// 如果users資料表中有任何一筆資料，則終止此方法。
		if (conn.createStatement().executeQuery("SELECT id FROM [PetForum].[dbo].[Users]").next()) {
			return;
		}

		// 使用Gson、JavaIO，讀取webapp/forInit/users.json的資料
		BufferedReader br = new BufferedReader(new FileReader(realPath + "forInit/users.json"));
		List<Users> userList = new Gson().fromJson(br, new TypeToken<List<Users>>() {
		}.getType());
		br.close();

		SQL = "INSERT INTO [PetForum].[dbo].[Users](id, account, password, level, photo) VALUES (?, ?, ?, ?, ?)";

		// 新增資料到資料表
		PreparedStatement preState = conn.prepareStatement(SQL);
		for (Users u : userList) {
			preState.setString(1, u.getId());
			preState.setString(2, u.getAccount());
			preState.setString(3, u.getPassword());
			preState.setString(4, u.getLevel());
			preState.setString(5, u.getPhoto());
			preState.addBatch();
		}
		preState.executeBatch();
		preState.close();
	}

	private void createPetTableAndInsertData(Connection conn) throws Exception {
		// 如果Pet資料表不存在，則建立Pet資料表
		String SQL = "IF OBJECT_ID('[PetForum].[dbo].[Pet]') IS NULL CREATE TABLE [PetForum].[dbo].[Pet](id INT IDENTITY(1,1) PRIMARY KEY NOT NULL, nickName NVARCHAR(50), type NVARCHAR(50) NOT NULL, age INT, photo VARBINARY(MAX), ownerID NVARCHAR(10), FOREIGN KEY (ownerID) REFERENCES [PetForum].[dbo].[Users](id))";
		Statement state = conn.createStatement();
		state.execute(SQL);
		state.close();

		// 如果Pet資料表中有任何一筆資料，則終止此方法。
		if (conn.createStatement().executeQuery("SELECT id FROM [PetForum].[dbo].[Pet]").next()) {
			return;
		}

		// 使用Gson、JavaIO，讀取webapp/forInit/pet.json的資料
		BufferedReader br = new BufferedReader(new FileReader(realPath + "forInit/Pet.json"));
		List<Pet> petList = new Gson().fromJson(br, new TypeToken<List<Pet>>() {
		}.getType());
		br.close();

		// 新增資料到資料表
		SQL = "INSERT INTO [PetForum].[dbo].[Pet](nickName, type, age, photo, ownerID) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement preState = conn.prepareStatement(SQL);
		for (Pet p : petList) {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(realPath + "forInit/petsIMG/" + p.getNickName() + ".jpg"));
			byte[] bytes = bis.readAllBytes();
			preState.setString(1, p.getNickName());
			preState.setString(2, p.getType());
			preState.setInt(3, p.getAge());
			preState.setBytes(4, bytes);
			preState.setString(5, p.getOwnerID());
			preState.addBatch();
			bis.close();
		}
		preState.executeBatch();
		preState.close();
	}
}
