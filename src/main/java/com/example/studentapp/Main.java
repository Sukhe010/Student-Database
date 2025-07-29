package com.example.studentapp;

import com.example.studentapp.db.DBConnection;
import com.example.studentapp.model.Student;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Add Student\n2. View All\n3. Update\n4. Delete\n5. Exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> updateStudent();
                case 4 -> deleteStudent();
                case 5 -> System.exit(0);
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void addStudent() {
        System.out.print("Enter ID, Name, Age, Grade: ");
        int id = sc.nextInt();
        String name = sc.next();
        int age = sc.nextInt();
        String grade = sc.nextLine();

        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO students (id, name, age, grade) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.setString(4, grade); // new
            ps.executeUpdate();
            System.out.println("Student added!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewAllStudents() {
        try (Connection con = DBConnection.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM students");
            while (rs.next()) {
                System.out.println(new Student(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void updateStudent() {
        System.out.print("Enter ID to update: ");
        int id = sc.nextInt();
        System.out.print("Enter new Name, Age, and Grade: ");
        String name = sc.next();
        int age = sc.nextInt();
        String grade = sc.nextLine(); // using this "nextLine" it would accept grade along with these symbols: +, - etc.

        try (Connection con = DBConnection.getConnection()) {
            String query = "UPDATE students SET name=?, age=?, grade=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, grade);
            ps.setInt(4, id);  // id is in WHERE clause
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student updated!");
            } else {
                System.out.println("No student found with given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    static void deleteStudent() {
        System.out.print("Enter ID to delete: ");
        int id = sc.nextInt();
        try (Connection con = DBConnection.getConnection()) {
            String query = "DELETE FROM students WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Student deleted!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
