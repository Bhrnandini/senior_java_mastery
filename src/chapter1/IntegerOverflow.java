package chapter1;

public class Overflow {

    public static void main(String[] args) {
        int max = Integer.MAX_VALUE;
        System.out.println("Max int: " + max);

        // 1. Silent wrap-around (Dangerous in billing, IDs, or backoff calculations)
        int wrapped = max + 1;
        System.out.println("Silent overflow (max + 1): " + wrapped);

        // 2. Fail-fast defense using Math.addExact()
        try {
            int safe = Math.addExact(max, 1);
            System.out.println("Safe result: " + safe);
        } catch (ArithmeticException e) {
            System.out.println("Caught overflow safely: " + e.getMessage());
        }

        // 3. Widening before arithmetic (when overflow shouldn't fail, just expand)
        long widened = (long) max + 1; // Note: (long)(max + 1) would STILL overflow first!
        System.out.println("Widened safely to long: " + widened);
    }
}
