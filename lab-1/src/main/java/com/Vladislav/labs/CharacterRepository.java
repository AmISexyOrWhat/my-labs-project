package com.Vladislav.labs;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class CharacterRepository {
    private final String filePath;

    public CharacterRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Character> findAll() {
        List<Character> characters = new ArrayList<>();
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new RuntimeException("Файл не найден по пути: " + path.toAbsolutePath());
        }

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(",", -1);
                if (fields.length >= 9) {
                    characters.add(new Character(
                            Long.parseLong(fields[0]),
                            fields[1],
                            OffsetDateTime.parse(fields[8])
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Не удалось прочитать файл", e);
        }
        return characters;
    }

    public void writeTop5ToFile(List<Character> top5, String outFilePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outFilePath))) {
            writer.println("id,name,created");
            for (Character c : top5) {
                writer.printf("%d,%s,%s%n", c.getId(), c.getName(), c.getCreated());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Не удалось записать результат в файл", e);
        }
    }

    public void save(Character newCharacter) {
        List<Character> all = findAll();

        if (newCharacter.getId() == null) {
            long maxId = all.stream().mapToLong(Character::getId).max().orElse(0);
            newCharacter = new Character(maxId + 1, newCharacter.getName(), newCharacter.getCreated());
        }

        all.add(newCharacter);
        writeAllToFile(all);
    }

    public void update(Long id, String newName) {
        List<Character> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(id)) {
                Character old = all.get(i);
                all.set(i, new Character(old.getId(), newName, old.getCreated()));
                break;
            }
        }
        writeAllToFile(all);
    }

    public void delete(Long id) {
        List<Character> all = findAll();
        all.removeIf(c -> c.getId().equals(id));
        writeAllToFile(all);
    }

    private void writeAllToFile(List<Character> characters) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("id,name,status,species,type,gender,origin/name,location/name,created");
            for (Character c : characters) {
                writer.printf("%d,%s,,,,,,,%s%n", c.getId(), c.getName(), c.getCreated());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при перезаписи файла: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Не удалось перезаписать файл", e);
        }
    }
}