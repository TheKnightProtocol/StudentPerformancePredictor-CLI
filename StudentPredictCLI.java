import java.io.*;
import java.util.Scanner;

public class StudentPredictCLI {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Student Performance Predictor (CLI) ===");

        try {
            System.out.print("Enter marks (percentage 0-100): ");
            String marks = sc.nextLine().trim();

            System.out.print("Enter attendance (percentage 0-100): ");
            String attendance = sc.nextLine().trim();

            System.out.print("Enter average study hours per day: ");
            String hours = sc.nextLine().trim();

            // Validate simple numeric inputs (basic)
            if (marks.isEmpty() || attendance.isEmpty() || hours.isEmpty()) {
                System.err.println("Error: All fields are required.");
                return;
            }

            String dataArg = marks + "," + attendance + "," + hours;

            // Build command: use 'python' or 'python3' depending on environment
            // Provide the path to script if needed
            String pythonCmd = "python";
            // On some systems you may need "python3"
            // String pythonCmd = "python3";

            ProcessBuilder pb = new ProcessBuilder(pythonCmd, "student_predict.py", dataArg);
            pb.redirectErrorStream(true); // merge stderr into stdout
            Process p = pb.start();

            // Read all output from Python
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            reader.close();

            int exitCode = p.waitFor();
            if (exitCode != 0) {
                System.err.println("Python script returned exit code " + exitCode);
                System.err.println("Output: " + output.toString());
                return;
            }

            String resultJson = output.toString().trim();
            if (resultJson.isEmpty()) {
                System.err.println("No output from Python script.");
                return;
            }

            // Simple JSON parsing (no external libraries)
            // Expecting: {"prediction":"PASS","probability":92.34}
            String prediction = parseJsonValue(resultJson, "prediction");
            String probability = parseJsonValue(resultJson, "probability");

            if (prediction == null) {
                System.out.println("Raw Python output: " + resultJson);
            } else {
                System.out.println("\n=== Prediction Result ===");
                System.out.println("Prediction : " + prediction);
                System.out.println("Confidence : " + probability + "%");
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error while executing Python script: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }

    // Very small helper to extract a top-level JSON value for a key (works for simple JSON)
    private static String parseJsonValue(String json, String key) {
        try {
            String pattern = "\"" + key + "\"";
            int idx = json.indexOf(pattern);
            if (idx == -1) return null;
            int colon = json.indexOf(":", idx);
            if (colon == -1) return null;
            int start = colon + 1;
            // Trim spaces
            while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
            char ch = json.charAt(start);
            if (ch == '\"') {
                // string value
                int end = json.indexOf("\"", start + 1);
                return json.substring(start + 1, end);
            } else {
                // numeric or boolean
                int end = start;
                while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end)=='.')) end++;
                return json.substring(start, end);
            }
        } catch (Exception e) {
            return null;
        }
    }
}
