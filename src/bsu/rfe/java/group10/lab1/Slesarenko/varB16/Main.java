package bsu.rfe.java.group10.lab1.Slesarenko.varB16;

import java.util.Scanner;

// Apple/Big Cheese Potato/Fried Potato/Boiled

public class Main {
    public static void main(String[] args) throws Exception {
        Food[] breakfast = new Food[20];
        Scanner scanner = new  Scanner(System.in);
        int countPotato = 0, countApple = 0, countCheese = 0;
        System.out.println("Enter breakfast:");

        String input = scanner.nextLine();
        String[] buff = input.split(" ");

        int itemsSoFar = 0;

        for (String arg: buff) {
            String[] parts = arg.split("/");
            if (parts[0].equals("Cheese")) {
                breakfast[itemsSoFar] = new Cheese();
            } else if (parts[0].equals("Apple")) {
                breakfast[itemsSoFar] = new Apple(parts[1]);
            } else if (parts[0].equals("Potato")){
                breakfast[itemsSoFar] = new Potato(parts[1]);
            }
            itemsSoFar++;
        }

        for (Food item : breakfast) {
            if (item != null) {
                if (item.equals(new Apple("Big"))) {
                    countApple++;
                }
                else if (item.equals(new Potato("Fried"))||item.equals(new Potato("Boiled"))) {
                    countPotato++;
                }
                else if (item.equals(new Cheese())) {
                    countCheese++;
                }
                item.consume();
            }
            else break;
        }

        System.out.println("Amount of apples: " + countApple);
        System.out.println("Amount of cheese: " + countCheese);
        System.out.println("Amount of potato: " + countPotato);
    }
}




