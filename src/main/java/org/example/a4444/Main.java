package org.example.a4444;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        int capacity = random.nextInt(10) + 1;
        Aquarium aquarium = new Aquarium(capacity);

        // adding fishes
        int fishesNum = Math.min(random.nextInt(10) + 1, capacity);
        for (int i = 0; i < fishesNum; i++) {
            int id = i + 1;
            String gender = random.nextBoolean() ? "Male" : "Female";
            int age = random.nextInt(10) + 1;
            int location = random.nextInt(10) + 1;
            aquarium.addFish(new Fish(id, gender, age, location, aquarium));
        }


        System.out.println("Aquarium size: " + aquarium.capacity);
        System.out.println("Fishes num: " + aquarium.fishList.size());

        while (!aquarium.fishList.isEmpty()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
