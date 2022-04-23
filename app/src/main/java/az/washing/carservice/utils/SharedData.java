package az.washing.carservice.utils;

import java.util.HashMap;

public class SharedData {
   private static final HashMap<String, Object> DATA = new HashMap<>();

   public static <T> T grab(String key) {
      @SuppressWarnings("unchecked")
      T result = (T) DATA.get(key);

      DATA.clear();

      return result;
   }

   public static <T> void put(String key, T params) {
      DATA.put(key, params);
   }

   public static boolean has(String key) {
      return DATA.containsKey(key);
   }

   private SharedData() {
   }
}
