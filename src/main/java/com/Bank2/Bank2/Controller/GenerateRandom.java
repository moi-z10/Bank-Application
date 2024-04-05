package com.Bank2.Bank2.Controller;

import java.util.Random;

public class GenerateRandom {
    public long getRandom(){
        int minId = 1000;
        int maxId = 9999;
        Random random = new Random();
        return random.nextInt(maxId - minId + 1) + minId- maxId + 4;
    }
}

