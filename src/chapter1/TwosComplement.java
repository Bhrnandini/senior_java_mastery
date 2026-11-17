package chapter1;

public class TwosComplement {

    public static void main(String[] args) {

        int min = Integer.MIN_VALUE;
        System.out.println(min);

        int abs_min = Math.abs(min);
        System.out.println(abs_min);

        try {
            int exactAbs = Math.absExact(min);
            System.out.println("Math.absExact(min): " + exactAbs);
        } catch (ArithmeticException e) {
            System.out.println("Caught overflow in absExact: " + e.getMessage());
        }

        // 3. Widening to 64-bit long before calling Math.abs()
        long safeLongAbs = Math.abs((long) min);
        System.out.println("Math.abs((long) min): " + safeLongAbs); // 2147483648
    }
}
//Math.abs() should return positive but it is silently returning same negative value, its not failing or throwing exception and
// so it is corrupting data and producing bugs.

/*
How the CPU Computes -min in Two's ComplementIn hardware, negating a Two's Complement number uses the rule:-x = (~x) + 1 (invert every bit, then add 1).Let's trace Integer.MIN_VALUE (-2,147,483,648) bit by bit:Start with min (-2,147,483,648):Plaintext1000 0000  0000 0000  0000 0000  0000 0000   (0x80000000)
Step 1 — Invert all bits (~min):Plaintext0111 1111  1111 1111  1111 1111  1111 1111   (0x7FFFFFFF = Integer.MAX_VALUE)
Step 2 — Add 1 (~min + 1):Plaintext  0111 1111  1111 1111  1111 1111  1111 1111
+ 0000 0000  0000 0000  0000 0000  0000 0001
--------------------------------------------
  1000 0000  0000 0000  0000 0000  0000 0000   (0x80000000 = -2,147,483,648)
The carry bit ripples all the way back into the 32nd sign bit, returning the exact same bit pattern you started with.3.


Why It Cannot Represent +2,147,483,648

A 32-bit int has $2^{32} = 4,294,967,296$ possible bit patterns:

1 pattern is used for 0 (0x00000000).

$2^{31} - 1$ patterns (2,147,483,647) are used for positive numbers (1 to 2,147,483,647).

$2^{31}$ patterns (2,147,483,648) are used for negative numbers (-1 down to -2,147,483,648).

Because 0 takes one of the non-negative slots (where the sign bit is 0), there is one extra negative number

that has no positive counterpart in 32 bits.
 */
