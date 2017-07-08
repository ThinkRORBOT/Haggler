package com.example.ming.haggler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Ming on 26/06/2017.
 */

public class ChangeUserSetting {
    private String uName;
    ChangeUserSetting() {
        uName = LoginActivity.username;
    }

    public void changeReputation(int rep) {
        try {
            BufferedReader file = new BufferedReader(new FileReader("login.txt"));
            String line;
            StringBuffer inputBuffer = new StringBuffer();

            while ((line = file.readLine()) != null) {
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }

            String inputString = inputBuffer.toString();
            file.close();

            inputString = inputString.replace(uName+":"+LoginActivity.pass+""+LoginActivity.reputation, uName+":"+LoginActivity.pass+":"+rep);

            FileOutputStream fileout = new FileOutputStream("Login.txt");
            fileout.write(inputString.getBytes());
            fileout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
