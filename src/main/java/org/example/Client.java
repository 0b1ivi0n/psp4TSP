package org.example;


import org.json.JSONArray;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) {

        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            socket = new Socket("localhost", 1234);

            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            Scanner scanner = new Scanner(System.in);

            while(true) {

//                String msgToSend = scanner.nextLine();
//                bufferedWriter.write(msgToSend);
//                bufferedWriter.newLine();
                //               bufferedWriter.flush();

                float[][] array = createArray(scanner);

                // Сериализуем массив в строку в формате JSON
                String json = new JSONArray(array).toString();

                bufferedWriter.write(json);
                bufferedWriter.flush();

                System.out.println("Отправлено: " + json);

                System.out.println("Server: определитель равен " + bufferedReader.readLine());

                System.out.println("Если хотите завершить работу, введите bye");

                String str;

                String msgToSend = scanner.nextLine();

                bufferedWriter.write(msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                if(msgToSend.equalsIgnoreCase("BYE"))
                    break;
//                System.out.println("Server: " + bufferedReader.readLine());
//
//                if(msgToSend.equalsIgnoreCase("BYE"))
//                    break;
            }
        }  catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (socket != null)
                    socket.close();
                if (inputStreamReader != null)
                    inputStreamReader.close();
                if (outputStreamWriter != null)
                    outputStreamWriter.close();
                if (bufferedReader != null)
                    bufferedReader.close();
                if (bufferedWriter != null)
                    bufferedWriter.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static float[][] createArray(Scanner scanner) {
        // Scanner scanner = new Scanner(System.in);

        System.out.println("Введите: размер матрицы ");
        int n = scanner.nextInt();
        scanner.nextLine();

        float[][] array = new float[n][n];

        System.out.println("Введите элементы массива построчно:");
        for (int i = 0; i < n; i++) {
            System.out.println("Стока № " + (i + 1));
            for (int j = 0; j < n; j++) {
                array[i][j] = scanner.nextFloat();
                scanner.nextLine();
            }
            System.out.println();
        }

        // scanner.close();

        System.out.println("Ваш массив:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(array[i][j] + "\t");
            }
            System.out.println();
        }

        return array;
    }

}