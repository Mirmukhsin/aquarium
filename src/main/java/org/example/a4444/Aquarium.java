package org.example.a4444;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Aquarium {
    Random random = new Random();
    AtomicInteger i = new AtomicInteger(50);
    List<Fish> fishList;
    int newBornFishes;
    int diedFishes;
    int capacity;

    public Aquarium(int capacity) {
        this.fishList = new ArrayList<>();
        this.capacity = capacity;
    }

    public synchronized void addFish(Fish fish) {
        if (fishList.size() < capacity) {
            fishList.add(fish);
            new Thread(fish).start();
        } else {
            System.out.println("Aquarium is full.");
        }
    }

    public synchronized void removeFish(Fish fish) {
        diedFishes++;
        fishList.remove(fish);
        if (fishList.isEmpty()) {
            System.out.println("Aquarium is empty");
            System.out.println("All new born fishes: " + newBornFishes);
            System.out.println("All died fishes: " + diedFishes);
        }
    }

    public synchronized boolean isBreed() {
        int male = 0;
        int female = 0;
        for (Fish fish : fishList) {
            if (fish.getGender().equals("Male")) {
                male++;
            } else {
                female++;
            }
        }
        return male > 0 && female > 0;
    }

    public synchronized void breed(Fish fish) {
        if (isBreed()) {
            Fish mate = findMate(fish);
            if (mate != null) {
                if (fishList.size() < capacity) {
                    Fish babyFish = new Fish(i.getAndIncrement(), getRandomGender(), getRandomNum(), getRandomNum(), this);
                    System.out.println(
                            "New fish born. " + babyFish +
                                    " Parent-1: " + fish +
                                    " Parent-2: " + mate);
                    newBornFishes++;
                    addFish(babyFish);
                } else {
                    System.out.println("The newborn fish was added to another aquarium because there was no room in this aquarium." +
                            " Parent-1: " + fish +
                            " Parent-2: " + mate);
                }
            }
        }
    }

    private Fish findMate(Fish fish) {
        for (Fish potentialMate : fishList) {
            if (!potentialMate.equals(fish) && !potentialMate.getGender().equals(fish.getGender()) && potentialMate.getLocation() == fish.getLocation()) {
                return potentialMate;
            }
        }
        return null;
    }

    private String getRandomGender() {
        return random.nextBoolean() ? "Male" : "Female";
    }

    private int getRandomNum() {
        return random.nextInt(10) + 1;
    }
}
