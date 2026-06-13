package model;

/**
 * [KONSEP: ABSTRACT CLASS]
 * Abstract class adalah "cetak biru yang tidak bisa diinstansiasi langsung".
 * Berbeda dengan interface, abstract class bisa memiliki:
 *   - Field dengan state (data konkret)
 *   - Constructor
 *   - Metode konkret (sudah diimplementasikan)
 *   - Metode abstract (wajib diimplementasikan subclass)
 *
 * Kapan pakai Abstract Class vs Interface?
 *   → Abstract Class: ada state bersama + relasi IS-A yang kuat (Orang)
 *   → Interface: mendefinisikan kapabilitas/kontrak perilaku (Priceable)
 *
 * [KONSEP: IMPLEMENTS INTERFACE]
 * Orang mengimplementasikan Identifiable dan Displayable,
 * memaksa semua turunannya (Pelanggan, TourGuide) untuk patuh pada kontrak tersebut.
 */
public abstract class Orang implements Identifiable, Displayable {

    // Field protected: bisa diakses langsung oleh subclass (bukan private)
    protected final String id;
    protected String nama;
    protected String noTelepon;
    protected String email;

    /**
     * Constructor abstract class dipanggil via super() dari subclass.
     * Ini mencegah duplikasi inisialisasi field di setiap subclass.
     */
    public Orang(String id, String nama, String noTelepon, String email) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID tidak boleh kosong.");
        if (nama == null || nama.isBlank()) throw new IllegalArgumentException("Nama tidak boleh kosong.");
        this.id = id;
        this.nama = nama;
        this.noTelepon = noTelepon;
        this.email = email;
    }

    // [KONSEP: Implementasi kontrak Identifiable]
    @Override
    public String getId() { return id; }

    // Getter standar
    public String getNama() { return nama; }
    public String getNoTelepon() { return noTelepon; }
    public String getEmail() { return email; }

    // Setter dengan validasi
    public void setNama(String nama) {
        if (nama == null || nama.isBlank()) throw new IllegalArgumentException("Nama tidak boleh kosong.");
        this.nama = nama;
    }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
    public void setEmail(String email) { this.email = email; }

    /**
     * [KONSEP: ABSTRACT METHOD]
     * Metode ini WAJIB diimplementasikan oleh setiap subclass konkret.
     * Ini adalah inti dari Polymorphism berbasis inheritance —
     * setiap subclass mengekspresikan perannya secara unik.
     */
    public abstract String getPeran();

    /**
     * [KONSEP: POLYMORPHISM via Override]
     * toString() ini akan digunakan secara polimorfik ketika objek Orang
     * (baik Pelanggan maupun TourGuide) dicetak ke terminal.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s | %s | %s | %s",
            getPeran(), id, nama, noTelepon, email);
    }
}
