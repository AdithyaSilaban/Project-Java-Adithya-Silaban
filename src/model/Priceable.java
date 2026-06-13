package model;

/**
 * [KONSEP: INTERFACE dengan kontrak kalkulasi harga]
 * Interface ini diimplementasikan oleh entitas yang memiliki nilai finansial.
 * Pemisahan interface ini memungkinkan komponen pembayaran bekerja secara
 * independen tanpa mengetahui detail internal paket wisata.
 */
public interface Priceable {
    /**
     * Mengembalikan harga dasar sebelum diskon/surcharge.
     */
    double getHargaDasar();

    /**
     * [KONSEP: Default Method]
     * Menghitung harga akhir dengan logika bawaan (bisa di-override).
     * @param jumlahPeserta jumlah orang dalam booking
     */
    default double hitungTotalHarga(int jumlahPeserta) {
        return getHargaDasar() * jumlahPeserta;
    }
}
