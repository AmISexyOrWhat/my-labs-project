package com.Vladislav.labs;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) {
        String inputFile = "characters.csv";
        String outputFile = "top-5-newest.csv";

        CharacterRepository repository = new CharacterRepository(inputFile);

        System.out.println("--- ОСНОВНОЕ ЗАДАНИЕ ---");

        List<Character> allCharacters = repository.findAll();
        System.out.println("Успешно загружено персонажей: " + allCharacters.size());

        if (allCharacters.isEmpty()) {
            System.out.println("Файл пуст или не найден.");
            return;
        }

        PriorityQueue<Character> pq = new PriorityQueue<>(
                Comparator.comparing(Character::getCreated).reversed()
        );

        pq.addAll(allCharacters); // Закидываем всех в очередь

        List<Character> top5 = new ArrayList<>();
        for (int i = 0; i < 5 && !pq.isEmpty(); i++) {
            top5.add(pq.poll()); // Достаем 5 самых новых
        }

        System.out.println("\nТоп-5 самых новых персонажей:");
        for (Character c : top5) {
            System.out.println(c);
        }

        repository.writeTop5ToFile(top5, outputFile);
        System.out.println("Результат сохранен в файл: " + outputFile);


        System.out.println("\n--- ДЕМОНСТРАЦИЯ CRUD ---");

        System.out.println("Создаем нового персонажа...");
        Character newChar = new Character(null, "New Character", OffsetDateTime.now());
        repository.save(newChar);

        System.out.println("Обновляем имя персонажа с id=1 на 'Rick Sanchez (Updated)'...");
        repository.update(1L, "Rick Sanchez (Updated)");

        System.out.println("Удаляем персонажа с id=2...");
        repository.delete(2L);

        System.out.println("\nСписок первых 3-х персонажей после изменений:");
        List<Character> afterCrud = repository.findAll();
        for (int i = 0; i < 3 && i < afterCrud.size(); i++) {
            System.out.println(afterCrud.get(i));
        }
        System.out.println("Всего персонажей в файле после CRUD: " + afterCrud.size());
        System.out.println("\nПроверь файл characters.csv! Остальные колонки (species, status и т.д.) были очищены, так как наша модель хранит только 3 поля.");
    }
}