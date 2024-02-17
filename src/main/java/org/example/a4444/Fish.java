package org.example.a4444;

import lombok.*;

import java.util.Random;

@Getter
public class Fish implements Runnable {
    private int id;
    private String gender;
    private int age;
    private int location;
    private boolean isAlive;
    private Aquarium aquarium;
    private Random random = new Random();

    public Fish(int id, String gender, int age, int location, Aquarium aquarium) {
        this.id = id;
        this.gender = gender;
        this.age = age;
        this.location = location;
        this.isAlive = true;
        this.aquarium = aquarium;
    }

    @Override
    public void run() {
        while (isAlive) {
            activity();
        }
        diedFish();
    }

    public void activity() {
        System.out.println("Fish is swimming." + this);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        aquarium.breed(this);
        age--;
        location = random.nextInt(10) + 1;
        if (age == 0) {
            isAlive = false;
        }
    }

    public void diedFish() {
        System.out.println("-------- Fish died." + this);
        aquarium.removeFish(this);
    }

    @Override
    public String toString() {
        return " ID: " + id +
                " Gender: " + gender +
                " Age: " + age +
                " Location: " + location;
    }
}
