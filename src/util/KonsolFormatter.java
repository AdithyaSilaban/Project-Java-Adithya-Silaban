package util;

import model.*;

import java.util.List;

/**
 * [KONSEP: Utility Class]
 * KonsolFormatter menyediakan metode statis untuk memformat output ke terminal.
 * Tidak membutuhkan state → semua metode static.
 *
 * Analogi DFD: Formatter adalah "output device" dalam diagram —
 * menerima aliran data (List<DTO>, objek domain) dan mengubahnya
 * menjadi representasi teks yang bisa dibaca manusia.
 */
public class KonsolFormatter {

    private KonsolFormatter() {} // Private constructor: class utility tidak boleh diinstansiasi

    public static void cetakHeader(String judul) {
        String garis = "═".repeat(60);
        System.out.println("\n╔" + garis + "╗");
        System.out.printf("║  %-58s  ║%n", "✦ " + judul.toUpperCase());
        System.out.println("╚" + garis + "╝");
    }

    public static void cetakSubHeader(String teks) {
        System.out.println("\n  ── " + teks + " " + "─".repeat(Math.max(0, 50 - teks.length())));
    }

    public static void cetakSeparator() {
        System.out.println("  " + "─".repeat(65));
    }

    public static void cetakDaftarBooking(List<BookingRingkasanDTO> daftar) {
        if (daftar.isEmpty()) {
            System.out.println("  (Tidak ada data booking)");
            return;
        }
        System.out.printf("  %-12s | %-20s | %-25s | %-14s | %-10s | %s%n",
            "ID Booking", "Pelanggan", "Paket Wisata", "Status", "Berangkat", "Total Bayar");
        cetakSeparator();
        daftar.forEach(dto -> System.out.println("  " + dto));
    }

    public static void cetakDaftarPaket(List<PaketWisata> daftar) {
        if (daftar.isEmpty()) {
            System.out.println("  (Tidak ada paket)");
            return;
        }
        System.out.printf("  %-8s | %-28s | %-20s | %-8s | %s%n",
            "ID", "Nama Paket", "Tipe", "Durasi", "Harga/org");
        cetakSeparator();
        daftar.forEach(p -> System.out.printf(
            "  %-8s | %-28s | %-20s | %-6d hr | Rp %,.0f%n",
            p.getId(), p.getNamaPaket(), p.getTipePaket(), p.getDurasiHari(), p.getHargaDasar()
        ));
    }

    public static void cetakDaftarGuide(List<TourGuide> daftar) {
        if (daftar.isEmpty()) {
            System.out.println("  (Tidak ada tour guide)");
            return;
        }
        System.out.printf("  %-8s | %-20s | %-8s | %-6s | %s%n",
            "ID", "Nama", "Pengalaman", "Rating", "Bahasa");
        cetakSeparator();
        daftar.forEach(g -> System.out.printf(
            "  %-8s | %-20s | %-8d th | %-6.1f | %s%n",
            g.getId(), g.getNama(), g.getPengalaman(),
            g.getRatingRataRata(), String.join(", ", g.getBahasaDikuasai())
        ));
    }

    public static void cetakPesan(String tipe, String pesan) {
        String prefix = switch (tipe.toUpperCase()) {
            case "INFO"    -> "  ℹ ";
            case "SUCCESS" -> "  ✓ ";
            case "ERROR"   -> "  ✗ ";
            case "WARN"    -> "  ⚠ ";
            default        -> "  › ";
        };
        System.out.println(prefix + pesan);
    }
}
