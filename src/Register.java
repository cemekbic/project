import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Register extends JDialog{

    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAdress;
    private JTextField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JTextField pfPassword;
    private JPanel registerPanel;

    public Register(JFrame parent){
        super(parent);
        setTitle("Register Form");
        setContentPane(registerPanel);
        setMaximumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String adress = tfAdress.getText();
        String confirmPassword = pfConfirmPassword.getText();
        String password = pfPassword.getText();


        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || adress.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields", "Try again", JOptionPane.ERROR_MESSAGE);
            return;

        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserToDatabase(name, email, phone, adress, password);
        if(user == null){
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(this, "Failed to register new user", "Try again", JOptionPane.ERROR_MESSAGE);
        }



    }
    public User user;
    private User addUserToDatabase(String name, String email, String phone, String adress, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306";
        final String USERNAME = "root";
        final String PASSWORD = "1234";
        try {
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users (name, email, phone, address, password" + "VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, adress);
            preparedStatement.setString(5, password);

            int addedRows = preparedStatement.executeUpdate();
            if(addedRows > 0){
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = adress;
                user.password = password;

            }
            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return user;

    }

    public static void main(String[] args) {
        Register myform = new Register(null);
        User user = myform.user;
        if (user != null) {
            System.out.println("Successfully registered user" + user.name);
        }
        else{
            System.out.println("Failed to register user");
        }


    }
}
