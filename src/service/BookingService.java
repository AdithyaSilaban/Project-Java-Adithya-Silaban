package service;

import model.*;
import repository.GenericRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * [KONSEP: SERVICE LAYER — Lapisan Logika Bisnis]
 * BookingService mengorkestrasikan interaksi antar repositori.
 * Ia menerima objek domain, menjalankan aturan bisnis, lalu mendelegasikan
 * penyimpanan ke repository.
 *
 * Analogi DFD: Service adalah "proses" yang menerima aliran data dari
 * berbagai sumber (Pelanggan, Paket, Guide), mengolahnya menjadi Booking,
 * dan meneruskan hasilnya ke data store (Repository).
 *
 * [KONSEP: ANONYMOUS CLASS — digunakan sebagai Comparator inline]
 * Comparator untuk pengurutan data diimplementasikan sebagai anonymous class,
 * mendemonstrasikan cara mendefinisikan implementasi interface secara inline
 * tanpa membuat class terpisah.
 */
public class BookingService {

    // [KONSEP: GENERICS] Tiga repository dengan tipe berbeda, satu class template
    private final GenericRepository<Pelanggan> pelangganRepo;
    private final GenericRepository<PaketWisata> paketRepo;
    private final GenericRepository<TourGuide> guideRepo;
    private final GenericRepository<Booking> bookingRepo;

    private int counterBooking = 1; // Auto-increment ID sederhana

    public BookingService() {
        // Setiap repository diinstansiasi dengan tipe konkretnya masing-masing
        this.pelangganRepo  = new GenericRepository<>("Pelanggan");
        this.paketRepo      = new GenericRepository<>("PaketWisata");
        this.guideRepo      = new GenericRepository<>("TourGuide");
        this.bookingRepo    = new GenericRepository<>("Booking");
    }

    // ─── MANAJEMEN PELANGGAN ──────────────────────────────────────────────

    public Pelanggan daftarPelanggan(String id, String nama, String telp,
                                     String email, String ktp, String alamat) {
        Pelanggan p = new Pelanggan(id, nama, telp, email, ktp, alamat);
        pelangganRepo.simpan(p);
        return p;
    }

    public Optional<Pelanggan> cariPelanggan(String id) {
        return pelangganRepo.cariById(id);
    }

    public List<Pelanggan> semuaPelanggan() {
        return pelangganRepo.ambilSemua();
    }

    // ─── MANAJEMEN PAKET WISATA ───────────────────────────────────────────

    public void daftarPaket(PaketWisata paket) {
        paketRepo.simpan(paket);
    }

    public Optional<PaketWisata> cariPaket(String id) {
        return paketRepo.cariById(id);
    }

    public List<PaketWisata> semuaPaket() {
        return paketRepo.ambilSemua();
    }

    /**
     * [KONSEP: GENERICS + Predicate<T>]
     * Mencari paket berdasarkan kategori menggunakan lambda sebagai filter.
     * KategoriPaket adalah Enum yang digunakan sebagai parameter filter.
     */
    public List<PaketWisata> cariPaketByKategori(KategoriPaket kategori) {
        return paketRepo.cariDenganFilter(p -> p.getKategori() == kategori);
    }

    // ─── MANAJEMEN TOUR GUIDE ─────────────────────────────────────────────

    public void daftarGuide(TourGuide guide) {
        guideRepo.simpan(guide);
    }

    public Optional<TourGuide> cariGuide(String id) {
        return guideRepo.cariById(id);
    }

    /**
     * [KONSEP: ANONYMOUS CLASS sebagai Comparator]
     * Mengembalikan daftar guide diurutkan berdasarkan rating dari tertinggi.
     *
     * Anonymous Class adalah class tanpa nama yang diinstansiasi langsung
     * saat implementasi interface dibutuhkan sekali saja.
     * Alternatif modern: lambda (guide1, guide2) -> Double.compare(...)
     * Di sini kita pakai anonymous class untuk demonstrasi konsep eksplisit.
     */
    public List<TourGuide> semuaGuideUrut() {
        List<TourGuide> semuaGuide = new ArrayList<>(guideRepo.ambilSemua());

        // [KONSEP: ANONYMOUS CLASS]
        // Implementasi Comparator<TourGuide> tanpa membuat class ComparatorGuide terpisah
        Comparator<TourGuide> urutByRating = new Comparator<TourGuide>() {
            @Override
            public int compare(TourGuide g1, TourGuide g2) {
                // Descending: rating tertinggi di atas
                return Double.compare(g2.getRatingRataRata(), g1.getRatingRataRata());
            }
        };

        semuaGuide.sort(urutByRating);
        return semuaGuide;
    }

    // ─── MANAJEMEN BOOKING ────────────────────────────────────────────────

    /**
     * Membuat booking baru dengan validasi lengkap.
     *
     * Aliran Data (DFD):
     *   [Input: idPelanggan, idPaket, idGuide, peserta, tanggal]
     *      → Validasi existensi semua entitas
     *      → Konstruksi objek Booking (harga dikalkulasi otomatis)
     *      → Simpan ke bookingRepo
     *      → Tambahkan ke riwayat pelanggan
     *   [Output: Booking yang sudah tersimpan]
     */
    public Booking buatBooking(String idPelanggan, String idPaket,
                               String idGuide, int jumlahPeserta,
                               LocalDate tanggalBerangkat) {

        // Validasi keberadaan entitas
        Pelanggan pelanggan = pelangganRepo.cariById(idPelanggan)
            .orElseThrow(() -> new NoSuchElementException("Pelanggan tidak ditemukan: " + idPelanggan));

        PaketWisata paket = paketRepo.cariById(idPaket)
            .orElseThrow(() -> new NoSuchElementException("Paket tidak ditemukan: " + idPaket));

        TourGuide guide = null;
        if (idGuide != null && !idGuide.isBlank()) {
            guide = guideRepo.cariById(idGuide)
                .orElseThrow(() -> new NoSuchElementException("Guide tidak ditemukan: " + idGuide));
        }

        // Generate ID booking otomatis
        String idBooking = String.format("BK%04d", counterBooking++);

        // [KONSEP: POLYMORPHISM] — Booking menerima PaketWisata (bisa Regular/Private)
        Booking booking = new Booking(idBooking, pelanggan, paket, guide,
                                      jumlahPeserta, tanggalBerangkat);

        bookingRepo.simpan(booking);

        // Update riwayat pelanggan (simulasi join table)
        Pelanggan.RiwayatPerjalanan riwayat = new Pelanggan.RiwayatPerjalanan(
            idBooking, paket.getNamaPaket(),
            booking.getTanggalBerangkatStr(), booking.getTotalPembayaran()
        );
        pelanggan.tambahRiwayat(riwayat);

        return booking;
    }

    /**
     * Mengubah status booking dengan validasi state machine.
     */
    public void ubahStatusBooking(String idBooking, StatusBooking statusBaru) {
        Booking booking = bookingRepo.cariById(idBooking)
            .orElseThrow(() -> new NoSuchElementException("Booking tidak ditemukan: " + idBooking));

        booking.ubahStatus(statusBaru);
        bookingRepo.update(booking);

        System.out.println("  [SERVICE] Status booking " + idBooking +
                           " diubah menjadi: " + statusBaru);
    }

    public List<Booking> semuaBooking() {
        return bookingRepo.ambilSemua();
    }

    /**
     * [KONSEP: GENERICS + Predicate]
     * Filter booking berdasarkan status menggunakan Enum sebagai kriteria.
     */
    public List<Booking> bookingByStatus(StatusBooking status) {
        return bookingRepo.cariDenganFilter(b -> b.getStatus() == status);
    }

    // ─── LAPORAN & AGREGASI ───────────────────────────────────────────────

    /**
     * Menghasilkan List<BookingRingkasanDTO> dari semua booking.
     * [KONSEP: RECORD sebagai DTO] — Booking domain object dikonversi ke Record DTO
     * yang lebih ringkas dan immutable untuk keperluan pelaporan.
     */
    public List<BookingRingkasanDTO> generateRingkasanBooking() {
        return bookingRepo.ambilSemua().stream()
            .map(b -> new BookingRingkasanDTO(
                b.getIdBooking(),
                b.getPelanggan().getNama(),
                b.getPaket().getNamaPaket(),
                b.getPaket().getTipePaket(),
                b.getTourGuide() != null ? b.getTourGuide().getNama() : "Self Tour",
                b.getJumlahPeserta(),
                b.getTanggalBerangkatStr(),
                b.getStatus().getLabelTampilan(),
                b.getTotalPembayaran()
            ))
            .collect(Collectors.toList());
    }

    /**
     * Menghasilkan LaporanKeuanganDTO menggunakan data agregat.
     * [KONSEP: RECORD] — LaporanKeuanganDTO adalah immutable snapshot keuangan.
     */
    public LaporanKeuanganDTO generateLaporanKeuangan() {
        List<Booking> semua = bookingRepo.ambilSemua();

        long selesai    = semua.stream().filter(b -> b.getStatus() == StatusBooking.SELESAI).count();
        long dibatalkan = semua.stream().filter(b -> b.getStatus() == StatusBooking.DIBATALKAN).count();
        long aktif      = semua.size() - selesai - dibatalkan;

        double totalPendapatan = semua.stream()
            .filter(b -> b.getStatus() != StatusBooking.DIBATALKAN)
            .mapToDouble(Booking::getTotalPembayaran)
            .sum();

        double rataRata = semua.isEmpty() ? 0 : totalPendapatan / semua.size();

        return new LaporanKeuanganDTO(
            semua.size(), (int) selesai, (int) dibatalkan, (int) aktif,
            totalPendapatan, rataRata, "Semua Periode"
        );
    }

    /**
     * [KONSEP: ANONYMOUS CLASS sebagai FilterKriteria]
     * Mendapatkan booking dengan total pembayaran di atas threshold.
     * Menggunakan anonymous class Predicate untuk filter — bukan lambda —
     * untuk demonstrasi konsep yang eksplisit.
     *
     * @param minimalBayar batas minimal total pembayaran
     */
    public List<Booking> bookingNilaiTinggi(double minimalBayar) {
        // [KONSEP: ANONYMOUS CLASS mengimplementasikan Predicate<Booking>]
        Predicate<Booking> filterNilaiTinggi = new Predicate<Booking>() {
            @Override
            public boolean test(Booking booking) {
                return booking.getTotalPembayaran() >= minimalBayar
                    && booking.getStatus() != StatusBooking.DIBATALKAN;
            }
        };

        return bookingRepo.cariDenganFilter(filterNilaiTinggi);
    }

    // ─── METHOD TAMBAHAN UNTUK MENU INTERAKTIF ────────────────────────────

    /** Mengembalikan semua guide tanpa pengurutan (tanpa sort). */
    public List<TourGuide> semuaGuide() {
        return guideRepo.ambilSemua();
    }

    /** Mencari booking berdasarkan ID. */
    public Optional<Booking> cariBooking(String id) {
        return bookingRepo.cariById(id);
    }
}
