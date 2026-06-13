package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * [KONSEP: ABSTRACT CLASS + IMPLEMENTS INTERFACE]
 * PaketWisata adalah abstract class yang mengimplementasikan Identifiable,
 * Displayable, dan Priceable — tiga kontrak sekaligus.
 *
 * Mengapa abstract? Karena kita tidak mau ada "PaketWisata generik" yang diinstansiasi.
 * Yang ada hanyalah PaketRegular atau PaketPrivate (subclass konkret).
 *
 * Analogi DFD: PaketWisata adalah "proses" utama dalam sistem —
 * menerima input (destinasi, durasi, harga), mengolahnya, dan menghasilkan
 * output berupa kontrak layanan yang bisa di-booking pelanggan.
 */
public abstract class PaketWisata implements Identifiable, Displayable, Priceable {

    protected final String id;
    protected String namaPaket;
    protected String deskripsi;
    protected KategoriPaket kategori; // [KONSEP: Penggunaan Enum sebagai tipe field]
    protected double hargaDasar;      // harga per-orang
    protected int durasiHari;
    protected int kapasitasMaksimal;
    protected final List<String> listDestinasi = new ArrayList<>();
    protected final List<String> listFasilitas = new ArrayList<>();

    public PaketWisata(String id, String namaPaket, String deskripsi,
                       KategoriPaket kategori, double hargaDasar,
                       int durasiHari, int kapasitasMaksimal) {
        this.id = id;
        this.namaPaket = namaPaket;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.hargaDasar = hargaDasar;
        this.durasiHari = durasiHari;
        this.kapasitasMaksimal = kapasitasMaksimal;
    }

    // [KONSEP: Implementasi kontrak Identifiable]
    @Override
    public String getId() { return id; }

    // [KONSEP: Implementasi kontrak Priceable]
    @Override
    public double getHargaDasar() { return hargaDasar; }

    /**
     * [KONSEP: ABSTRACT METHOD]
     * Setiap subclass paket mendefinisikan cara unik menghitung harga akhirnya.
     * PaketRegular: harga flat. PaketPrivate: ada surcharge layanan.
     */
    public abstract double hitungHargaAkhir(int jumlahPeserta);

    /**
     * [KONSEP: ABSTRACT METHOD]
     * Setiap jenis paket punya tipe layanan berbeda (Reguler, Private, dll).
     */
    public abstract String getTipePaket();

    // [KONSEP: Implementasi default method dari Priceable di-override dengan logika paket]
    @Override
    public double hitungTotalHarga(int jumlahPeserta) {
        // Mendelegasikan ke hitungHargaAkhir yang polimorfik
        return hitungHargaAkhir(jumlahPeserta);
    }

    // Metode utility untuk menambahkan destinasi dan fasilitas
    public void tambahDestinasi(String destinasi) { listDestinasi.add(destinasi); }
    public void tambahFasilitas(String fasilitas) { listFasilitas.add(fasilitas); }

    // Getter
    public String getNamaPaket() { return namaPaket; }
    public String getDeskripsi() { return deskripsi; }
    public KategoriPaket getKategori() { return kategori; }
    public int getDurasiHari() { return durasiHari; }
    public int getKapasitasMaksimal() { return kapasitasMaksimal; }
    public List<String> getListDestinasi() { return Collections.unmodifiableList(listDestinasi); }
    public List<String> getListFasilitas() { return Collections.unmodifiableList(listFasilitas); }

    /**
     * Metode tampilkan info bersama yang bisa dipanggil oleh subclass via super.
     */
    protected void tampilkanInfoDasar() {
        System.out.println("│  ID         : " + id);
        System.out.println("│  Nama       : " + namaPaket);
        System.out.println("│  Tipe       : " + getTipePaket());
        System.out.println("│  Kategori   : " + kategori);
        System.out.println("│  Durasi     : " + durasiHari + " hari");
        System.out.println("│  Kapasitas  : " + kapasitasMaksimal + " orang");
        System.out.printf( "│  Harga/org  : Rp %,.0f%n", hargaDasar);
        System.out.println("│  Destinasi  : " + String.join(", ", listDestinasi));
        System.out.println("│  Fasilitas  : " + String.join(", ", listFasilitas));
    }
}
