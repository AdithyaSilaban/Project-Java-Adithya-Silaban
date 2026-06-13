package app;

import service.BookingService;

import java.util.Scanner;

/**
 * ╔══════════════════════════════════════════════════════════════════════╗
 * ║       SISTEM MANAJEMEN PAKET WISATA SAMOSIR — INTERAKTIF             ║
 * ║       Entry Point: Inisialisasi semua komponen dan jalankan menu      ║
 * ╚══════════════════════════════════════════════════════════════════════╝
 *
 * Arsitektur runtime:
 *   Main → DataSeeder (seed data awal)
 *       → MenuView   (rendering tampilan)
 *       → InputHelper (baca & validasi input user)
 *       → MenuController (orkestrasi menu + call BookingService)
 *       → BookingService (logika bisnis + GenericRepository)
 */
public class Main {

    public static void main(String[] args) {

        // ── 1. Inisialisasi komponen utama ──────────────────────────────
        BookingService service = new BookingService();
        MenuView       view    = new MenuView();

        // Scanner dibuat SEKALI dan di-share ke seluruh komponen
        // agar tidak ada konflik buffering antar instance Scanner
        Scanner        scanner = new Scanner(System.in);
        InputHelper    input   = new InputHelper(scanner);

        // ── 2. Seed data awal ────────────────────────────────────────────
        // Muat data demo agar sistem tidak kosong saat pertama dijalankan
        System.out.println("\n  Memuat data awal sistem...");
        DataSeeder.seed(service);

        // ── 3. Tampilkan splash screen ──────────────────────────────────
        view.tampilkanSplashScreen();

        // ── 4. Jalankan loop menu interaktif ────────────────────────────
        MenuController controller = new MenuController(service, view, input);
        controller.jalankan();

        // ── 5. Tutup scanner saat program selesai ───────────────────────
        scanner.close();
    }
}
