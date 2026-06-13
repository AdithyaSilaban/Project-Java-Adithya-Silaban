package model;

/**
 * [KONSEP: INHERITANCE - Subclass konkret kedua dari PaketWisata]
 * PaketPrivate menawarkan layanan eksklusif dengan surcharge tambahan.
 * Memiliki logika hitungHargaAkhir() yang berbeda dari PaketRegular —
 * inilah inti dari Polymorphism: satu interface, banyak implementasi.
 */
public class PaketPrivate extends PaketWisata {

    private final double persentaseSurcharge; // Surcharge layanan eksklusif (misal: 0.25 = 25%)
    private final boolean termasukButler;     // Layanan butler pribadi

    public PaketPrivate(String id, String namaPaket, String deskripsi,
                        KategoriPaket kategori, double hargaDasar,
                        int durasiHari, int kapasitasMaksimal,
                        double persentaseSurcharge, boolean termasukButler) {
        super(id, namaPaket, deskripsi, kategori, hargaDasar, durasiHari, kapasitasMaksimal);
        this.persentaseSurcharge = persentaseSurcharge;
        this.termasukButler = termasukButler;
    }

    @Override
    public String getTipePaket() { return "Paket Private (Eksklusif)"; }

    /**
     * [KONSEP: Override hitungHargaAkhir() berbeda dari PaketRegular]
     * PaketPrivate = harga dasar + surcharge eksklusif.
     * Tidak ada diskon grup — justru sebaliknya, ada premium.
     */
    @Override
    public double hitungHargaAkhir(int jumlahPeserta) {
        double totalDasar = hargaDasar * jumlahPeserta;
        double totalSurcharge = totalDasar * persentaseSurcharge;
        return totalDasar + totalSurcharge;
    }

    @Override
    public void tampilkanInfo() {
        System.out.println("┌─ PAKET WISATA PRIVATE ────────────────────────────");
        tampilkanInfoDasar();
        System.out.printf("│  Surcharge  : %.0f%%%n", persentaseSurcharge * 100);
        System.out.println("│  Butler     : " + (termasukButler ? "Termasuk ✓" : "Tidak Termasuk"));
        System.out.println("└───────────────────────────────────────────────────");
    }

    public double getPersentaseSurcharge() { return persentaseSurcharge; }
    public boolean isTermasukButler() { return termasukButler; }
}
