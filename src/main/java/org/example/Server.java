package org.example;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONArray;

public class Server {
    public Server() {
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        serverSocket = new ServerSocket(1234);

        while(true) {
            while(true) {
                try {
                    socket = serverSocket.accept();
                    System.out.println("Клиент подключился");
                    inputStreamReader = new InputStreamReader(socket.getInputStream());
                    outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                    bufferedReader = new BufferedReader(inputStreamReader);
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    String msgFromClient = "asd";
                    while(!msgFromClient.equalsIgnoreCase("BYE")) {
                        char[] buffer = new char[1024];
                        int length = bufferedReader.read(buffer);
                        String json = new String(buffer, 0, length);
                        System.out.println("Получено от кличента: " + json);
                        JSONArray jsonArray = new JSONArray(json);
                        float[][] array = new float[jsonArray.length()][];

                        for(int i = 0; i < jsonArray.length(); ++i) {
                            JSONArray row = jsonArray.getJSONArray(i);
                            array[i] = new float[row.length()];

                            for(int j = 0; j < row.length(); ++j) {
                                array[i][j] = (float)row.getInt(j);
                            }
                        }

                        String msgToSend = "" + determinant(array);
                        System.out.println("Отправлено клиенту: " + msgToSend);
                        bufferedWriter.write(msgToSend);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        msgFromClient = bufferedReader.readLine();
                    }

                    System.out.println("Клиент отключился");
                    socket.close();
                    inputStreamReader.close();
                    outputStreamWriter.close();
                    bufferedWriter.close();
                    bufferedReader.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }
            }
        }
    }

    public static float determinant(float[][] matrix) {
        int n = matrix.length;
        if (n != 0 && n == matrix[0].length) {
            if (n == 1) {
                return matrix[0][0];
            } else if (n == 2) {
                return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
            } else {
                float result = 0.0F;

                for(int i = 0; i < n; ++i) {
                    float coefficient = (float)((double)matrix[0][i] * Math.pow(-1.0, (double)i));
                    float[][] submatrix = new float[n - 1][n - 1];

                    for(int j = 1; j < n; ++j) {
                        for(int k = 0; k < n; ++k) {
                            if (k < i) {
                                submatrix[j - 1][k] = matrix[j][k];
                            } else if (k > i) {
                                submatrix[j - 1][k - 1] = matrix[j][k];
                            }
                        }
                    }

                    result += coefficient * determinant(submatrix);
                }

                return result;
            }
        } else {
            return 0.0F;
        }
    }
}