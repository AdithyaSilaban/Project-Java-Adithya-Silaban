package model;

/**
 * [KONSEP: ENUMERATION (ENUM)]
 * Enum merepresentasikan himpunan konstanta yang terbatas dan bermakna.
 * Di sini kita mendefinisikan siklus hidup sebuah booking.
 * Keunggulan Enum vs konstanta biasa (static final int):
 *   - Type-safe: compiler mencegah nilai ilegal
 *   - Dapat membawa data & behavior sendiri
 *   - Mendukung switch-expression modern Java
 */
public enum StatusBooking {

    // Setiap konstanta Enum menyimpan label tampilan dan kode singkat
    PENDING("Menunggu Konfirmasi", "P"),
    DIKONFIRMASI("Dikonfirmasi", "K"),
    BERJALAN("Sedang Berjalan", "B"),
    SELESAI("Selesai", "S"),
    DIBATALKAN("Dibatalkan", "X");

    // [KONSEP: Field pada Enum] Setiap konstanta memiliki metadata sendiri
    private final String labelTampilan;
    private final String kode;

    // Constructor Enum selalu private secara implisit
    StatusBooking(String labelTampilan, String kode) {
        this.labelTampilan = labelTampilan;
        this.kode = kode;
    }

    public String getLabelTampilan() { return labelTampilan; }
    public String getKode() { return kode; }

    /**
     * Method statis untuk mencari enum berdasarkan kode singkat.
     * Contoh penggunaan: StatusBooking.dariKode("K") → DIKONFIRMASI
     */
    public static StatusBooking dariKode(String kode) {
        for (StatusBooking s : values()) {
            if (s.kode.equalsIgnoreCase(kode)) return s;
        }
        throw new IllegalArgumentException("Kode status tidak dikenal: " + kode);
    }

    @Override
    public String toString() {
        return "[" + kode + "] " + labelTampilan;
    }
}
