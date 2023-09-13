import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) {
        int port = 8080; // Port number for the server to listen on

        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);

            // Accept client connections
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            // Get the input and output streams for communication
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            // ... (previous code)

            // Read operation code from the client
            byte[] operationBuffer = new byte[4];
            int bytesRead = inputStream.read(operationBuffer);
            int operation = ByteBuffer.wrap(operationBuffer).getInt();

            // Read operand(s) from the client
            double operand1 = 0.0;
            double operand2 = 0.0;

            if (operation >= 1 && operation <= 4) {
                // Read two operands for addition, subtraction, multiplication, division
                byte[] operandBuffer1 = new byte[8];
                byte[] operandBuffer2 = new byte[8];
                bytesRead = inputStream.read(operandBuffer1);
                operand1 = ByteBuffer.wrap(operandBuffer1).getDouble();
                bytesRead = inputStream.read(operandBuffer2);
                operand2 = ByteBuffer.wrap(operandBuffer2).getDouble();
            } else if (operation == 5 || operation == 6) {
                // Read a single operand for square and square root
                byte[] operandBuffer = new byte[8];
                bytesRead = inputStream.read(operandBuffer);
                operand1 = ByteBuffer.wrap(operandBuffer).getDouble();
            }

            // Calculate result based on the operation
            double result = 0.0;
            String operationName = "";

            switch (operation) {
                case 1:
                    result = operand1 + operand2;
                    operationName = "Addition";
                    break;
                case 2:
                    result = operand1 - operand2;
                    operationName = "Subtraction";
                    break;
                case 3:
                    result = operand1 * operand2;
                    operationName = "Multiplication";
                    break;
                case 4:
                    if (operand2 != 0) {
                        result = operand1 / operand2;
                        operationName = "Division";
                    } else {
                        operationName = "Division by zero is not allowed.";
                    }
                    break;
                case 5:
                    result = Math.sqrt(operand1);
                    operationName = "Square Root";
                    break;
                case 6:
                    result = operand1 * operand1;
                    operationName = "Square";
                    break;
                default:
                    operationName = "Invalid operation.";
                    break;
            }

            // Send the result back to the client
            String serverResponse = operationName + ": " + result;
            outputStream.write(serverResponse.getBytes(StandardCharsets.UTF_8));
            // Print the input integers received by the client
            System.out.println("Input integers received by the client:");
            System.out.println("Operand 1: " + operand1);
            System.out.println("Operand 2: " + operand2);
            outputStream.flush();

            // Close the connections
            inputStream.close();
            outputStream.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
