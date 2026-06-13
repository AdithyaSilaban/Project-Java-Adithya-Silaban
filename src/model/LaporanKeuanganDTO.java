package model;

/**
 * [KONSEP: RECORD - DTO kedua untuk laporan keuangan]
 * LaporanKeuanganDTO membawa agregat data keuangan dari service ke presentasi.
 * Immutability Record menjamin data laporan tidak bisa dimanipulasi setelah dibuat.
 */
public record LaporanKeuanganDTO(
    int totalBooking,
    int bookingSelesai,
    int bookingDibatalkan,
    int bookingAktif,
    double totalPendapatan,
    double pendapatanRataRata,
    String periodeRingkasan
) {
    /**
     * Compact constructor untuk validasi
     */
    public LaporanKeuanganDTO {
        if (totalBooking < 0) throw new IllegalArgumentException("Total booking tidak valid.");
    }

    public String cetakLaporan() {
        return String.format("""
            ╔══ LAPORAN KEUANGAN ══════════════════════════════╗
            ║  Periode        : %s
            ║  Total Booking  : %d transaksi
            ║  - Selesai      : %d
            ║  - Dibatalkan   : %d
            ║  - Aktif        : %d
            ║  Total Pendapatan : Rp %,.0f
            ║  Rata-rata/booking: Rp %,.0f
            ╚══════════════════════════════════════════════════╝""",
            periodeRingkasan, totalBooking, bookingSelesai,
            bookingDibatalkan, bookingAktif, totalPendapatan, pendapatanRataRata
        );
    }
}
