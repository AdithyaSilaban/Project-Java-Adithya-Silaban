package app;

import model.*;
import service.BookingService;

/**
 * [DataSeeder]
 * Mengisi data awal (seed data) ke dalam sistem saat aplikasi pertama dijalankan.
 * Pola ini umum digunakan di aplikasi backend agar tidak perlu input manual
 * setiap kali program dimulai ulang.
 *
 * Data yang di-seed:
 *   - 3 Paket Wisata (2 Regular, 1 Private)
 *   - 2 Tour Guide dengan sertifikasi
 *   - 3 Pelanggan terdaftar
 */
public class DataSeeder {

    /**
     * Mengisi semua data awal ke dalam BookingService.
     * Dipanggil sekali di awal program sebelum menu interaktif ditampilkan.
     */
    public static void seed(BookingService service) {
        seedPaketWisata(service);
        seedTourGuide(service);
        seedPelanggan(service);
    }

    private static void seedPaketWisata(BookingService service) {
        // Paket 1: Regular - Petualangan
        PaketRegular p1 = new PaketRegular(
            "PW001", "Samosir Adventure Classic",
            "Jelajahi Pulau Samosir dengan trekking, danau, dan budaya Batak",
            KategoriPaket.PETUALANGAN, 850_000, 3, 20, 4
        );
        p1.tambahDestinasi("Danau Toba"); p1.tambahDestinasi("Desa Tomok"); p1.tambahDestinasi("Bukit Holbung");
        p1.tambahFasilitas("Transportasi Ferry"); p1.tambahFasilitas("Penginapan 2 Malam"); p1.tambahFasilitas("Makan 3x Sehari");
        service.daftarPaket(p1);

        // Paket 2: Private - Premium
        PaketPrivate p2 = new PaketPrivate(
            "PW002", "Samosir Premium Getaway",
            "Pengalaman eksklusif dengan layanan premium dan butler pribadi",
            KategoriPaket.PREMIUM, 3_500_000, 5, 6, 0.25, true
        );
        p2.tambahDestinasi("Pulau Samosir"); p2.tambahDestinasi("Air Terjun Situmorang");
        p2.tambahDestinasi("Museum Batak"); p2.tambahDestinasi("Pantai Pasir Putih Parbaba");
        p2.tambahFasilitas("Speedboat Privat"); p2.tambahFasilitas("Villa Eksklusif Tepi Danau");
        p2.tambahFasilitas("Butler Pribadi"); p2.tambahFasilitas("Chef Masak di Tempat");
        service.daftarPaket(p2);

        // Paket 3: Regular - Budaya
        PaketRegular p3 = new PaketRegular(
            "PW003", "Samosir Cultural Tour",
            "Mendalami kekayaan budaya, musik, dan kuliner Batak",
            KategoriPaket.BUDAYA, 650_000, 2, 25, 5
        );
        p3.tambahDestinasi("Desa Tradisional Ambarita"); p3.tambahDestinasi("Makam Raja Sidabutar");
        p3.tambahFasilitas("Guide Lokal Berpengalaman"); p3.tambahFasilitas("Pertunjukan Gondang Batak");
        service.daftarPaket(p3);

        // Paket 4: Regular - Kuliner
        PaketRegular p4 = new PaketRegular(
            "PW004", "Samosir Culinary Journey",
            "Wisata kuliner otentik: arsik ikan mas, naniura, dan saksang",
            KategoriPaket.KULINER, 500_000, 1, 15, 3
        );
        p4.tambahDestinasi("Pasar Tradisional Pangururan"); p4.tambahDestinasi("Dapur Batak Autentik");
        p4.tambahFasilitas("Kelas Memasak Batak"); p4.tambahFasilitas("Makan Malam Tradisional");
        service.daftarPaket(p4);
    }

    private static void seedTourGuide(BookingService service) {
        TourGuide g1 = new TourGuide("TG001", "Maruli Simanullang",
            "0812-3456-7890", "maruli@samosirtour.id", 8);
        g1.tambahBahasa("Bahasa Indonesia"); g1.tambahBahasa("Inggris"); g1.tambahBahasa("Batak Toba");
        g1.updateRating(4.8);
        g1.tambahSertifikasi("BNSP-2019", "Tour Guide Nasional", "2019", "BNSP Indonesia");
        g1.tambahSertifikasi("ASEAN-2021", "ASEAN Tourism Professional", "2021", "ASEAN Tourism Federation");
        service.daftarGuide(g1);

        TourGuide g2 = new TourGuide("TG002", "Rosintan Nababan",
            "0813-9876-5432", "rosintan@samosirtour.id", 5);
        g2.tambahBahasa("Bahasa Indonesia"); g2.tambahBahasa("Mandarin");
        g2.updateRating(4.5);
        g2.tambahSertifikasi("BNSP-2020", "Tour Guide Nasional", "2020", "BNSP Indonesia");
        service.daftarGuide(g2);
    }

    private static void seedPelanggan(BookingService service) {
        service.daftarPelanggan("PLN001", "Hotman Pardede",
            "0811-2222-3333", "hotman@email.com", "1275021001800001", "Jl. Sisingamangaraja No.12, Medan");
        service.daftarPelanggan("PLN002", "Rima Saragih",
            "0822-4444-5555", "rima@email.com", "1275024504900002", "Jl. Diponegoro No.45, Siantar");
        service.daftarPelanggan("PLN003", "Darmawan Silalahi",
            "0855-6666-7777", "darmawan@email.com", "1275021502750003", "Jl. Veteran No.88, Balige");
    }
}
