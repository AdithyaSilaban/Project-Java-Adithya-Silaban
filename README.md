# 📋 SOAL PRAKTIKUM PEMROGRAMAN BERORIENTASI OBJEK (PBO)
## Sistem Manajemen Paket Wisata — Pulau Samosir Tour & Travel

---

> **Mata Kuliah** : Pemrograman Berorientasi Objek (PBO)  
> **Topik** : Integrasi Konsep OOP Lanjutan dalam Satu Arsitektur Sistem  
> **Platform** : Java 21 (JDK 21) — Command Line Interface (CLI)  
> **IDE** : Visual Studio Code  
> **Sifat** : Individu 

---

## 🎯 Deskripsi Permasalahan

**PT. Samosir Tour & Travel** adalah sebuah agen perjalanan wisata yang beroperasi di kawasan Pulau Samosir, Danau Toba, Sumatera Utara. Agen ini mengelola berbagai paket wisata, mempekerjakan tour guide profesional, dan melayani pemesanan (booking) dari pelanggan yang datang dari berbagai daerah.

Selama ini, seluruh operasional agen dicatat secara manual menggunakan buku dan kertas. Hal ini menyebabkan berbagai masalah: data mudah hilang, kalkulasi harga sering salah, riwayat perjalanan pelanggan tidak tercatat rapi, dan laporan keuangan sulit dibuat.

**Anda ditugaskan** untuk membangun sebuah **Sistem Manajemen Paket Wisata** berbasis Java murni (Command Line Interface) yang mampu mengelola seluruh operasional agen tersebut secara digital. Sistem harus dirancang dengan arsitektur OOP yang bersih, mudah dikembangkan, dan mengintegrasikan seluruh konsep PBO yang telah dipelajari.

---

## 📦 Ruang Lingkup Sistem

Sistem yang dibangun harus mampu mengelola tiga entitas utama berikut, beserta relasi dan operasi di antara ketiganya:

| Entitas | Deskripsi |
|---|---|
| **Paket Wisata** | Produk utama agen — ada dua jenis: Paket Reguler (kelompok) dan Paket Private (eksklusif) |
| **Tour Guide** | Pemandu wisata profesional yang ditugaskan pada setiap booking |
| **Pelanggan** | Konsumen akhir yang melakukan pemesanan paket wisata |
| **Booking** | Transaksi pemesanan yang menghubungkan Pelanggan, Paket Wisata, dan Tour Guide |

---

## ✅ Ketentuan & Syarat Implementasi

Sistem yang Anda bangun **wajib mengintegrasikan seluruh 13 konsep** berikut dalam satu arsitektur yang logis dan tidak terkesan dipaksakan. Setiap konsep harus ditempatkan pada konteks yang tepat sesuai permasalahan bisnis.

---

### Konsep 1 — Inheritance (Pewarisan)

Sistem harus memiliki hierarki kelas yang memanfaatkan pewarisan secara bermakna.

**Ketentuan:**
- Buatlah sebuah kelas induk (parent class) bernama **`Orang`** yang merepresentasikan entitas manusia dalam sistem. Kelas ini memiliki atribut: `id`, `nama`, `noTelepon`, dan `email`.
- Kelas **`Pelanggan`** dan **`TourGuide`** harus mewarisi (`extends`) kelas `Orang`.
- Masing-masing subclass boleh menambahkan atribut spesifiknya sendiri:
  - `Pelanggan` tambahan: `nomorKTP`, `alamat`
  - `TourGuide` tambahan: `pengalaman` (tahun), `ratingRataRata`, `bahasaDikuasai`
- Konstruktor subclass harus memanggil `super()` untuk menginisialisasi field kelas induk.
- Buatlah juga hierarki kedua: kelas abstract **`PaketWisata`** sebagai induk dari **`PaketRegular`** dan **`PaketPrivate`**.

**Yang harus dibuktikan dalam kode:**
- Penggunaan kata kunci `extends` pada minimal 4 (empat) kelas berbeda.
- Penggunaan `super(...)` di dalam konstruktor subclass.
- Subclass dapat mengakses field `protected` milik superclass tanpa getter.

---

### Konsep 2 — Abstract Class

**Ketentuan:**
- Kelas **`Orang`** harus dideklarasikan sebagai `abstract class` sehingga tidak bisa diinstansiasi langsung. Alasannya: dalam sistem ini tidak ada entitas "Orang generik" — yang ada hanya Pelanggan atau TourGuide.
- Kelas **`PaketWisata`** juga harus `abstract`, karena tidak ada "paket wisata generik" — yang ada hanya PaketRegular atau PaketPrivate.
- Setiap abstract class harus memiliki minimal **satu abstract method** yang wajib diimplementasikan oleh subclassnya:
  - `Orang` → abstract method: `getPeran()` yang mengembalikan String peran (misalnya `"PELANGGAN"` atau `"TOUR GUIDE"`)
  - `PaketWisata` → abstract method: `hitungHargaAkhir(int jumlahPeserta)` dan `getTipePaket()`

**Yang harus dibuktikan dalam kode:**
- Kata kunci `abstract` pada deklarasi kelas dan metode.
- Bukti bahwa abstract method diimplementasikan oleh SEMUA subclass konkretnya.
- Jika ada kode yang mencoba `new Orang(...)` atau `new PaketWisata(...)`, compiler harus menolaknya.

---

### Konsep 3 — Interface

**Ketentuan:**
Buatlah minimal **tiga interface** yang merepresentasikan kontrak perilaku berbeda:

| Interface | Method yang Diwajibkan | Diimplementasikan oleh |
|---|---|---|
| `Identifiable` | `String getId()` | `Orang`, `PaketWisata`, `Booking` |
| `Displayable` | `void tampilkanInfo()` | `Orang`, `PaketWisata`, `Booking` |
| `Priceable` | `double getHargaDasar()` | `PaketWisata` |

- Interface `Identifiable` harus memiliki minimal satu **default method**, contoh: `default String getIdentifierLabel()` yang mengembalikan `"ID-" + getId()`.
- Interface `Priceable` harus memiliki minimal satu **default method** untuk kalkulasi harga total: `default double hitungTotalHarga(int jumlahPeserta)`.
- Setiap interface harus memiliki Javadoc yang menjelaskan tujuan kontrak tersebut.

**Yang harus dibuktikan dalam kode:**
- Kata kunci `interface` dan `implements`.
- Satu kelas mengimplementasikan **lebih dari satu** interface sekaligus (multiple interface).
- Penggunaan `default method` pada minimal satu interface.

---

### Konsep 4 — Polymorphism

**Ketentuan:**
Polymorphism harus muncul secara alami dari hierarki yang dibangun, bukan dibuat-buat.

**Skenario yang wajib diimplementasikan:**

**(a) Polymorphism berbasis Inheritance:**
- Sebuah variabel bertipe `PaketWisata` harus mampu menampung objek `PaketRegular` MAUPUN `PaketPrivate`.
- Ketika method `hitungHargaAkhir()` atau `tampilkanInfo()` dipanggil pada variabel tersebut, JVM harus secara otomatis memilih implementasi yang sesuai dengan tipe aktual objek (*dynamic dispatch*).

**(b) Polymorphism berbasis Interface:**
- Sebuah variabel bertipe `Displayable` harus mampu memanggil `tampilkanInfo()` pada objek `Pelanggan`, `TourGuide`, `PaketRegular`, atau `PaketPrivate` — masing-masing menghasilkan output yang berbeda.

**(c) Logika harga yang berbeda antar tipe paket:**
- `PaketRegular.hitungHargaAkhir(n)` = harga dasar × jumlah peserta. Jika peserta ≥ 10 orang, berikan **diskon grup 10%**.
- `PaketPrivate.hitungHargaAkhir(n)` = (harga dasar × jumlah peserta) + **surcharge eksklusif** (persentase yang dikonfigurasi saat pembuatan paket).

**Yang harus dibuktikan dalam kode:**
- Minimal satu contoh variabel supertype yang menampung objek subtype.
- Pemanggilan method yang menghasilkan output berbeda tergantung tipe aktual objek.

---

### Konsep 5 — Enumeration (Enum)

**Ketentuan:**
Buatlah minimal **dua Enum** yang bermakna dalam konteks bisnis:

**(a) `StatusBooking`** — merepresentasikan siklus hidup transaksi booking:
```
PENDING → DIKONFIRMASI → BERJALAN → SELESAI
                              ↓
                         DIBATALKAN
```
Setiap konstanta Enum harus membawa **dua field data**: `labelTampilan` (teks panjang untuk ditampilkan ke user) dan `kode` (kode singkat satu huruf). Tambahkan juga static method `dariKode(String kode)` untuk mencari konstanta berdasarkan kode singkat.

**(b) `KategoriPaket`** — merepresentasikan kategori pengalaman wisata:
- `PETUALANGAN`, `BUDAYA`, `KULINER`, `RELIGI`, `PREMIUM`
- Setiap konstanta membawa field `deskripsi` dan `multiplierHarga` (angka desimal yang merepresentasikan faktor pengali harga).

**Yang harus dibuktikan dalam kode:**
- Enum dengan field, konstruktor, dan method.
- Enum digunakan sebagai tipe field pada kelas lain (bukan sekadar dideklarasikan).
- Enum digunakan dalam `switch` atau `if-else` untuk logika bisnis.
- Penggunaan `values()` untuk iterasi semua konstanta.

---

### Konsep 6 — Nested Class (Static dan Non-Static)

Anda harus mengimplementasikan **kedua jenis** nested class, masing-masing pada konteks yang tepat.

**(a) Static Nested Class — `Pelanggan.RiwayatPerjalanan`:**
- Di dalam kelas `Pelanggan`, buatlah sebuah `public static class RiwayatPerjalanan`.
- Kelas ini menyimpan catatan historis satu perjalanan: `idBooking`, `namaPaket`, `tanggalBerangkat`, `totalBayar`.
- Karena bersifat `static`, kelas ini dapat diinstansiasi tanpa perlu instance `Pelanggan`: `new Pelanggan.RiwayatPerjalanan(...)`.
- `Pelanggan` menyimpan `List<RiwayatPerjalanan>` sebagai rekaman perjalanan yang pernah dilakukan.

**(b) Non-Static Inner Class — `TourGuide.SertifikasiGuide`:**
- Di dalam kelas `TourGuide`, buatlah sebuah `public class SertifikasiGuide` (tanpa kata kunci `static`).
- Kelas ini menyimpan data sertifikasi profesional: `kodeSertifikasi`, `namaSertifikasi`, `tahunDiperoleh`, `lembagaPenerbit`.
- Di dalam method `toString()` milik `SertifikasiGuide`, akses field `nama` milik `TourGuide` yang memilikinya menggunakan sintaks `TourGuide.this.nama`. Ini membuktikan bahwa inner class memiliki referensi implisit ke instance outer class-nya.
- Karena non-static, instansiasi hanya bisa dilakukan melalui instance outer class: `guide.new SertifikasiGuide(...)` atau melalui method factory di dalam `TourGuide`.

**Yang harus dibuktikan dalam kode:**
- Perbedaan cara instansiasi antara static nested class dan non-static inner class.
- Inner class mengakses field `private/protected` milik outer class-nya.
- Nested class digunakan secara nyata dalam logika sistem (bukan hanya dideklarasikan).

---

### Konsep 7 — Generics

**Ketentuan:**
Buatlah sebuah kelas **`GenericRepository<T extends Identifiable>`** di dalam package `repository`.

Kelas ini adalah implementasi Repository Pattern menggunakan Java Generics yang mensimulasikan ORM in-memory. Penjelasan komponen:
- `T` adalah type parameter — placeholder untuk tipe konkret yang akan digunakan.
- `extends Identifiable` adalah **upper bound** — membatasi bahwa `T` harus mengimplementasikan interface `Identifiable`, sehingga compiler tahu `T` memiliki method `getId()`.

Kelas ini harus menyediakan operasi CRUD lengkap:

| Method | Simulasi SQL | Deskripsi |
|---|---|---|
| `simpan(T entitas)` | `INSERT INTO` | Menyimpan entitas baru. Tolak jika ID sudah ada (simulasi constraint UNIQUE) |
| `cariById(String id)` | `SELECT WHERE id = ?` | Kembalikan `Optional<T>` untuk menghindari NullPointerException |
| `ambilSemua()` | `SELECT *` | Kembalikan `List<T>` yang tidak bisa dimodifikasi |
| `cariDenganFilter(Predicate<T> filter)` | `SELECT WHERE kondisi` | Menerima `Predicate<T>` sehingga kondisi filter bisa fleksibel |
| `update(T entitas)` | `UPDATE` | Perbarui entitas yang sudah ada |
| `hapus(String id)` | `DELETE WHERE id = ?` | Kembalikan `boolean` — `true` jika berhasil |
| `jumlah()` | `COUNT(*)` | Kembalikan jumlah entitas tersimpan |

Struktur data internal yang digunakan sebagai "tabel" in-memory adalah `LinkedHashMap<String, T>` (menggunakan `LinkedHashMap` agar urutan penyimpanan dipertahankan).

**Yang harus dibuktikan dalam kode:**
- Deklarasi kelas dengan type parameter dan upper bound: `class GenericRepository<T extends Identifiable>`.
- Satu class `GenericRepository` digunakan untuk menyimpan **empat tipe berbeda**: `Pelanggan`, `TourGuide`, `PaketWisata`, dan `Booking` — tanpa duplikasi kode.
- Penggunaan `Predicate<T>` sebagai parameter method untuk filter yang fleksibel.

---

### Konsep 8 — Record

**Ketentuan:**
Buatlah minimal **dua Record** yang berperan sebagai Data Transfer Object (DTO). Record digunakan untuk membawa data ringkas antar lapisan sistem tanpa mengekspos entitas domain secara penuh.

**(a) `BookingRingkasanDTO`** — ringkasan satu transaksi booking untuk ditampilkan di tabel laporan:
```java
public record BookingRingkasanDTO(
    String idBooking,
    String namaPelanggan,
    String namaPaket,
    String tipePaket,
    String namaGuide,
    int jumlahPeserta,
    String tanggalBerangkat,
    String statusBooking,
    double totalPembayaran
) { ... }
```

**(b) `LaporanKeuanganDTO`** — agregat data keuangan untuk laporan:
```java
public record LaporanKeuanganDTO(
    int totalBooking,
    int bookingSelesai,
    int bookingDibatalkan,
    int bookingAktif,
    double totalPendapatan,
    double pendapatanRataRata,
    String periodeRingkasan
) { ... }
```

Setiap Record harus memiliki:
- **Compact constructor** untuk validasi input (misal: `totalPembayaran` tidak boleh negatif).
- **Minimal satu custom method** tambahan di luar yang digenerate otomatis oleh compiler (misal: `formatTotalPembayaran()` yang mengembalikan string terformat `"Rp 1.250.000"`).
- **Override `toString()`** untuk format tampilan yang rapi di tabel CLI.

**Yang harus dibuktikan dalam kode:**
- Kata kunci `record` pada deklarasi.
- Accessor Record menggunakan nama komponen langsung (bukan `getXxx()`): misalnya `dto.totalPembayaran()`, bukan `dto.getTotalPembayaran()`.
- Record digunakan sebagai return type dari method service (bukan sebagai entitas yang disimpan di repository).

---

### Konsep 9 — Local Class

**Ketentuan:**
- Di dalam method **`kalkulasiPembayaran()`** milik kelas `Booking`, deklarasikan sebuah **local class** bernama `KalkulatorDiskon`.
- Local class ini hanya hidup di dalam method tersebut dan tidak bisa diakses dari luar.
- `KalkulatorDiskon` harus mengimplementasikan logika diskon sebagai berikut:

| Kondisi | Diskon |
|---|---|
| Booking dilakukan lebih dari 30 hari sebelum tanggal berangkat | Diskon Early Bird: 5% |
| Pelanggan memiliki riwayat perjalanan ≥ 3 kali | Diskon Loyalty Member: 3% |
| Kedua kondisi terpenuhi | Total diskon: 8% |

- `KalkulatorDiskon` harus memiliki minimal tiga method: `evaluasiDiskon()`, `hitungHargaSetelahDiskon(double)`, dan `getLaporanDiskon()`.
- Local class boleh mengakses field `private` dari kelas `Booking` yang mengandungnya (*effectively final*).

**Yang harus dibuktikan dalam kode:**
- Deklarasi kelas di dalam body method (bukan di dalam kelas, tapi di dalam method).
- Local class diinstansiasi dan digunakan di dalam method yang sama.
- Local class mengakses variable/field dari enclosing scope-nya.

---

### Konsep 10 — Anonymous Class

**Ketentuan:**
Anonymous class harus digunakan pada minimal **dua konteks berbeda** dalam sistem:

**(a) Sebagai `Comparator<TourGuide>` di dalam `BookingService`:**
- Method `semuaGuideUrut()` harus mengembalikan daftar guide yang diurutkan berdasarkan `ratingRataRata` dari tertinggi ke terendah.
- Pengurutan dilakukan menggunakan anonymous class yang mengimplementasikan `Comparator<TourGuide>`, bukan lambda.

```java
Comparator<TourGuide> urutByRating = new Comparator<TourGuide>() {
    @Override
    public int compare(TourGuide g1, TourGuide g2) {
        return Double.compare(g2.getRatingRataRata(), g1.getRatingRataRata());
    }
};
```

**(b) Sebagai `Predicate<Booking>` di dalam `BookingService`:**
- Method `bookingNilaiTinggi(double minimalBayar)` harus menggunakan anonymous class `Predicate<Booking>` sebagai filter, bukan lambda.
- Kondisi filter: booking tidak berstatus `DIBATALKAN` DAN nilai `totalPembayaran` ≥ parameter `minimalBayar`.

**Yang harus dibuktikan dalam kode:**
- Kata kunci `new InterfaceName() { ... }` untuk mendeklarasikan anonymous class secara inline.
- Anonymous class mengimplementasikan method abstract dari interface yang dituju.
- Komentar kode yang menjelaskan mengapa anonymous class dipilih vs lambda dalam konteks akademik ini.

---

### Konsep 11 — Simulasi ORM In-Memory

**Ketentuan:**
Sistem tidak menggunakan database sungguhan. Sebagai gantinya, `GenericRepository` (dari Konsep 7) berfungsi sebagai **simulasi ORM** dengan ketentuan:

- Gunakan `LinkedHashMap<String, T>` sebagai struktur data penyimpanan, di mana:
  - `String` (key) → merepresentasikan Primary Key di database
  - `T` (value) → merepresentasikan satu baris (row) data

- Operasi CRUD pada repository harus **mencetak log** ke konsol yang mensimulasikan query yang dieksekusi:
  ```
  [REPO] Pelanggan tersimpan: PLN001
  [REPO] Booking diperbarui: BK0001
  ```

- `BookingService` harus mengelola **empat repository** secara bersamaan untuk menjaga integritas referensial antar entitas:
  ```java
  GenericRepository<Pelanggan>   pelangganRepo;
  GenericRepository<PaketWisata> paketRepo;
  GenericRepository<TourGuide>   guideRepo;
  GenericRepository<Booking>     bookingRepo;
  ```

- Saat membuat booking baru, service harus **memvalidasi keberadaan** semua referensi (pelanggan, paket, dan guide) di repository masing-masing sebelum booking dibuat. Jika salah satu tidak ditemukan, lempar `NoSuchElementException` dengan pesan yang informatif.

**Yang harus dibuktikan dalam kode:**
- `GenericRepository` digunakan dengan empat tipe parameter berbeda tanpa duplikasi kode.
- Penanganan referensial integrity pada saat pembuatan booking.
- Penggunaan `Optional<T>` sebagai return type dari pencarian untuk menghindari `NullPointerException`.

---

### Konsep 12 — Konsep JDBC (Data Access Object / Blueprint)

**Ketentuan:**
Buatlah sebuah file **`BookingJDBCDAO.java`** di dalam package `dao` yang berfungsi sebagai **blueprint arsitektur JDBC**. File ini mendemonstrasikan bagaimana sistem ini akan dikoneksikan ke database sungguhan (PostgreSQL/MySQL) di masa depan.

Kelas ini harus:

**(a) Mengimplementasikan interface `IBookingDAO<Booking>`** yang mendefinisikan kontrak operasi DAO:
```java
public interface IBookingDAO<T> {
    void insert(T entitas) throws Exception;
    Optional<T> findById(String id) throws Exception;
    List<T> findAll() throws Exception;
    void update(T entitas) throws Exception;
    boolean delete(String id) throws Exception;
}
```

**(b) Mendemonstrasikan komponen JDBC berikut secara eksplisit:**

| Komponen JDBC | Deskripsi | Harus muncul di method |
|---|---|---|
| `java.sql.Connection` | Sesi koneksi ke database, dibuka via `DriverManager.getConnection()` | Semua method |
| `java.sql.PreparedStatement` | Query parameterik yang aman dari SQL Injection | `insert()`, `findById()`, `update()`, `delete()` |
| `ResultSet` | Kursor iterasi baris hasil query, via `rs.next()` dan `rs.getString()` | `findById()`, `findAll()` |
| Transaction Management | `conn.setAutoCommit(false)`, `conn.commit()`, `conn.rollback()` | `insert()`, `update()` |

**(c) Menyertakan DDL SQL** (sebagai komentar Javadoc) yang mendefinisikan struktur tabel `booking` yang dibutuhkan:
```sql
CREATE TABLE booking (
    id_booking     VARCHAR(20) PRIMARY KEY,
    id_pelanggan   VARCHAR(20) NOT NULL,
    id_paket       VARCHAR(20) NOT NULL,
    id_guide       VARCHAR(20),
    jumlah_peserta INTEGER     NOT NULL CHECK (jumlah_peserta > 0),
    tgl_berangkat  DATE        NOT NULL,
    status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    total_bayar    NUMERIC(15,2) NOT NULL,
    catatan        TEXT,
    created_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);
```

**(d) Catatan penting:**
- Kelas ini **tidak perlu dikompilasi bersama kelas lain** (tidak diinstansiasi di `Main.java`) karena membutuhkan driver JDBC yang tidak tersedia di lingkungan pengembangan ini.
- Sertakan komentar yang menjelaskan langkah-langkah yang diperlukan untuk mengaktifkannya (tambah driver JDBC, ubah URL koneksi, jalankan DDL, swap di Service).

---

### Konsep 13 — Sistem Menu Interaktif (Scanner / CLI)

**Ketentuan:**
Sistem harus dapat **menerima input dari user** secara interaktif melalui `Scanner`. Bangun layer presentasi CLI dengan arsitektur yang bersih:

**(a) `DataSeeder.java`** — mengisi data awal (seed data) saat program pertama kali dijalankan:
- Minimal 4 paket wisata (campuran Regular dan Private)
- Minimal 2 tour guide (dengan sertifikasi)
- Minimal 3 pelanggan terdaftar

**(b) `InputHelper.java`** — utility class untuk membaca dan memvalidasi input:
- `bacaString(String prompt)` — loop sampai input tidak kosong
- `bacaInt(String prompt, int min, int max)` — validasi range, ulangi jika input bukan angka
- `bacaDouble(String prompt, double min)` — validasi minimal, tahan format koma
- `bacaTanggal(String prompt)` — validasi format `dd-MM-yyyy` dan harus di masa depan
- `bacaKonfirmasi(String prompt)` — baca Y/N, ulangi jika input tidak valid
- `tekanEnterUntukLanjut()` — pause program sampai Enter ditekan

**(c) `MenuView.java`** — bertanggung jawab HANYA untuk rendering tampilan (pola MVC-View):
- Tampilkan menu dengan format box ASCII yang rapi
- Tampilkan data dalam format tabel dengan kolom yang sejajar
- Method terpisah untuk setiap menu: `tampilkanMenuUtama()`, `tampilkanMenuPaket()`, dll.

**(d) `MenuController.java`** — mengorkestrasi seluruh aliran menu:

Sistem menu harus memiliki struktur hierarki sebagai berikut:

```
MENU UTAMA
├── 1. Manajemen Paket Wisata
│   ├── 1. Lihat Semua Paket
│   ├── 2. Lihat Detail Paket (berdasarkan ID)
│   ├── 3. Tambah Paket Reguler Baru (input lengkap termasuk destinasi & fasilitas)
│   ├── 4. Tambah Paket Private Baru
│   └── 5. Cari Paket berdasarkan Kategori (gunakan Enum KategoriPaket)
│
├── 2. Manajemen Tour Guide
│   ├── 1. Lihat Semua Tour Guide (diurutkan rating tertinggi)
│   ├── 2. Lihat Detail Tour Guide
│   ├── 3. Daftarkan Tour Guide Baru
│   ├── 4. Tambah Sertifikasi Guide (gunakan Inner Class SertifikasiGuide)
│   └── 5. Update Rating Guide
│
├── 3. Manajemen Pelanggan
│   ├── 1. Lihat Semua Pelanggan
│   ├── 2. Lihat Detail & Riwayat Perjalanan (gunakan Static Nested Class)
│   └── 3. Daftarkan Pelanggan Baru
│
├── 4. Manajemen Booking
│   ├── 1. Lihat Semua Booking (tabel menggunakan BookingRingkasanDTO Record)
│   ├── 2. Lihat Detail Booking
│   ├── 3. Buat Booking Baru (validasi, preview harga, konfirmasi)
│   ├── 4. Ubah Status Booking (gunakan Enum StatusBooking sebagai state machine)
│   ├── 5. Filter Booking berdasarkan Status
│   └── 6. Cari Booking Nilai Tinggi (gunakan Anonymous Class Predicate)
│
└── 5. Laporan & Statistik
    ├── 1. Ringkasan Semua Booking (tampilkan BookingRingkasanDTO dalam tabel)
    ├── 2. Laporan Keuangan (tampilkan LaporanKeuanganDTO)
    └── 3. Booking Aktif (Pending, Dikonfirmasi, Berjalan)
```

**Ketentuan input pada fitur Buat Booking Baru:**
- Tampilkan terlebih dahulu daftar pelanggan, paket, dan guide yang tersedia sebelum meminta input.
- Validasi kapasitas: jumlah peserta tidak boleh melebihi kapasitas maksimal paket.
- Tour guide bersifat opsional — user boleh mengosongkan input untuk self-tour.
- Tampilkan estimasi harga sebelum konfirmasi.
- Minta konfirmasi (Y/N) sebelum booking benar-benar disimpan.

---

## 🗂️ Struktur Package yang Diharapkan

```
src/
├── app/
│   ├── Main.java              ← Entry point — inisialisasi dan jalankan controller
│   ├── DataSeeder.java        ← Seed data awal ke dalam service
│   ├── InputHelper.java       ← Utility wrapper Scanner dengan validasi
│   ├── MenuView.java          ← Rendering tampilan CLI (MVC View)
│   └── MenuController.java    ← Dispatcher menu dan orkestrasi alur
│
├── model/
│   ├── Identifiable.java      ← Interface kontrak getId() + default method
│   ├── Displayable.java       ← Interface kontrak tampilkanInfo()
│   ├── Priceable.java         ← Interface kontrak getHargaDasar() + default method
│   ├── StatusBooking.java     ← Enum siklus hidup booking (5 konstanta + field + method)
│   ├── KategoriPaket.java     ← Enum kategori wisata (5 konstanta + multiplier harga)
│   ├── Orang.java             ← Abstract Class (field protected, abstract getPeran())
│   ├── Pelanggan.java         ← extends Orang + Static Nested Class RiwayatPerjalanan
│   ├── TourGuide.java         ← extends Orang + Non-Static Inner Class SertifikasiGuide
│   ├── PaketWisata.java       ← Abstract Class implements Identifiable, Displayable, Priceable
│   ├── PaketRegular.java      ← extends PaketWisata (diskon grup ≥ 10 peserta)
│   ├── PaketPrivate.java      ← extends PaketWisata (surcharge eksklusif)
│   ├── Booking.java           ← Entitas transaksi + Local Class KalkulatorDiskon
│   ├── BookingRingkasanDTO.java ← Record DTO untuk laporan
│   └── LaporanKeuanganDTO.java  ← Record DTO untuk laporan keuangan
│
├── repository/
│   └── GenericRepository.java ← Generics<T extends Identifiable> + simulasi ORM
│
├── dao/
│   ├── IBookingDAO.java       ← Interface kontrak DAO (Generics)
│   └── BookingJDBCDAO.java    ← Blueprint JDBC (PreparedStatement, ResultSet, Transaction)
│
├── service/
│   └── BookingService.java    ← Business logic + Anonymous Class (Comparator & Predicate)
│
└── util/
    └── KonsolFormatter.java   ← Utility static untuk format output terminal
```

---

## 📊 Tabel Pemetaan Konsep ke File

| No | Konsep | File Utama | Kelas / Elemen Kunci |
|---|---|---|---|
| 1 | Inheritance | `Pelanggan.java`, `TourGuide.java`, `PaketRegular.java`, `PaketPrivate.java` | `extends Orang`, `extends PaketWisata`, `super(...)` |
| 2 | Abstract Class | `Orang.java`, `PaketWisata.java` | `abstract class`, `abstract String getPeran()`, `abstract double hitungHargaAkhir()` |
| 3 | Interface | `Identifiable.java`, `Displayable.java`, `Priceable.java` | `interface`, `implements`, `default method` |
| 4 | Polymorphism | `Booking.java`, `BookingService.java`, `MenuController.java` | Dynamic dispatch, variabel supertype menampung subtype |
| 5 | Enum | `StatusBooking.java`, `KategoriPaket.java` | Field pada Enum, constructor, method, `values()` |
| 6 | Nested Class | `Pelanggan.java`, `TourGuide.java` | `static class RiwayatPerjalanan`, `class SertifikasiGuide`, `TourGuide.this.nama` |
| 7 | Generics | `GenericRepository.java` | `<T extends Identifiable>`, `Predicate<T>`, `Optional<T>` |
| 8 | Record | `BookingRingkasanDTO.java`, `LaporanKeuanganDTO.java` | `record`, compact constructor, custom method, accessor tanpa `get` |
| 9 | Local Class | `Booking.java` → method `kalkulasiPembayaran()` | `class KalkulatorDiskon { ... }` di dalam method |
| 10 | Anonymous Class | `BookingService.java` | `new Comparator<TourGuide>() { ... }`, `new Predicate<Booking>() { ... }` |
| 11 | Simulasi ORM | `GenericRepository.java`, `BookingService.java` | `LinkedHashMap`, CRUD log, referensial integrity |
| 12 | JDBC Blueprint | `BookingJDBCDAO.java`, `IBookingDAO.java` | `PreparedStatement`, `ResultSet`, `commit()`, `rollback()`, DDL SQL |
| 13 | Menu Interaktif | `MenuController.java`, `InputHelper.java`, `MenuView.java` | `Scanner`, validasi input, menu berjenjang |

---

## ⚙️ Cara Kompilasi dan Menjalankan Program

### Prasyarat
- JDK 21 terinstal dan terkonfigurasi di PATH
- Visual Studio Code dengan ekstensi Java (Extension Pack for Java)

### Langkah Kompilasi

Buka terminal di VS Code (`Ctrl + `` ` ```) lalu jalankan dari root folder proyek:

```bash
# 1. Buat folder output untuk bytecode
mkdir out

# 2. Kompilasi semua file (kecuali BookingJDBCDAO yang membutuhkan driver)
javac -d out -sourcepath src \
  src/model/*.java \
  src/repository/*.java \
  src/dao/IBookingDAO.java \
  src/service/*.java \
  src/util/*.java \
  src/app/DataSeeder.java \
  src/app/InputHelper.java \
  src/app/MenuView.java \
  src/app/MenuController.java \
  src/app/Main.java
```

### Menjalankan Program

```bash
java -cp out app.Main
```

### Verifikasi Kompilasi Berhasil

Tidak ada pesan error di terminal setelah perintah `javac`. File `.class` akan muncul di dalam folder `out/` dengan struktur yang mencerminkan package.

---

## 📋 Kriteria Penilaian

| Kriteria | Bobot | Detail |
|---|---|---|
| **Kelengkapan Konsep** | 35% | Semua 13 konsep hadir dan diimplementasikan dengan benar |
| **Ketepatan Penempatan Konsep** | 25% | Setiap konsep ditempatkan pada konteks yang logis dan tidak dipaksakan |
| **Fungsionalitas Sistem** | 20% | Semua menu berjalan, input divalidasi, tidak ada crash saat operasi normal |
| **Kualitas Kode** | 10% | Penamaan bermakna, komentar Bahasa Indonesia yang menjelaskan konsep, tidak ada duplikasi berlebihan |
| **Komentar & Dokumentasi** | 10% | Setiap file memiliki Javadoc yang menjelaskan konsep yang diterapkan dan alasan pemilihannya |

---

## 💡 Petunjuk Pengerjaan

1. **Mulai dari model** — bangun semua kelas entitas (`Orang`, `Pelanggan`, `TourGuide`, `PaketWisata`, dst.) sebelum mengerjakan service dan controller.

2. **Interface dan Abstract Class lebih dulu** — pastikan `Identifiable`, `Displayable`, `Priceable`, dan kelas abstract sudah selesai sebelum membuat subclass konkret.

3. **Generics Repository** — setelah model selesai, bangun `GenericRepository<T>` dan pastikan ia bisa diinstansiasi dengan berbagai tipe.

4. **Service Layer** — setelah repository siap, bangun `BookingService` yang menggunakannya. Di sinilah Anonymous Class Comparator dan Predicate harus muncul.

5. **Menu interaktif terakhir** — bangun `InputHelper`, `MenuView`, dan `MenuController` paling akhir setelah logika bisnis selesai dan terverifikasi.

6. **Uji setiap fitur secara terpisah** — sebelum mengintegrasikan semua menu, pastikan setiap operasi bisnis (buat booking, ubah status, dll.) bekerja dengan benar di level service.

7. **Jangan menghapus komentar konsep** — komentar yang menjelaskan konsep OOP apa yang sedang diterapkan adalah bagian dari dokumentasi yang dinilai.

---

## ❓ FAQ

**Q: Apakah boleh menggunakan lambda untuk mengganti Anonymous Class?**  
A: Untuk keperluan akademik ini, **wajib menggunakan Anonymous Class** di dua tempat yang ditentukan (Comparator dan Predicate). Lambda boleh digunakan di tempat lain.

**Q: Apakah `BookingJDBCDAO.java` harus bisa dikompilasi bersama file lainnya?**  
A: Tidak wajib. File ini adalah blueprint arsitektur. Boleh dikecualikan dari perintah `javac` atau dikompilasi terpisah. Yang dinilai adalah kelengkapan dan ketepatan demonstrasi komponen JDBC di dalamnya.

**Q: Apakah data harus tersimpan setelah program ditutup?**  
A: Tidak. Sistem menggunakan penyimpanan in-memory (simulasi ORM dengan `HashMap`). Data akan hilang setiap kali program dijalankan ulang. `DataSeeder` akan mengisi ulang data awal secara otomatis.

**Q: Berapa minimal jumlah Paket Wisata yang harus ada di seed data?**  
A: Minimal 4 paket — dengan komposisi minimal 1 `PaketPrivate` dan sisanya `PaketRegular`, tersebar di minimal 3 kategori berbeda.

**Q: Apakah boleh menambahkan fitur di luar yang diminta?**  
A: Boleh, selama seluruh 13 konsep yang diwajibkan sudah terpenuhi terlebih dahulu.

---

*Selamat mengerjakan. Pastikan setiap baris kode yang Anda tulis memiliki tujuan yang jelas dan dapat Anda jelaskan secara lisan kepada dosen pembimbing.*
