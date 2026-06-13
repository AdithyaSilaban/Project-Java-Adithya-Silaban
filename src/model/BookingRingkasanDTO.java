package model;

/**
 * [KONSEP: RECORD (Java 16+, stable di Java 21)]
 * Record adalah fitur modern Java untuk membuat kelas Data Transfer Object (DTO)
 * yang IMMUTABLE secara ringkas. Compiler otomatis men-generate:
 *   - Constructor dengan semua komponen
 *   - Accessor methods (bukan getXxx(), tapi langsung nama field: nama(), email())
 *   - equals(), hashCode(), toString() yang benar
 *
 * Kapan pakai Record vs Class biasa?
 *   → Record: untuk membawa data antar lapisan (DTO), tidak butuh mutability
 *   → Class: entitas domain yang bisa berubah state-nya (Booking, Pelanggan)
 *
 * Di sini, Record digunakan sebagai DTO — objek yang membawa data ringkas
 * dari layer service ke layer presentasi, tanpa expose entitas domain penuh.
 *
 * Analogi DFD: Record adalah "payload paket data" yang bersih dan presisi
 * yang mengalir antar node proses — tidak ada overhead, tidak ada side effect.
 */

// DTO untuk ringkasan Booking (digunakan di laporan / konfirmasi)
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
) {
    /**
     * [KONSEP: Compact Constructor pada Record]
     * Bisa ditambahkan validasi di compact constructor.
     * Berbeda dari constructor biasa — tidak ada parameter list, langsung badan.
     */
    public BookingRingkasanDTO {
        if (totalPembayaran < 0)
            throw new IllegalArgumentException("Total pembayaran tidak boleh negatif.");
    }

    /**
     * [KONSEP: Custom method pada Record]
     * Record bukan hanya data — bisa punya method tambahan.
     */
    public String formatTotalPembayaran() {
        return String.format("Rp %,.0f", totalPembayaran);
    }

    /**
     * Record otomatis generate toString() yang rapi, tapi kita override untuk format laporan.
     */
    @Override
    public String toString() {
        return String.format(
            "%-12s | %-20s | %-25s | %-12s | %-10s | %s",
            idBooking, namaPelanggan, namaPaket,
            statusBooking, tanggalBerangkat, formatTotalPembayaran()
        );
    }
}
