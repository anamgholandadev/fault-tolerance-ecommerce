package com.ufrn.faulttolerance.store.utils;
import java.util.UUID;

public class GenerateRandomIdHelper {
   public static String generateRandomId() {
       return UUID.randomUUID().toString();
   }
}
