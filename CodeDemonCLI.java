import java.io.*;
import java.util.*;

/**
 * CodeDemonCLI - A command-line interface for managing job descriptions and consultant profiles.
 * It allows users to upload job descriptions and consultant profiles, view matching results,
 * and communicate with consultants.
 */

public class CodeDemonCLI {

    static Scanner sc = new Scanner(System.in);
    static List<Map<String, String>> jobDescriptions = new ArrayList<>();
    static List<Map<String, String>> consultantProfiles = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            showMenu();
            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> handleJobDescription();
                case 2 -> handleConsultantProfile();
                case 3 -> viewResults();
                case 4 -> communicationDashboard();
                case 5 -> {
                    System.out.println("Exiting CodeDemon. Thank you!");
                    return;
                }
                case 6 -> {
                    System.out.println("Consultant profiles count: " + consultantProfiles.size());
                    System.out.println("Job descriptions count: " + jobDescriptions.size());
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    static void showMenu() {
        System.out.println("==================================================");
        System.out.println(" Welcome to CodeDemon");
        System.out.println(" Document Comparison, Ranking & Communication");
        System.out.println("==================================================");
        System.out.println("Please select an option to proceed:");
        System.out.println("1. Upload Job Description (File or Form)");
        System.out.println("2. Upload Consultant Profile (File or Form)");
        System.out.println("3. View Matching Results & Rankings");
        System.out.println("4. Communication Dashboard");
        System.out.println("5. Exit");
        System.out.println("6. SizeCheck");
    }

    static void readFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            System.out.println("\n--- File Content ---");
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("✅ File read successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + path);
        }
    }

    static List<String> splitAndLowercase(String input) {
        List<String> tokens = new ArrayList<>();
        for (String token : input.split(",")) {
            tokens.add(token.trim().toLowerCase());
        }
        return tokens;
    }

    static List<Map<String, String>> parseCSVFile(String path, List<String> headers) {
        List<Map<String, String>> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                Map<String, String> entry = new HashMap<>();
                for (int i = 0; i < headers.size() && i < tokens.length; i++) {
                    entry.put(headers.get(i), tokens[i].trim());
                }
                if (!entry.isEmpty()) {
                    result.add(entry);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + path);
        }

        return result;
    }

    static void handleJobDescription() {
        System.out.println("--------------------------------------------------");
        System.out.println(" Upload Job Description");
        System.out.println("--------------------------------------------------");
        System.out.println("Select input method:\n1. Upload JD File\n2. Submit JD Form");

        int inputChoice = Integer.parseInt(sc.nextLine());

        if (inputChoice == 1) {
            System.out.print("Enter file path for Job Description: ");
            String path = sc.nextLine();
            readFile(path);
            List<String> headers = List.of("title", "skills", "location", "experience", "details");
            jobDescriptions.addAll(parseCSVFile(path, headers));
            System.out.println("✅ Job Descriptions loaded from file successfully!");
        } else if (inputChoice == 2) {
            Map<String, String> jd = new HashMap<>();
            System.out.print("Enter Job Title: ");
            jd.put("title", sc.nextLine());
            System.out.print("Enter Required Skills (comma-separated): ");
            jd.put("skills", sc.nextLine());
            System.out.print("Enter Job Location: ");
            jd.put("location", sc.nextLine());
            System.out.print("Enter Experience Required (in years): ");
            jd.put("experience", sc.nextLine());
            System.out.print("Enter Job Description Details: ");
            jd.put("details", sc.nextLine());

            jobDescriptions.add(jd);
            System.out.println("✅ Job Description form submitted successfully!");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    static void handleConsultantProfile() {
        System.out.println("--------------------------------------------------");
        System.out.println(" Upload Consultant Profile");
        System.out.println("--------------------------------------------------");
        System.out.println("Select input method:\n1. Upload Consultant Resume File\n2. Submit Consultant Details via Form");

        int inputChoice = Integer.parseInt(sc.nextLine());

        if (inputChoice == 1) {
            System.out.print("Enter file path for Consultant Resume: ");
            String path = sc.nextLine();
            readFile(path);
            List<String> headers = List.of("name", "education", "skills", "experience", "location");
            consultantProfiles.addAll(parseCSVFile(path, headers));
            System.out.println("✅ Consultant profiles loaded from file successfully!");
        } else if (inputChoice == 2) {
            Map<String, String> profile = new HashMap<>();
            System.out.print("Enter Full Name: ");
            profile.put("name", sc.nextLine());
            System.out.print("Enter Education Qualification: ");
            profile.put("education", sc.nextLine());
            System.out.print("Enter Skills (comma-separated): ");
            profile.put("skills", sc.nextLine());
            System.out.print("Enter Years of Experience: ");
            profile.put("experience", sc.nextLine());
            System.out.print("Enter Preferred Location: ");
            profile.put("location", sc.nextLine());

            consultantProfiles.add(profile);
            System.out.println("✅ Consultant profile form submitted successfully!");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    static void viewResults() {
        System.out.println("--------------------------------------------------");
        System.out.println(" Matching Results & Ranking Summary");
        System.out.println("--------------------------------------------------");

        for (Map<String, String> profile : consultantProfiles) {
            String name = profile.getOrDefault("name", "Unknown");
            List<String> profileSkills = splitAndLowercase(profile.getOrDefault("skills", ""));
            String matchedJD = "None";
            int maxScore = 0;

            for (Map<String, String> jd : jobDescriptions) {
                String jdTitle = jd.getOrDefault("title", "Untitled");
                List<String> jdSkills = splitAndLowercase(jd.getOrDefault("skills", ""));
                int score = 0;

                for (String skill : profileSkills) {
                    for (String jdSkill : jdSkills) {
                        if (skill.equals(jdSkill)) {
                            score++;
                        }
                    }
                }

                int percentage = jdSkills.size() > 0 ? (score * 100 / jdSkills.size()) : 0;
                if (percentage > maxScore) {
                    maxScore = percentage;
                    matchedJD = jdTitle;
                }
            }

            System.out.println("| " + name + " | " + matchedJD + " | " + maxScore + "% |");
        }

        System.out.println("--------------------------------------------------");
        System.out.println("✅ Ranking Completed.");
    }

    static void communicationDashboard() {
        System.out.println("--------------------------------------------------");
        System.out.println(" Communication Dashboard");
        System.out.println("--------------------------------------------------");

        for (int i = 0; i < consultantProfiles.size(); i++) {
            System.out.println((i + 1) + ". " + consultantProfiles.get(i).get("name"));
        }
        System.out.println((consultantProfiles.size() + 1) + ". Go Back");

        int choice = Integer.parseInt(sc.nextLine());
        if (choice > consultantProfiles.size()) return;

        System.out.print("Enter your message: ");
        String message = sc.nextLine();
        

        System.out.println("✅ Message sent successfully to " + consultantProfiles.get(choice - 1).get("name") + ".");
    }
}