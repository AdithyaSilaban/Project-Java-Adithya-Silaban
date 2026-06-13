package model;

/**
 * [KONSEP: ENUMERATION (ENUM) - lanjutan]
 * Kategori paket wisata menentukan jenis pengalaman yang ditawarkan.
 * Enum ini juga membawa informasi tarif multiplier yang mempengaruhi kalkulasi harga.
 */
public enum KategoriPaket {

    PETUALANGAN("Petualangan & Alam", 1.2),
    BUDAYA("Budaya & Sejarah", 1.0),
    KULINER("Wisata Kuliner", 0.9),
    RELIGI("Wisata Religi", 0.85),
    PREMIUM("Paket Premium All-Inclusive", 1.8);

    private final String deskripsi;
    // Multiplier harga: PREMIUM 1.8x lipat dari harga dasar
    private final double multiplierHarga;

    KategoriPaket(String deskripsi, double multiplierHarga) {
        this.deskripsi = deskripsi;
        this.multiplierHarga = multiplierHarga;
    }

    public String getDeskripsi() { return deskripsi; }
    public double getMultiplierHarga() { return multiplierHarga; }

    @Override
    public String toString() { return deskripsi; }
}
