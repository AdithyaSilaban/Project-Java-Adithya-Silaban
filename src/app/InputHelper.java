package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * [InputHelper]
 * Utility class untuk mengelola semua input dari user via Scanner.
 * Mengenkapsulasi logika validasi input agar tidak berulang di setiap menu.
 *
 * Prinsip utama:
 *   - Setiap metode TIDAK kembali sampai input valid diterima
 *   - Pesan error informatif, bukan crash/exception mentah ke user
 *   - Scanner di-share (satu instance) di seluruh aplikasi
 */
public class InputHelper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Membaca satu baris teks. Loop sampai tidak kosong.
     * @param prompt Label yang ditampilkan sebelum input
     */
    public String bacaString(String prompt) {
        while (true) {
            System.out.print("  " + prompt + ": ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("  [!] Input tidak boleh kosong. Coba lagi.");
        }
    }

    /**
     * Membaca teks opsional — boleh kosong, langsung return "".
     */
    public String bacaStringOpsional(String prompt) {
        System.out.print("  " + prompt + " (kosongkan jika tidak ada): ");
        return scanner.nextLine().trim();
    }

    /**
     * Membaca bilangan bulat positif dengan validasi range.
     * @param min nilai minimum (inklusif)
     * @param max nilai maksimum (inklusif)
     */
    public int bacaInt(String prompt, int min, int max) {
        while (true) {
            System.out.print("  " + prompt + " [" + min + "-" + max + "]: ");
            String input = scanner.nextLine().trim();
            try {
                int nilai = Integer.parseInt(input);
                if (nilai >= min && nilai <= max) return nilai;
                System.out.println("  [!] Masukkan angka antara " + min + " dan " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Input harus berupa angka bulat.");
            }
        }
    }

    /**
     * Membaca bilangan desimal (double) dengan validasi minimum.
     */
    public double bacaDouble(String prompt, double min) {
        while (true) {
            System.out.print("  " + prompt + " (min: " + (long)min + "): ");
            String input = scanner.nextLine().trim();
            try {
                double nilai = Double.parseDouble(input.replace(",", ""));
                if (nilai >= min) return nilai;
                System.out.println("  [!] Nilai minimal adalah " + (long)min + ".");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Input harus berupa angka. Contoh: 850000");
            }
        }
    }

    /**
     * Membaca tanggal dengan format dd-MM-yyyy.
     * Validasi: tanggal harus di masa depan (setelah hari ini).
     */
    public LocalDate bacaTanggal(String prompt) {
        while (true) {
            System.out.print("  " + prompt + " (format: dd-MM-yyyy, contoh: 25-12-2026): ");
            String input = scanner.nextLine().trim();
            try {
                LocalDate tanggal = LocalDate.parse(input, DATE_FORMATTER);
                if (tanggal.isAfter(LocalDate.now())) return tanggal;
                System.out.println("  [!] Tanggal harus di masa depan (setelah " +
                    LocalDate.now().format(DATE_FORMATTER) + ").");
            } catch (DateTimeParseException e) {
                System.out.println("  [!] Format tanggal tidak valid. Gunakan: dd-MM-yyyy");
            }
        }
    }

    /**
     * Membaca konfirmasi Ya/Tidak dari user.
     * @return true jika user memilih Y/y, false jika N/n
     */
    public boolean bacaKonfirmasi(String prompt) {
        while (true) {
            System.out.print("  " + prompt + " (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y")) return true;
            if (input.equals("N")) return false;
            System.out.println("  [!] Masukkan Y untuk Ya atau N untuk Tidak.");
        }
    }

    /**
     * Menunggu user menekan Enter sebelum melanjutkan.
     * Digunakan setelah menampilkan hasil agar user bisa membaca.
     */
    public void tekanEnterUntukLanjut() {
        System.out.print("\n  Tekan [Enter] untuk kembali ke menu...");
        scanner.nextLine();
    }

    /**
     * Membaca pilihan menu — alias bacaInt yang lebih semantik.
     */
    public int bacaPilihan(int max) {
        return bacaInt("Pilihan Anda", 0, max);
    }
}
