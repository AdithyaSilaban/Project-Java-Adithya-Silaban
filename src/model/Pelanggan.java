package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * [KONSEP: INHERITANCE (Pewarisan)]
 * Pelanggan EXTENDS Orang → mewarisi semua field dan metode Orang.
 * Pelanggan tidak perlu mendefinisikan ulang id, nama, noTelepon, email.
 *
 * Aliran data (DFD): Objek Pelanggan dibuat di Main/Service,
 * lalu dialirkan ke Repository untuk disimpan, dan ke Booking sebagai referensi.
 *
 * [KONSEP: NESTED CLASS - Static Nested Class]
 * RiwayatPerjalanan adalah static nested class di dalam Pelanggan.
 * "Static" berarti ia tidak butuh instance Pelanggan untuk diinstansiasi.
 * Cocok untuk entitas yang secara konseptual "milik" Pelanggan
 * tapi bisa berdiri sendiri secara teknis.
 */
public class Pelanggan extends Orang {

    private String nomorKTP;
    private String alamat;

    /**
     * [KONSEP: NESTED CLASS - Static Nested Class]
     * RiwayatPerjalanan adalah rekaman historis yang melekat pada konsep "Pelanggan".
     * Dengan menjadikannya nested class, kita menjaga encapsulation dan kohesi:
     * logika riwayat tidak tercecer ke package lain.
     */
    public static class RiwayatPerjalanan {
        private final String idBooking;
        private final String namaPaket;
        private final String tanggalBerangkat;
        private final double totalBayar;

        public RiwayatPerjalanan(String idBooking, String namaPaket,
                                  String tanggalBerangkat, double totalBayar) {
            this.idBooking = idBooking;
            this.namaPaket = namaPaket;
            this.tanggalBerangkat = tanggalBerangkat;
            this.totalBayar = totalBayar;
        }

        @Override
        public String toString() {
            return String.format("  Booking[%s] → %s | Berangkat: %s | Dibayar: Rp %,.0f",
                idBooking, namaPaket, tanggalBerangkat, totalBayar);
        }
    }

    // Koleksi riwayat perjalanan pelanggan ini
    private final List<RiwayatPerjalanan> riwayatPerjalananList = new ArrayList<>();

    /**
     * Constructor memanggil super() untuk menginisialisasi field Orang.
     * Ini adalah wujud nyata dari "constructor chaining" dalam inheritance.
     */
    public Pelanggan(String id, String nama, String noTelepon,
                     String email, String nomorKTP, String alamat) {
        super(id, nama, noTelepon, email); // Delegasi ke constructor Orang
        this.nomorKTP = nomorKTP;
        this.alamat = alamat;
    }

    /**
     * [KONSEP: Implementasi ABSTRACT METHOD]
     * Pelanggan mendefinisikan identitas perannya sendiri.
     * Metode ini dipanggil secara polimorfik dari Orang.toString().
     */
    @Override
    public String getPeran() { return "PELANGGAN"; }

    /**
     * [KONSEP: Implementasi kontrak Displayable]
     * Setiap subclass Orang menampilkan info dengan format yang relevan.
     */
    @Override
    public void tampilkanInfo() {
        System.out.println("┌─ DATA PELANGGAN ─────────────────────────────────");
        System.out.println("│  ID      : " + id);
        System.out.println("│  Nama    : " + nama);
        System.out.println("│  KTP     : " + nomorKTP);
        System.out.println("│  Telp    : " + noTelepon);
        System.out.println("│  Email   : " + email);
        System.out.println("│  Alamat  : " + alamat);
        System.out.println("│  Riwayat : " + riwayatPerjalananList.size() + " perjalanan");
        riwayatPerjalananList.forEach(r -> System.out.println(r));
        System.out.println("└──────────────────────────────────────────────────");
    }

    public void tambahRiwayat(RiwayatPerjalanan riwayat) {
        riwayatPerjalananList.add(riwayat);
    }

    // Getter
    public String getNomorKTP() { return nomorKTP; }
    public String getAlamat() { return alamat; }
    public List<RiwayatPerjalanan> getRiwayatPerjalananList() {
        return Collections.unmodifiableList(riwayatPerjalananList);
    }
}
