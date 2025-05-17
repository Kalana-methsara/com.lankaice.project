package com.lankaice.project.model;

import com.lankaice.project.dto.UserDto;
import com.lankaice.project.db.DBConnection;
import com.lankaice.project.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserModel {

    public static UserDto searchUser(String userName, String password) {
        String sql = "SELECT * FROM User WHERE userName = ? AND password = ?";

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new UserDto(
                        resultSet.getString("userName"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("role")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<UserDto> viewAllUsers()throws ClassNotFoundException, SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM User");
        ArrayList<UserDto> users = new ArrayList<>();
        while (resultSet.next()) {
           UserDto user = new UserDto(
                   resultSet.getString("userName"),
                   resultSet.getString("password"),
                   resultSet.getString("name"),
                   resultSet.getString("email"),
                   resultSet.getString("role")
           );
           users.add(user);
        }
        return users;
    }
    public boolean addUser(UserDto user) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO User (userName, password, name, email, role) VALUES (?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public boolean updateUser(UserDto user) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE User SET password = ?, name = ?, email = ?, role = ? WHERE userName = ?";
        return CrudUtil.execute(sql,
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getUsername()
        );
    }

    public boolean deleteUser(String userName) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM User WHERE userName = ?";
        return CrudUtil.execute(sql, userName);
    }
    public boolean isOnlyOneUserExists() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) AS count FROM User";
        ResultSet resultSet = CrudUtil.execute(sql);

        if (resultSet.next()) {
            int count = resultSet.getInt("count");
            return count == 1;
        }
        return false;
    }

}
