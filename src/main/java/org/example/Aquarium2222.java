package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Aquarium2222 {
    private static int deadFishes = 0;
    private static int newbornFishes = 0;
    private static final int AQUARIUM_CAPACITY = new Random().nextInt(20) + 1;
    private static final List<Fish> breedingOccurred = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Aquarium size: " + AQUARIUM_CAPACITY);
        List<Fish> aquarium = generateAquarium();
        System.out.println("Fishes number: " + aquarium.size());
        AtomicInteger finalCurrentAquariumSize = new AtomicInteger(aquarium.size());
        lifeInAquarium(aquarium, finalCurrentAquariumSize);
    }

    private static void lifeInAquarium(List<Fish> aquarium, AtomicInteger finalCurrentAquariumSize) {
        AtomicInteger i = new AtomicInteger();
        Random random = new Random();
        while (!aquarium.isEmpty() && aquarium.size() <= AQUARIUM_CAPACITY) {
            List<Fish> deadFishList = new ArrayList<>();
            List<Fish> newbornFishList = new ArrayList<>();
            List<Thread> threads = new ArrayList<>();

            //fish activity
            for (Fish fish : aquarium) {
                Thread fishThread = new Thread(() -> {
                    while (fish.getLifespan() > 0) {
                        System.out.println(
                                "Fish is swimming." +
                                        " FishID: " + fish.getId() +
                                        " Gender: " + fish.getGender() +
                                        " Lifespan: " + fish.getLifespan() +
                                        " Location: " + fish.getLocation());

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        //fish breeding
                        synchronized (aquarium) {
                            for (Fish anotherFish : aquarium) {
                                if (!fish.getGender().equals(anotherFish.getGender()) && fish.getLocation() == anotherFish.getLocation()) {
                                    if (!hasBreedingOccurred(fish, anotherFish)) {
                                        if (finalCurrentAquariumSize.get() < AQUARIUM_CAPACITY) {
                                            String babyFishGender = random.nextBoolean() ? "Male" : "Female";
                                            int babyFishLifespan = random.nextInt(10) + 1;
                                            int babyFishLocation = random.nextInt(10) + 1;
                                            System.out.println(
                                                    "New fish born." +
                                                            " FishID: " + i.incrementAndGet() + "- babyFish." +
                                                            " Gender: " + babyFishGender +
                                                            " Lifespan: " + babyFishLifespan +
                                                            " Location: " + babyFishLocation +
                                                            "   Parents fishes:" +
                                                            " FishID: " + fish.getId() +
                                                            " Gender: " + fish.getGender() +
                                                            " Lifespan: " + fish.getLifespan() +
                                                            " Location: " + fish.getLocation() + " <==>" +
                                                            " FishID: " + anotherFish.getId() +
                                                            " Gender: " + anotherFish.getGender() +
                                                            " Lifespan: " + anotherFish.getLifespan() +
                                                            " Location: " + anotherFish.getLocation());
                                            newbornFishList.add(new Fish(i + "- babyFish", babyFishGender, babyFishLifespan, babyFishLocation));
                                            newbornFishes++;
                                            finalCurrentAquariumSize.incrementAndGet();
                                            markBreedingOccurred(fish, anotherFish);
                                        } else {
                                            System.out.println(
                                                    "><><><><><><><><><><><><><><><><><><><><><" +
                                                            " The aquarium is full, so no new fish can be added." +
                                                            " ><><><><><><><><><><><><><><><><><><><><><");
                                        }
                                    }
                                }
                            }
                        }

                        //reduction of fish life
                        fish.setLifespan(fish.getLifespan() - 1);

                        //change of fish location
                        if (fish.getLifespan() > 0) {
                            fish.setLocation(random.nextInt(50) + 1);
                        }
                    }

                    //death of the fish
                    if (fish.getLifespan() == 0) {
                        System.out.println(
                                "Fish died." +
                                        " FishID: " + fish.getId() +
                                        " Gender: " + fish.getGender() +
                                        " --------------------------------");
                        deadFishList.add(fish);
                        deadFishes++;
                        finalCurrentAquariumSize.decrementAndGet();
                    }
                });
                threads.add(fishThread);
                fishThread.start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            aquarium.removeAll(deadFishList);
            aquarium.addAll(newbornFishList);
            newbornFishList.clear();
            deadFishList.clear();
        }
        if (aquarium.isEmpty()) {
            System.out.println("Aquarium is empty!");
            System.out.println("Total number of newborn fish: " + newbornFishes);
            System.out.println("Total Number of Dead Fish: " + deadFishes);
        }
    }

    //Adding fish to the aquarium
    private static List<Fish> generateAquarium() {
        List<Fish> aquarium = new ArrayList<>();
        Random random = new Random();
        int randomNum = random.nextInt(20) + 1;
        int fishesNumber = Math.min(randomNum, AQUARIUM_CAPACITY);
        for (int i = 0; i < fishesNumber; i++) {
            aquarium.add(new Fish(String.valueOf(i + 1), random.nextBoolean() ? "Male" : "Female", random.nextInt(10) + 1, random.nextInt(50) + 1));
        }
        return aquarium;
    }


    // bu 2 method ma'lum bir baliq bir vaqtning uzida faqat bir marta ko'payishini nazorat qilish uchun;
    private static boolean hasBreedingOccurred(Fish fish1, Fish fish2) {
        return breedingOccurred.contains(fish1) || breedingOccurred.contains(fish2);
    }

    private static void markBreedingOccurred(Fish fish1, Fish fish2) {
        breedingOccurred.add(fish1);
        breedingOccurred.add(fish2);
    }
}
