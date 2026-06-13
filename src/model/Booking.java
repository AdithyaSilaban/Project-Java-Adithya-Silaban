package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * [KONSEP: Entitas Transaksi — menghubungkan Pelanggan, PaketWisata, dan TourGuide]
 * Booking adalah "dokumen kontrak" yang merekam kesepakatan antara semua pihak.
 *
 * Analogi DFD: Booking adalah node proses sentral —
 *   Input  : Pelanggan + PaketWisata + TourGuide + tanggal + peserta
 *   Proses : Validasi, kalkulasi harga final, penetapan status
 *   Output : Dokumen booking dengan status dan total pembayaran
 *
 * [KONSEP: LOCAL CLASS — digunakan di dalam method hitungDiskon()]
 * Local class adalah class yang dideklarasikan di dalam sebuah method.
 * Scope-nya terbatas hanya pada method tersebut.
 */
public class Booking implements Identifiable, Displayable {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final String idBooking;
    private final Pelanggan pelanggan;    // Referensi ke objek Pelanggan
    private final PaketWisata paket;     // Referensi ke objek PaketWisata (polimorfik!)
    private TourGuide tourGuide;          // Bisa null jika self-tour
    private final int jumlahPeserta;
    private final LocalDate tanggalBerangkat;
    private StatusBooking status;        // [KONSEP: Penggunaan Enum sebagai state]
    private double totalPembayaran;
    private String catatanKhusus;

    public Booking(String idBooking, Pelanggan pelanggan, PaketWisata paket,
                   TourGuide tourGuide, int jumlahPeserta, LocalDate tanggalBerangkat) {
        if (jumlahPeserta <= 0) throw new IllegalArgumentException("Jumlah peserta minimal 1.");
        if (jumlahPeserta > paket.getKapasitasMaksimal())
            throw new IllegalArgumentException("Melebihi kapasitas paket: " + paket.getKapasitasMaksimal());

        this.idBooking = idBooking;
        this.pelanggan = pelanggan;
        this.paket = paket;
        this.tourGuide = tourGuide;
        this.jumlahPeserta = jumlahPeserta;
        this.tanggalBerangkat = tanggalBerangkat;
        this.status = StatusBooking.PENDING; // Status awal selalu PENDING

        // Kalkulasi total pembayaran saat booking dibuat
        this.totalPembayaran = kalkulasiPembayaran();
    }

    /**
     * [KONSEP: LOCAL CLASS]
     * KalkulatorDiskon adalah local class yang hidup hanya di dalam method ini.
     * Digunakan untuk mengenkapsulasi logika diskon yang kompleks tanpa
     * mencemari scope class Booking. Ini adalah pola yang tepat saat
     * logika terlalu panjang untuk anonymous class tapi terlalu spesifik untuk top-level class.
     */
    private double kalkulasiPembayaran() {
        /**
         * LOCAL CLASS: KalkulatorDiskon
         * - Hanya bisa diakses di dalam method kalkulasiPembayaran()
         * - Bisa mengakses final/effectively-final variable dari enclosing method/class
         */
        class KalkulatorDiskon {
            private double diskonPersen = 0.0;
            private String alasanDiskon = "Tidak ada diskon";

            void evaluasiDiskon() {
                // Diskon early bird: pesan > 30 hari sebelum berangkat
                long hariSebelumBerangkat = java.time.temporal.ChronoUnit.DAYS.between(
                    LocalDate.now(), tanggalBerangkat
                );

                if (hariSebelumBerangkat > 30) {
                    diskonPersen = 0.05;
                    alasanDiskon = "Early Bird (>30 hari)";
                }

                // Diskon loyalty: pelanggan punya riwayat >= 3 perjalanan
                if (pelanggan.getRiwayatPerjalananList().size() >= 3) {
                    diskonPersen += 0.03;
                    alasanDiskon += " + Loyalty Member";
                }
            }

            double hitungHargaSetelahDiskon(double hargaSebelumDiskon) {
                return hargaSebelumDiskon * (1 - diskonPersen);
            }

            String getLaporanDiskon() {
                return String.format("Diskon %.0f%% [%s]", diskonPersen * 100, alasanDiskon);
            }
        }

        // [KONSEP: POLYMORPHISM] paket.hitungTotalHarga() memanggil implementasi
        // yang sesuai (PaketRegular atau PaketPrivate) secara dinamis
        double hargaSebelumDiskon = paket.hitungTotalHarga(jumlahPeserta);

        KalkulatorDiskon kalkulator = new KalkulatorDiskon();
        kalkulator.evaluasiDiskon();
        this.catatanKhusus = kalkulator.getLaporanDiskon();

        return kalkulator.hitungHargaSetelahDiskon(hargaSebelumDiskon);
    }

    /**
     * [KONSEP: State Machine via Enum]
     * Booking memiliki siklus hidup yang valid. Transisi status divalidasi di sini.
     */
    public void ubahStatus(StatusBooking statusBaru) {
        // Validasi transisi: SELESAI/DIBATALKAN adalah terminal state
        if (this.status == StatusBooking.SELESAI || this.status == StatusBooking.DIBATALKAN) {
            throw new IllegalStateException(
                "Booking " + idBooking + " sudah dalam status terminal: " + status);
        }
        this.status = statusBaru;
    }

    @Override
    public String getId() { return idBooking; }

    @Override
    public void tampilkanInfo() {
        System.out.println("┌─ DETAIL BOOKING ──────────────────────────────────");
        System.out.println("│  ID Booking   : " + idBooking);
        System.out.println("│  Status       : " + status);
        System.out.println("│  Pelanggan    : " + pelanggan.getNama() + " (" + pelanggan.getId() + ")");
        System.out.println("│  Paket Wisata : " + paket.getNamaPaket() + " [" + paket.getTipePaket() + "]");
        System.out.println("│  Tour Guide   : " + (tourGuide != null ? tourGuide.getNama() : "Self Tour"));
        System.out.println("│  Peserta      : " + jumlahPeserta + " orang");
        System.out.println("│  Berangkat    : " + tanggalBerangkat.format(FORMATTER));
        System.out.printf( "│  Total Bayar  : Rp %,.0f%n", totalPembayaran);
        System.out.println("│  Keterangan   : " + catatanKhusus);
        System.out.println("└───────────────────────────────────────────────────");
    }

    // Getter
    public String getIdBooking() { return idBooking; }
    public Pelanggan getPelanggan() { return pelanggan; }
    public PaketWisata getPaket() { return paket; }
    public TourGuide getTourGuide() { return tourGuide; }
    public int getJumlahPeserta() { return jumlahPeserta; }
    public LocalDate getTanggalBerangkat() { return tanggalBerangkat; }
    public StatusBooking getStatus() { return status; }
    public double getTotalPembayaran() { return totalPembayaran; }
    public String getCatatanKhusus() { return catatanKhusus; }
    public String getTanggalBerangkatStr() { return tanggalBerangkat.format(FORMATTER); }
}
