package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * [KONSEP: INHERITANCE - subclass kedua dari Orang]
 * TourGuide juga IS-A Orang, tapi memiliki atribut dan perilaku yang berbeda.
 * Ini demonstrasi bahwa satu abstract class bisa punya banyak subclass
 * dengan implementasi yang beragam (fondasi Polymorphism).
 *
 * [KONSEP: NESTED CLASS - Non-Static Nested Class (Inner Class)]
 * SertifikasiGuide adalah inner class (non-static).
 * "Non-static" berarti setiap instance SertifikasiGuide secara implisit
 * memegang referensi ke instance TourGuide yang memilikinya.
 * Cocok saat nested class perlu mengakses data outer class-nya.
 */
public class TourGuide extends Orang {

    private int pengalaman; // dalam tahun
    private double ratingRataRata;
    private final List<String> bahasaDikuasai = new ArrayList<>();

    /**
     * [KONSEP: Non-Static Inner Class]
     * SertifikasiGuide adalah inner class yang bisa mengakses field outer class-nya.
     * Perhatikan di toString() cara ia mengakses `TourGuide.this.nama`
     * untuk merujuk ke nama TourGuide yang memiliki sertifikasi ini.
     */
    public class SertifikasiGuide {
        private final String kodeSertifikasi;
        private final String namaSertifikasi;
        private final String tahunDiperoleh;
        private final String lembagaPenerbit;

        // Constructor inner class hanya bisa dipanggil dari instance outer class
        public SertifikasiGuide(String kodeSertifikasi, String namaSertifikasi,
                                  String tahunDiperoleh, String lembagaPenerbit) {
            this.kodeSertifikasi = kodeSertifikasi;
            this.namaSertifikasi = namaSertifikasi;
            this.tahunDiperoleh = tahunDiperoleh;
            this.lembagaPenerbit = lembagaPenerbit;
        }

        @Override
        public String toString() {
            // Mengakses field outer class (nama) → bukti inner class punya referensi ke outer
            return String.format("  Sertifikasi[%s]: %s | Diterbitkan: %s | Tahun: %s | Guide: %s",
                kodeSertifikasi, namaSertifikasi, lembagaPenerbit, tahunDiperoleh, TourGuide.this.nama);
        }
    }

    private final List<SertifikasiGuide> sertifikasiList = new ArrayList<>();

    public TourGuide(String id, String nama, String noTelepon,
                     String email, int pengalaman) {
        super(id, nama, noTelepon, email);
        if (pengalaman < 0) throw new IllegalArgumentException("Pengalaman tidak boleh negatif.");
        this.pengalaman = pengalaman;
        this.ratingRataRata = 0.0;
    }

    /**
     * [KONSEP: Implementasi ABSTRACT METHOD getPeran()]
     * Setiap subclass WAJIB mengimplementasikan ini — dipaksa oleh abstract class Orang.
     */
    @Override
    public String getPeran() { return "TOUR GUIDE"; }

    /**
     * [KONSEP: Implementasi Displayable]
     * Format tampilan yang unik untuk TourGuide, berbeda dari Pelanggan.
     * Inilah Polymorphism: metode sama (tampilkanInfo), perilaku berbeda.
     */
    @Override
    public void tampilkanInfo() {
        System.out.println("┌─ DATA TOUR GUIDE ────────────────────────────────");
        System.out.println("│  ID          : " + id);
        System.out.println("│  Nama        : " + nama);
        System.out.println("│  Pengalaman  : " + pengalaman + " tahun");
        System.out.println("│  Rating      : " + String.format("%.1f", ratingRataRata) + " / 5.0");
        System.out.println("│  Bahasa      : " + String.join(", ", bahasaDikuasai));
        System.out.println("│  Sertifikasi : " + sertifikasiList.size() + " sertifikat");
        sertifikasiList.forEach(s -> System.out.println(s));
        System.out.println("└──────────────────────────────────────────────────");
    }

    /**
     * Cara membuat inner class: harus dari instance outer class-nya.
     * guideSaya.new SertifikasiGuide(...) adalah sintaks yang valid.
     */
    public SertifikasiGuide tambahSertifikasi(String kode, String nama,
                                               String tahun, String lembaga) {
        // Inner class diinstansiasi melalui instance outer class (implisit `this`)
        SertifikasiGuide sert = new SertifikasiGuide(kode, nama, tahun, lembaga);
        sertifikasiList.add(sert);
        return sert;
    }

    public void tambahBahasa(String bahasa) { bahasaDikuasai.add(bahasa); }
    public void updateRating(double rating) {
        if (rating < 0 || rating > 5) throw new IllegalArgumentException("Rating harus antara 0-5.");
        this.ratingRataRata = rating;
    }

    public int getPengalaman() { return pengalaman; }
    public double getRatingRataRata() { return ratingRataRata; }
    public List<String> getBahasaDikuasai() { return Collections.unmodifiableList(bahasaDikuasai); }
    public List<SertifikasiGuide> getSertifikasiList() { return Collections.unmodifiableList(sertifikasiList); }
}
