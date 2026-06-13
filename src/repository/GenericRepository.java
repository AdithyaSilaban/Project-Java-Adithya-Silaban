package repository;

import model.Identifiable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * [KONSEP: GENERICS + SIMULASI ORM IN-MEMORY]
 *
 * GenericRepository<T extends Identifiable> adalah implementasi Repository Pattern
 * menggunakan Java Generics. Ini mensimulasikan ORM (Object-Relational Mapping)
 * di mana objek Java di-persist ke struktur data in-memory (HashMap) alih-alih database.
 *
 * Komponen Generics:
 *   <T extends Identifiable>
 *   → T adalah "type parameter" — placeholder untuk tipe konkret
 *   → "extends Identifiable" adalah UPPER BOUND — T harus mengimplementasikan Identifiable
 *   → Tanpa bound ini, compiler tidak tahu bahwa T punya metode getId()
 *
 * Analogi DFD:
 *   Repository adalah "data store" dalam DFD — menerima aliran objek dari service,
 *   menyimpannya, dan mengembalikan objek yang diminta berdasarkan query.
 *
 * [KONSEP: SIMULASI ORM]
 * HashMap<String, T> mensimulasikan tabel database:
 *   - Key (String) = Primary Key kolom ID
 *   - Value (T)    = Row data yang direpresentasikan sebagai objek Java
 * CRUD operations mensimulasikan: INSERT, SELECT, UPDATE, DELETE
 */
public class GenericRepository<T extends Identifiable> {

    // "Tabel" in-memory: key=ID, value=Entitas
    private final Map<String, T> store = new LinkedHashMap<>(); // LinkedHashMap: mempertahankan urutan insert
    private final String namaEntitas; // Untuk pesan error yang informatif

    public GenericRepository(String namaEntitas) {
        this.namaEntitas = namaEntitas;
    }

    /**
     * [SIMULASI ORM: INSERT]
     * Menyimpan entitas baru. Melempar exception jika ID sudah ada (constraint UNIQUE).
     *
     * @param entitas Objek yang mengimplementasikan Identifiable (Pelanggan, TourGuide, dll)
     */
    public void simpan(T entitas) {
        if (store.containsKey(entitas.getId())) {
            throw new IllegalArgumentException(
                namaEntitas + " dengan ID '" + entitas.getId() + "' sudah ada.");
        }
        store.put(entitas.getId(), entitas);
        System.out.println("  [REPO] " + namaEntitas + " tersimpan: " + entitas.getId());
    }

    /**
     * [SIMULASI ORM: SELECT WHERE id = ?]
     * @return Optional<T> untuk menghindari NullPointerException (null-safe)
     */
    public Optional<T> cariById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * [SIMULASI ORM: SELECT * FROM table]
     * @return List yang tidak bisa dimodifikasi untuk menjaga integritas store
     */
    public List<T> ambilSemua() {
        return Collections.unmodifiableList(new ArrayList<>(store.values()));
    }

    /**
     * [SIMULASI ORM: SELECT * WHERE kondisi]
     * [KONSEP: GENERICS dengan Predicate<T>]
     * Predicate<T> adalah functional interface — memungkinkan query fleksibel
     * tanpa menulis method terpisah untuk setiap kondisi filter.
     *
     * Contoh pemanggilan:
     *   repo.cariDenganFilter(p -> p.getNama().startsWith("A"))
     *
     * @param filter Lambda / Predicate yang mendefinisikan kondisi pencarian
     */
    public List<T> cariDenganFilter(Predicate<T> filter) {
        return store.values().stream()
            .filter(filter)
            .collect(Collectors.toList());
    }

    /**
     * [SIMULASI ORM: UPDATE]
     * Memperbarui entitas yang sudah ada. Melempar exception jika tidak ditemukan.
     */
    public void update(T entitas) {
        if (!store.containsKey(entitas.getId())) {
            throw new NoSuchElementException(
                namaEntitas + " dengan ID '" + entitas.getId() + "' tidak ditemukan.");
        }
        store.put(entitas.getId(), entitas);
        System.out.println("  [REPO] " + namaEntitas + " diperbarui: " + entitas.getId());
    }

    /**
     * [SIMULASI ORM: DELETE WHERE id = ?]
     * @return true jika berhasil dihapus, false jika tidak ditemukan
     */
    public boolean hapus(String id) {
        boolean berhasil = store.remove(id) != null;
        if (berhasil) {
            System.out.println("  [REPO] " + namaEntitas + " dihapus: " + id);
        }
        return berhasil;
    }

    /**
     * [SIMULASI ORM: COUNT(*)]
     */
    public int jumlah() {
        return store.size();
    }

    /**
     * [SIMULASI ORM: SELECT EXISTS]
     */
    public boolean ada(String id) {
        return store.containsKey(id);
    }

    public String getNamaEntitas() { return namaEntitas; }
}
