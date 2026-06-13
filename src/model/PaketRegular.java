package model;

/**
 * [KONSEP: INHERITANCE - Subclass konkret dari PaketWisata]
 * PaketRegular adalah paket wisata kelompok dengan harga standar.
 * Mewarisi semua infrastruktur dari PaketWisata dan mengimplementasikan
 * metode abstract yang diwajibkan.
 *
 * [KONSEP: POLYMORPHISM]
 * Sebuah variabel bertipe PaketWisata dapat menampung objek PaketRegular ATAU PaketPrivate.
 * Ketika hitungHargaAkhir() dipanggil, JVM secara dinamis memilih
 * implementasi yang tepat berdasarkan tipe AKTUAL objek (Dynamic Dispatch).
 */
public class PaketRegular extends PaketWisata {

    private final int minPeserta; // Minimal peserta untuk paket bisa jalan

    public PaketRegular(String id, String namaPaket, String deskripsi,
                        KategoriPaket kategori, double hargaDasar,
                        int durasiHari, int kapasitasMaksimal, int minPeserta) {
        super(id, namaPaket, deskripsi, kategori, hargaDasar, durasiHari, kapasitasMaksimal);
        this.minPeserta = minPeserta;
    }

    @Override
    public String getTipePaket() { return "Paket Reguler (Kelompok)"; }

    /**
     * [KONSEP: Implementasi ABSTRACT METHOD + POLYMORPHISM]
     * Harga reguler: harga dasar dikalikan jumlah peserta.
     * Jika peserta >= 10, ada diskon 10% (early group discount).
     */
    @Override
    public double hitungHargaAkhir(int jumlahPeserta) {
        double total = hargaDasar * jumlahPeserta;
        if (jumlahPeserta >= 10) {
            total *= 0.90; // Diskon 10% untuk grup besar
        }
        return total;
    }

    /**
     * [KONSEP: Implementasi Displayable via POLYMORPHISM]
     * Metode tampilkanInfo() dipanggil secara polimorfik —
     * jika variabelnya bertipe PaketWisata tapi isinya PaketRegular,
     * implementasi ini yang dieksekusi.
     */
    @Override
    public void tampilkanInfo() {
        System.out.println("┌─ PAKET WISATA REGULER ────────────────────────────");
        tampilkanInfoDasar(); // Memanggil metode protected milik parent
        System.out.println("│  Min Peserta: " + minPeserta + " orang");
        System.out.println("│  Diskon     : 10% untuk ≥ 10 peserta");
        System.out.println("└───────────────────────────────────────────────────");
    }

    public int getMinPeserta() { return minPeserta; }
}
