import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Animal {
    String name;
    String species;
    String birthDate;

    public Animal(String name, String species, String birthDate) {
        this.name = name;
        this.species = species;
        this.birthDate = birthDate;
    }
}

class DomesticAnimal extends Animal {
    List<String> commands;

    public DomesticAnimal(String name, String species, String birthDate) {
        super(name, species, birthDate);
        this.commands = new ArrayList<>();
    }

    public void addCommand(String command) {
        commands.add(command);
    }

    public void showCommands() {
        System.out.println("Список команд для " + name + ":");
        for (String command : commands) {
            System.out.println("- " + command);
        }
    }
}

class LivestockAnimal extends Animal {
    List<String> commands;

    public LivestockAnimal(String name, String species, String birthDate) {
        super(name, species, birthDate);
        this.commands = new ArrayList<>();
    }

    public void addCommand(String command) {
        commands.add(command);
    }

    public void showCommands() {
        System.out.println("Список команд для " + name + ":");
        for (String command : commands) {
            System.out.println("- " + command);
        }
    }
}

class AnimalRegistry {
    List<Animal> animals;

    public AnimalRegistry() {
        this.animals = new ArrayList<>();
    }

    public void addAnimal(Animal animal) {
        this.animals.add(animal);
    }
}

@SpringBootApplication
public class Main {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    public static void createFiles() throws IOException {
        FileWriter domesticFile = new FileWriter("Домашние_животные.txt");
        domesticFile.write("Собаки\nКошки\nХомяки");
        domesticFile.close();

        FileWriter livestockFile = new FileWriter("Вьючные_животные.txt");
        livestockFile.write("Лошади\nОслы\nВерблюды");
        livestockFile.close();
    }

    public static void mergeFiles() throws IOException {
        BufferedReader domesticReader = new BufferedReader(new FileReader("Домашние_животные.txt"));
        BufferedReader livestockReader = new BufferedReader(new FileReader("Вьючные_животные.txt"));
        BufferedWriter mergedWriter = new BufferedWriter(new FileWriter("Друзья_человека.txt"));

        String line;
        while ((line = domesticReader.readLine()) != null) {
            mergedWriter.write(line + "\n");
        }

        while ((line = livestockReader.readLine()) != null) {
            mergedWriter.write(line + "\n");
        }

        domesticReader.close();
        livestockReader.close();
        mergedWriter.close();
    }

    public static void createDirectory() {
        File directory = new File("animals");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File fileToMove = new File("Друзья_человека.txt");
        if (fileToMove.exists()) {
            fileToMove.renameTo(new File("animals/" + fileToMove.getName()));
        }
    }

    public static void addMySQLRepository() throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder("sudo", "apt-get", "install", "mysql-server");
        Process process = builder.start();
        process.waitFor();
    }

    public static void installMySQLPackage(String package_name) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder("sudo", "apt-get", "install", package_name);
        Process process = builder.start();
        process.waitFor();
    }

    public static void showCommandHistory() throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder("history");
        Process process = builder.start();
        process.waitFor();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(Main.class, args);

        createFiles();
        mergeFiles();
        createDirectory();
        addMySQLRepository();
        installMySQLPackage("mysql-client");
        showCommandHistory();

        Scanner scanner = new Scanner(System.in);

        DomesticAnimal dog = new DomesticAnimal("Собака", "Собаки", "01.01.2019");
        dog.addCommand("Сидеть");
        dog.addCommand("Лежать");
        dog.addCommand("Фас");
        dog.addCommand("Дай лапу");

        LivestockAnimal horse = new LivestockAnimal("Лошадь", "Лошади", "01.01.2015");
        horse.addCommand("Идти");
        horse.addCommand("Стой");
        horse.addCommand("Галопом");

        AnimalRegistry registry = new AnimalRegistry();
        registry.addAnimal(dog);
        registry.addAnimal(horse);

        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Завести новое животное");
            System.out.println("2. Показать список команд для животного");
            System.out.println("3. Добавить новую команду для животного");
            System.out.println("4. Выйти из программы");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Чтение символа новой строки после ввода числа

            switch (choice) {
                case 1:
                    System.out.print("Введите имя нового животного: ");
                    String animalName = scanner.nextLine();
                    System.out.print("Введите вид животного (домашнее или вьючное): ");
                    String animalSpecies = scanner.nextLine();
                    System.out.print("Введите дату рождения животного (формат ДД.ММ.ГГГГ): ");
                    String birthDate = scanner.nextLine();
                    if (animalSpecies.equalsIgnoreCase("домашнее")) {
                        DomesticAnimal newDomesticAnimal = new DomesticAnimal(animalName, "Домашнее", birthDate);
                        registry.addAnimal(newDomesticAnimal);
                    } else if (animalSpecies.equalsIgnoreCase("вьючное")) {
                        LivestockAnimal newLivestockAnimal = new LivestockAnimal(animalName, "Вьючное", birthDate);
                        registry.addAnimal(newLivestockAnimal);
                    }
                    break;
                case 2:
                    System.out.print("Введите имя животного: ");
                    String animalNameToShow = scanner.nextLine();
                    for (Animal animal : registry.animals) {
                        if (animal.name.equalsIgnoreCase(animalNameToShow)) {
                            if (animal instanceof DomesticAnimal) {
                                ((DomesticAnimal) animal).showCommands();
                            } else if (animal instanceof LivestockAnimal) {
                                ((LivestockAnimal) animal).showCommands();
                            }
