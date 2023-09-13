import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1"; // IP address of the server
        int serverPort = 8080; // Port number of the server

        try {
            // Create a socket and establish a connection to the server
            Socket socket = new Socket(serverIP, serverPort);
            System.out.println("Connected to server: " + socket.getInetAddress());

            // Get the input and output streams for communication
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            // Get user input for operation
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose operation:");
            System.out.println("1. Addition");
            System.out.println("2. Subtraction");
            System.out.println("3. Multiplication");
            System.out.println("4. Division");
            System.out.println("5. Square Root");
            System.out.println("6. Square");
            int operation = scanner.nextInt();

            // Send the selected operation to the server
            byte[] operationBuffer = ByteBuffer.allocate(4).putInt(operation).array();
            outputStream.write(operationBuffer);
            outputStream.flush();

            // Get user input for operand(s)
            double operand1 = 0.0;
            double operand2 = 0.0;

            if (operation >= 1 && operation <= 4) {
                System.out.print("Enter first operand: ");
                operand1 = scanner.nextDouble();
                System.out.print("Enter second operand: ");
                operand2 = scanner.nextDouble();

                // Send the operands to the server
                byte[] operandBuffer1 = ByteBuffer.allocate(8).putDouble(operand1).array();
                byte[] operandBuffer2 = ByteBuffer.allocate(8).putDouble(operand2).array();
                outputStream.write(operandBuffer1);
                outputStream.flush();
                outputStream.write(operandBuffer2);
                outputStream.flush();
            } else if (operation == 5 || operation == 6) {
                System.out.print("Enter operand: ");
                operand1 = scanner.nextDouble();

                // Send the operand to the server
                byte[] operandBuffer = ByteBuffer.allocate(8).putDouble(operand1).array();
                outputStream.write(operandBuffer);
                outputStream.flush();
            }

            // Receive the server's response
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            String serverResponse = new String(buffer, 0, bytesRead);
            System.out.println("Received from server: " + serverResponse);

            // Close the connection
            inputStream.close();
            outputStream.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
