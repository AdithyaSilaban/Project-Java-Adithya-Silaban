package model;

/**
 * [KONSEP: INTERFACE]
 * Interface mendefinisikan KONTRAK perilaku tanpa implementasi (kecuali default method).
 * Setiap entitas dalam sistem yang bisa diidentifikasi wajib mengimplementasikan ini.
 *
 * Analogi DFD: Interface adalah "spesifikasi port" dari sebuah entitas data —
 * menjamin bahwa data masuk/keluar melalui jalur yang terdefinisi.
 */
public interface Identifiable {
    /**
     * Setiap entitas harus mampu mengembalikan ID uniknya sendiri.
     * Kontrak ini digunakan oleh GenericRepository untuk operasi CRUD.
     */
    String getId();

    /**
     * [KONSEP: Default Method pada Interface (Java 8+)]
     * Menyediakan implementasi bawaan yang bisa di-override.
     * Tidak memaksa semua implementor menulis ulang logika ini.
     */
    default String getIdentifierLabel() {
        return "ID-" + getId();
    }
}
