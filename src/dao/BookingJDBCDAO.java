package dao;

import model.Booking;
import model.StatusBooking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * [KONSEP: JDBC — Data Access Object sebagai Blueprint]
 *
 * File ini adalah demonstrasi arsitektur nyata JDBC (Java Database Connectivity).
 * Kelas ini TIDAK akan dikompilasi untuk dijalankan karena membutuhkan
 * koneksi database aktif. Ia berdiri sebagai BLUEPRINT — cetak biru teknis
 * yang menunjukkan bagaimana PreparedStatement, ResultSet, Connection,
 * dan Transaction Management digunakan dalam konteks profesional.
 *
 * Jika proyek ini akan disambungkan ke database nyata (PostgreSQL/MySQL),
 * langkah yang diperlukan:
 *   1. Tambahkan driver JDBC ke classpath (misal: postgresql-42.x.jar)
 *   2. Ganti nilai konstan DB_URL, DB_USER, DB_PASSWORD
 *   3. Buat tabel booking di database (DDL tersedia di komentar bawah)
 *   4. Ubah BookingService untuk menggunakan BookingJDBCDAO ini
 *      alih-alih GenericRepository
 *
 * [ARSITEKTUR JDBC FLOW]
 * DriverManager → Connection → PreparedStatement → ResultSet → Model Object
 *      ↑                                                              ↓
 *   (pool)     ←←←←←←←← Mapping (mapRowToBooking) ←←←←←←←←←←←←←←←
 *
 * DDL Table yang dibutuhkan (PostgreSQL):
 * ─────────────────────────────────────────
 * CREATE TABLE booking (
 *     id_booking     VARCHAR(20) PRIMARY KEY,
 *     id_pelanggan   VARCHAR(20) NOT NULL,
 *     id_paket       VARCHAR(20) NOT NULL,
 *     id_guide       VARCHAR(20),
 *     jumlah_peserta INTEGER     NOT NULL CHECK (jumlah_peserta > 0),
 *     tgl_berangkat  DATE        NOT NULL,
 *     status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
 *     total_bayar    NUMERIC(15,2) NOT NULL,
 *     catatan        TEXT,
 *     created_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
 * );
 * ─────────────────────────────────────────
 *
 * CATATAN PENTING: Class ini sengaja menggunakan komponen yang membutuhkan
 * koneksi aktif (Connection, PreparedStatement, ResultSet) —
 * oleh karena itu, class ini TIDAK diinstansiasi di Main.java.
 * Ia adalah referensi arsitektur, bukan kode runtime aktif.
 */
public class BookingJDBCDAO implements IBookingDAO<Booking> {

    // ─── Konfigurasi koneksi database ────────────────────────────────────
    private static final String DB_URL      = "jdbc:postgresql://localhost:5432/samosir_tour";
    private static final String DB_USER     = "admin_samosir";
    private static final String DB_PASSWORD = "password_rahasia_ganti_ini";

    // ─── SQL Queries menggunakan Named Intent Pattern ─────────────────────
    private static final String SQL_INSERT =
        "INSERT INTO booking (id_booking, id_pelanggan, id_paket, id_guide, " +
        "jumlah_peserta, tgl_berangkat, status, total_bayar, catatan) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID =
        "SELECT * FROM booking WHERE id_booking = ?";

    private static final String SQL_FIND_ALL =
        "SELECT * FROM booking ORDER BY tgl_berangkat DESC";

    private static final String SQL_UPDATE_STATUS =
        "UPDATE booking SET status = ?, catatan = ? WHERE id_booking = ?";

    private static final String SQL_DELETE =
        "DELETE FROM booking WHERE id_booking = ?";

    /**
     * Membuka koneksi ke database.
     * Dalam aplikasi nyata, ini diganti dengan Connection Pool (HikariCP, C3P0).
     *
     * [KONSEP: JDBC - java.sql.Connection]
     * Connection merepresentasikan satu sesi komunikasi dengan database.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * [KONSEP: JDBC - PreparedStatement]
     * PreparedStatement LEBIH AMAN dari Statement biasa karena:
     *   1. Mencegah SQL Injection: input user tidak diinterpretasikan sebagai SQL
     *   2. Performa: query di-compile sekali oleh DB, dieksekusi berkali-kali
     *   3. Tipe data: setString(), setDate(), setDouble() memastikan tipe yang benar
     *
     * [KONSEP: TRANSACTION MANAGEMENT]
     * conn.setAutoCommit(false): mulai transaksi manual
     * conn.commit(): simpan semua perubahan jika sukses
     * conn.rollback(): batalkan semua perubahan jika ada error
     *
     * @throws Exception Melempar exception agar pemanggil bisa handle/log
     */
    @Override
    public void insert(Booking booking) throws Exception {
        // Try-with-resources: Connection & PreparedStatement otomatis ditutup
        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false); // Mulai transaksi eksplisit

            try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

                // Binding parameter: posisi sesuai urutan tanda '?' di SQL
                ps.setString(1, booking.getIdBooking());
                ps.setString(2, booking.getPelanggan().getId());
                ps.setString(3, booking.getPaket().getId());
                // Guide bisa null (self-tour) → gunakan setNull untuk NULL SQL
                if (booking.getTourGuide() != null) {
                    ps.setString(4, booking.getTourGuide().getId());
                } else {
                    ps.setNull(4, Types.VARCHAR);
                }
                ps.setInt(5, booking.getJumlahPeserta());
                // [KONSEP: JDBC] LocalDate → java.sql.Date untuk JDBC
                ps.setDate(6, Date.valueOf(booking.getTanggalBerangkat()));
                ps.setString(7, booking.getStatus().name()); // Enum → String
                ps.setDouble(8, booking.getTotalPembayaran());
                ps.setString(9, booking.getCatatanKhusus());

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected != 1) {
                    throw new SQLException("INSERT gagal: tidak ada baris yang terpengaruh.");
                }

                conn.commit(); // Konfirmasi transaksi
                System.out.println("[JDBC] Booking berhasil disimpan ke database: " + booking.getIdBooking());

            } catch (SQLException e) {
                conn.rollback(); // Batalkan jika ada error
                throw new Exception("Gagal menyimpan booking: " + e.getMessage(), e);
            }
        }
    }

    /**
     * [KONSEP: JDBC - ResultSet]
     * ResultSet adalah "kursor" yang menunjuk ke baris hasil query.
     * rs.next() menggerakkan kursor ke baris berikutnya.
     * rs.getString("kolom"), rs.getDouble("kolom") mengekstrak nilai per kolom.
     *
     * @return Optional.empty() jika tidak ditemukan (menghindari null)
     */
    @Override
    public Optional<Booking> findById(String id) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setString(1, id); // Binding parameter pencarian

            try (ResultSet rs = ps.executeQuery()) {
                // rs.next() → true jika ada baris hasil
                if (rs.next()) {
                    return Optional.of(mapRowToPartialBooking(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * [KONSEP: JDBC - Iterasi ResultSet untuk multiple rows]
     */
    @Override
    public List<Booking> findAll() throws Exception {
        List<Booking> hasil = new ArrayList<>();

        try (Connection conn = getConnection();
             // PreparedStatement tanpa parameter untuk SELECT *
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            // Iterasi setiap baris — setara dengan forEach di repository
            while (rs.next()) {
                hasil.add(mapRowToPartialBooking(rs));
            }
        }
        return hasil;
    }

    /**
     * [KONSEP: JDBC UPDATE dengan PreparedStatement]
     */
    @Override
    public void update(Booking booking) throws Exception {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATUS)) {

                ps.setString(1, booking.getStatus().name());
                ps.setString(2, booking.getCatatanKhusus());
                ps.setString(3, booking.getIdBooking());

                int rows = ps.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("UPDATE gagal: booking tidak ditemukan.");
                }
                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw new Exception("Gagal update booking: " + e.getMessage(), e);
            }
        }
    }

    /**
     * [KONSEP: JDBC DELETE]
     */
    @Override
    public boolean delete(String id) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * [KONSEP: ResultSet → Object Mapping (simulasi ORM Mapping)]
     * Metode ini memetakan satu baris ResultSet menjadi objek Booking parsial.
     * Dalam aplikasi nyata: juga JOIN ke tabel pelanggan, paket, guide
     * untuk mengisi referensi objek penuh. Di sini kita hanya ambil data
     * dari tabel booking saja sebagai demonstrasi.
     *
     * CATATAN: Metode ini mengembalikan Booking "stub" karena tidak ada
     * akses ke repository pelanggan/paket dari sini.
     * Dalam arsitektur nyata, gunakan JOIN atau lazy-loading.
     */
    private Booking mapRowToPartialBooking(ResultSet rs) throws SQLException {
        // [KONSEP: ResultSet] Ekstraksi data per kolom berdasarkan nama kolom
        String idBooking     = rs.getString("id_booking");
        String idPelanggan   = rs.getString("id_pelanggan");  // FK
        String idPaket       = rs.getString("id_paket");       // FK
        String statusStr     = rs.getString("status");
        StatusBooking status = StatusBooking.valueOf(statusStr);

        // Placeholder — di sistem nyata, ini akan fetch objek penuh dari repo terkait
        System.out.printf("[JDBC] Row terbaca: %s | Pelanggan: %s | Paket: %s | Status: %s%n",
            idBooking, idPelanggan, idPaket, status);

        // Mengembalikan null sebagai marker — di implementasi nyata, kembalikan Booking penuh
        // setelah resolve FK ke objek domain melalui PelangganDAO dan PaketDAO
        return null; // BLUEPRINT PLACEHOLDER
    }
}
