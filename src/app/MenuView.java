package app;

import model.*;

import java.util.List;

/**
 * [MenuView]
 * Bertanggung jawab HANYA untuk rendering tampilan ke terminal.
 * Tidak ada logika bisnis di sini — ia hanya menerima data dan mencetaknya.
 *
 * Pola ini adalah penerapan sederhana dari MVC (Model-View-Controller):
 *   Model      → package model/
 *   View       → MenuView (file ini)
 *   Controller → MenuController
 *
 * Dengan memisahkan View, jika nanti ingin mengganti tampilan (misal ke GUI/web),
 * hanya file ini yang perlu diubah — service dan model tidak tersentuh.
 */
public class MenuView {

    // ═══════════════════════════════════════════════════════════════════════
    //  HEADER & NAVIGASI
    // ═══════════════════════════════════════════════════════════════════════

    public void tampilkanSplashScreen() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("  ║                                                                  ║");
        System.out.println("  ║      S I S T E M   M A N A J E M E N   P A K E T   W I S A T A ║");
        System.out.println("  ║                  ★  P U L A U   S A M O S I R  ★                ║");
        System.out.println("  ║                                                                  ║");
        System.out.println("  ║             Agen Perjalanan Samosir Tour & Travel                ║");
        System.out.println("  ║                    Java 21 — OOP Edition                        ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  Data awal (seed) berhasil dimuat.");
        System.out.println("  Ketik pilihan menu lalu tekan [Enter].");
        System.out.println();
    }

    public void tampilkanMenuUtama() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════╗");
        System.out.println("  ║             MENU UTAMA SISTEM WISATA             ║");
        System.out.println("  ╠══════════════════════════════════════════════════╣");
        System.out.println("  ║  1.  Manajemen Paket Wisata                      ║");
        System.out.println("  ║  2.  Manajemen Tour Guide                        ║");
        System.out.println("  ║  3.  Manajemen Pelanggan                         ║");
        System.out.println("  ║  4.  Manajemen Booking                           ║");
        System.out.println("  ║  5.  Laporan & Statistik                         ║");
        System.out.println("  ║─────────────────────────────────────────────────║");
        System.out.println("  ║  0.  Keluar Program                              ║");
        System.out.println("  ╚══════════════════════════════════════════════════╝");
    }

    public void tampilkanMenuPaket() {
        System.out.println();
        System.out.println("  ┌── MENU: PAKET WISATA ────────────────────────────┐");
        System.out.println("  │  1. Lihat Semua Paket                            │");
        System.out.println("  │  2. Lihat Detail Paket                           │");
        System.out.println("  │  3. Tambah Paket Reguler Baru                    │");
        System.out.println("  │  4. Tambah Paket Private Baru                    │");
        System.out.println("  │  5. Cari Paket berdasarkan Kategori              │");
        System.out.println("  │─────────────────────────────────────────────────│");
        System.out.println("  │  0. Kembali ke Menu Utama                        │");
        System.out.println("  └──────────────────────────────────────────────────┘");
    }

    public void tampilkanMenuGuide() {
        System.out.println();
        System.out.println("  ┌── MENU: TOUR GUIDE ──────────────────────────────┐");
        System.out.println("  │  1. Lihat Semua Tour Guide (urut rating)         │");
        System.out.println("  │  2. Lihat Detail Tour Guide                      │");
        System.out.println("  │  3. Daftarkan Tour Guide Baru                    │");
        System.out.println("  │  4. Tambah Sertifikasi Guide                     │");
        System.out.println("  │  5. Update Rating Guide                          │");
        System.out.println("  │─────────────────────────────────────────────────│");
        System.out.println("  │  0. Kembali ke Menu Utama                        │");
        System.out.println("  └──────────────────────────────────────────────────┘");
    }

    public void tampilkanMenuPelanggan() {
        System.out.println();
        System.out.println("  ┌── MENU: PELANGGAN ───────────────────────────────┐");
        System.out.println("  │  1. Lihat Semua Pelanggan                        │");
        System.out.println("  │  2. Lihat Detail & Riwayat Pelanggan             │");
        System.out.println("  │  3. Daftarkan Pelanggan Baru                     │");
        System.out.println("  │─────────────────────────────────────────────────│");
        System.out.println("  │  0. Kembali ke Menu Utama                        │");
        System.out.println("  └──────────────────────────────────────────────────┘");
    }

    public void tampilkanMenuBooking() {
        System.out.println();
        System.out.println("  ┌── MENU: BOOKING ─────────────────────────────────┐");
        System.out.println("  │  1. Lihat Semua Booking                          │");
        System.out.println("  │  2. Lihat Detail Booking                         │");
        System.out.println("  │  3. Buat Booking Baru                            │");
        System.out.println("  │  4. Ubah Status Booking                          │");
        System.out.println("  │  5. Filter Booking berdasarkan Status            │");
        System.out.println("  │  6. Cari Booking Nilai Tinggi                    │");
        System.out.println("  │─────────────────────────────────────────────────│");
        System.out.println("  │  0. Kembali ke Menu Utama                        │");
        System.out.println("  └──────────────────────────────────────────────────┘");
    }

    public void tampilkanMenuLaporan() {
        System.out.println();
        System.out.println("  ┌── MENU: LAPORAN & STATISTIK ─────────────────────┐");
        System.out.println("  │  1. Ringkasan Semua Booking (Tabel)              │");
        System.out.println("  │  2. Laporan Keuangan                             │");
        System.out.println("  │  3. Booking Aktif (Pending & Dikonfirmasi)       │");
        System.out.println("  │─────────────────────────────────────────────────│");
        System.out.println("  │  0. Kembali ke Menu Utama                        │");
        System.out.println("  └──────────────────────────────────────────────────┘");
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  TABEL DATA
    // ═══════════════════════════════════════════════════════════════════════

    public void tampilkanTabelPaket(List<PaketWisata> daftar) {
        if (daftar.isEmpty()) { tampilkanKosong("paket wisata"); return; }
        System.out.println();
        garis(80);
        System.out.printf("  %-8s │ %-26s │ %-20s │ %5s hr │ %s%n",
            "ID", "Nama Paket", "Tipe", "Dur.", "Harga/org");
        garis(80);
        for (PaketWisata p : daftar) {
            System.out.printf("  %-8s │ %-26s │ %-20s │ %5d hr │ Rp %,.0f%n",
                p.getId(), truncate(p.getNamaPaket(), 26),
                truncate(p.getTipePaket(), 20), p.getDurasiHari(), p.getHargaDasar());
        }
        garis(80);
        System.out.printf("  Total: %d paket%n", daftar.size());
    }

    public void tampilkanTabelGuide(List<TourGuide> daftar) {
        if (daftar.isEmpty()) { tampilkanKosong("tour guide"); return; }
        System.out.println();
        garis(72);
        System.out.printf("  %-8s │ %-20s │ %5s thn │ %6s │ %s%n",
            "ID", "Nama", "Exp.", "Rating", "Bahasa");
        garis(72);
        for (TourGuide g : daftar) {
            System.out.printf("  %-8s │ %-20s │ %5d thn │  %.1f ★  │ %s%n",
                g.getId(), truncate(g.getNama(), 20), g.getPengalaman(),
                g.getRatingRataRata(), String.join(", ", g.getBahasaDikuasai()));
        }
        garis(72);
        System.out.printf("  Total: %d guide%n", daftar.size());
    }

    public void tampilkanTabelPelanggan(List<Pelanggan> daftar) {
        if (daftar.isEmpty()) { tampilkanKosong("pelanggan"); return; }
        System.out.println();
        garis(75);
        System.out.printf("  %-8s │ %-22s │ %-15s │ %-20s │ %s%n",
            "ID", "Nama", "No. Telepon", "Email", "Riwayat");
        garis(75);
        for (Pelanggan p : daftar) {
            System.out.printf("  %-8s │ %-22s │ %-15s │ %-20s │ %d perj.%n",
                p.getId(), truncate(p.getNama(), 22),
                p.getNoTelepon(), truncate(p.getEmail(), 20),
                p.getRiwayatPerjalananList().size());
        }
        garis(75);
        System.out.printf("  Total: %d pelanggan%n", daftar.size());
    }

    public void tampilkanTabelBooking(List<BookingRingkasanDTO> daftar) {
        if (daftar.isEmpty()) { tampilkanKosong("booking"); return; }
        System.out.println();
        garis(90);
        System.out.printf("  %-8s │ %-18s │ %-22s │ %-10s │ %-18s │ %s%n",
            "ID", "Pelanggan", "Paket", "Berangkat", "Status", "Total Bayar");
        garis(90);
        for (BookingRingkasanDTO b : daftar) {
            System.out.printf("  %-8s │ %-18s │ %-22s │ %-10s │ %-18s │ Rp %,.0f%n",
                b.idBooking(), truncate(b.namaPelanggan(), 18),
                truncate(b.namaPaket(), 22), b.tanggalBerangkat(),
                truncate(b.statusBooking(), 18), b.totalPembayaran());
        }
        garis(90);
        System.out.printf("  Total: %d booking%n", daftar.size());
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  PILIHAN ENUM
    // ═══════════════════════════════════════════════════════════════════════

    public void tampilkanPilihanKategori() {
        System.out.println();
        System.out.println("  Pilih Kategori Paket:");
        KategoriPaket[] kategori = KategoriPaket.values();
        for (int i = 0; i < kategori.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, kategori[i].getDeskripsi());
        }
    }

    public void tampilkanPilihanStatus() {
        System.out.println();
        System.out.println("  Pilih Status Baru:");
        StatusBooking[] statuses = StatusBooking.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, statuses[i]);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  FEEDBACK PESAN
    // ═══════════════════════════════════════════════════════════════════════

    public void sukses(String pesan) {
        System.out.println("\n  ✓ BERHASIL: " + pesan);
    }

    public void error(String pesan) {
        System.out.println("\n  ✗ ERROR: " + pesan);
    }

    public void info(String pesan) {
        System.out.println("  ℹ " + pesan);
    }

    public void subHeader(String teks) {
        System.out.println("\n  ── " + teks + " " + "─".repeat(Math.max(0, 48 - teks.length())));
    }

    private void tampilkanKosong(String entitas) {
        System.out.println("\n  (Belum ada data " + entitas + " yang terdaftar.)");
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  UTILITY
    // ═══════════════════════════════════════════════════════════════════════

    private void garis(int panjang) {
        System.out.println("  " + "─".repeat(panjang));
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() > maxLen ? s.substring(0, maxLen - 2) + ".." : s;
    }
}
