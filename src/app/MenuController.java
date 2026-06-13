package app;

import model.*;
import service.BookingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * [MenuController]
 * Mengorkestrasi seluruh aliran menu interaktif aplikasi.
 * Bertanggung jawab:
 *   - Menampilkan menu via MenuView
 *   - Membaca input via InputHelper
 *   - Memanggil operasi bisnis via BookingService
 *   - Menangani semua exception dengan pesan yang ramah user
 *
 * Pola: Controller menerima input → delegasi ke Service → hasilkan ke View
 *
 * Loop utama aplikasi berjalan di sini sampai user memilih "Keluar".
 */
public class MenuController {

    private final BookingService service;
    private final MenuView view;
    private final InputHelper input;

    public MenuController(BookingService service, MenuView view, InputHelper input) {
        this.service = service;
        this.view    = view;
        this.input   = input;
    }

    /**
     * Titik masuk loop utama aplikasi.
     * Menampilkan menu utama dan mendispatch ke sub-controller sesuai pilihan.
     */
    public void jalankan() {
        boolean jalan = true;
        while (jalan) {
            view.tampilkanMenuUtama();
            int pilihan = input.bacaPilihan(5);

            switch (pilihan) {
                case 1 -> menuPaketWisata();
                case 2 -> menuTourGuide();
                case 3 -> menuPelanggan();
                case 4 -> menuBooking();
                case 5 -> menuLaporan();
                case 0 -> {
                    System.out.println();
                    System.out.println("  Terima kasih telah menggunakan Samosir Tour & Travel.");
                    System.out.println("  Sampai jumpa! 🏔️");
                    System.out.println();
                    jalan = false;
                }
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  SUB-MENU 1: PAKET WISATA
    // ═══════════════════════════════════════════════════════════════════════

    private void menuPaketWisata() {
        boolean kembali = false;
        while (!kembali) {
            view.tampilkanMenuPaket();
            int pilihan = input.bacaPilihan(5);

            switch (pilihan) {
                case 1 -> aksLihatSemuaPaket();
                case 2 -> aksDetailPaket();
                case 3 -> aksTambahPaketRegular();
                case 4 -> aksTambahPaketPrivate();
                case 5 -> aksCariPaketByKategori();
                case 0 -> kembali = true;
            }
        }
    }

    private void aksLihatSemuaPaket() {
        view.subHeader("Daftar Semua Paket Wisata");
        view.tampilkanTabelPaket(service.semuaPaket());
        input.tekanEnterUntukLanjut();
    }

    private void aksDetailPaket() {
        view.subHeader("Detail Paket Wisata");
        view.tampilkanTabelPaket(service.semuaPaket());
        if (service.semuaPaket().isEmpty()) { input.tekanEnterUntukLanjut(); return; }

        String id = input.bacaString("Masukkan ID Paket");
        Optional<PaketWisata> opt = service.cariPaket(id.toUpperCase());
        if (opt.isPresent()) {
            System.out.println();
            opt.get().tampilkanInfo();
        } else {
            view.error("Paket dengan ID '" + id + "' tidak ditemukan.");
        }
        input.tekanEnterUntukLanjut();
    }

    private void aksTambahPaketRegular() {
        view.subHeader("Tambah Paket Wisata Reguler Baru");
        try {
            String id       = input.bacaString("ID Paket (contoh: PW005)").toUpperCase();
            String nama     = input.bacaString("Nama Paket");
            String deskripsi= input.bacaString("Deskripsi Singkat");
            KategoriPaket kat = pilihKategori();
            double harga    = input.bacaDouble("Harga per Orang (Rp)", 10_000);
            int durasi      = input.bacaInt("Durasi (hari)", 1, 30);
            int kapasitas   = input.bacaInt("Kapasitas Maksimal (orang)", 1, 100);
            int minPeserta  = input.bacaInt("Minimal Peserta", 1, kapasitas);

            PaketRegular paket = new PaketRegular(id, nama, deskripsi, kat,
                                                   harga, durasi, kapasitas, minPeserta);

            // Tambah destinasi
            System.out.println("\n  -- Tambah Destinasi (minimal 1, ketik 'selesai' untuk berhenti) --");
            while (true) {
                System.out.print("  Destinasi: ");
                String dest = input.bacaString("Nama Destinasi");
                if (dest.equalsIgnoreCase("selesai")) {
                    if (paket.getListDestinasi().isEmpty()) {
                        view.error("Minimal 1 destinasi harus diisi."); continue;
                    }
                    break;
                }
                paket.tambahDestinasi(dest);
            }

            // Tambah fasilitas
            System.out.println("\n  -- Tambah Fasilitas (ketik 'selesai' untuk berhenti) --");
            while (true) {
                System.out.print("  Fasilitas: ");
                String fas = input.bacaString("Nama Fasilitas");
                if (fas.equalsIgnoreCase("selesai")) break;
                paket.tambahFasilitas(fas);
            }

            service.daftarPaket(paket);
            view.sukses("Paket '" + nama + "' berhasil didaftarkan!");
            System.out.println();
            paket.tampilkanInfo();

        } catch (IllegalArgumentException e) {
            view.error(e.getMessage());
        }
        input.tekanEnterUntukLanjut();
    }

    private void aksTambahPaketPrivate() {
        view.subHeader("Tambah Paket Wisata Private Baru");
        try {
            String id       = input.bacaString("ID Paket (contoh: PW006)").toUpperCase();
            String nama     = input.bacaString("Nama Paket");
            String deskripsi= input.bacaString("Deskripsi Singkat");
            KategoriPaket kat = pilihKategori();
            double harga    = input.bacaDouble("Harga per Orang (Rp)", 10_000);
            int durasi      = input.bacaInt("Durasi (hari)", 1, 30);
            int kapasitas   = input.bacaInt("Kapasitas Maksimal (orang)", 1, 20);
            int surcharge   = input.bacaInt("Surcharge Eksklusif (%)", 0, 100);
            boolean butler  = input.bacaKonfirmasi("Apakah termasuk layanan Butler pribadi?");

            PaketPrivate paket = new PaketPrivate(id, nama, deskripsi, kat,
                                                   harga, durasi, kapasitas,
                                                   surcharge / 100.0, butler);

            System.out.println("\n  -- Tambah Destinasi (ketik 'selesai' untuk berhenti) --");
            while (true) {
                String dest = input.bacaString("Destinasi");
                if (dest.equalsIgnoreCase("selesai")) {
                    if (paket.getListDestinasi().isEmpty()) { view.error("Minimal 1 destinasi."); continue; }
                    break;
                }
                paket.tambahDestinasi(dest);
            }

            System.out.println("\n  -- Tambah Fasilitas (ketik 'selesai' untuk berhenti) --");
            while (true) {
                String fas = input.bacaString("Fasilitas");
                if (fas.equalsIgnoreCase("selesai")) break;
                paket.tambahFasilitas(fas);
            }

            service.daftarPaket(paket);
            view.sukses("Paket Private '" + nama + "' berhasil didaftarkan!");
            System.out.println();
            paket.tampilkanInfo();

        } catch (IllegalArgumentException e) {
            view.error(e.getMessage());
        }
        input.tekanEnterUntukLanjut();
    }

    private void aksCariPaketByKategori() {
        view.subHeader("Cari Paket berdasarkan Kategori");
        KategoriPaket kat = pilihKategori();
        List<PaketWisata> hasil = service.cariPaketByKategori(kat);
        view.info("Ditemukan " + hasil.size() + " paket dalam kategori: " + kat.getDeskripsi());
        view.tampilkanTabelPaket(hasil);
        input.tekanEnterUntukLanjut();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  SUB-MENU 2: TOUR GUIDE
    // ═══════════════════════════════════════════════════════════════════════

    private void menuTourGuide() {
        boolean kembali = false;
        while (!kembali) {
            view.tampilkanMenuGuide();
            int pilihan = input.bacaPilihan(5);

            switch (pilihan) {
                case 1 -> aksLihatSemuaGuide();
                case 2 -> aksDetailGuide();
                case 3 -> aksTambahGuide();
                case 4 -> aksTambahSertifikasi();
                case 5 -> aksUpdateRatingGuide();
                case 0 -> kembali = true;
            }
        }
    }

    private void aksLihatSemuaGuide() {
        view.subHeader("Daftar Tour Guide (urut: rating tertinggi)");
        view.tampilkanTabelGuide(service.semuaGuideUrut());
        input.tekanEnterUntukLanjut();
    }

    private void aksDetailGuide() {
        view.subHeader("Detail Tour Guide");
        view.tampilkanTabelGuide(service.semuaGuideUrut());
        if (service.semuaGuide().isEmpty()) { input.tekanEnterUntukLanjut(); return; }

        String id = input.bacaString("Masukkan ID Guide").toUpperCase();
        service.cariGuide(id).ifPresentOrElse(
            g -> { System.out.println(); g.tampilkanInfo(); },
            () -> view.error("Guide '" + id + "' tidak ditemukan.")
        );
        input.tekanEnterUntukLanjut();
    }

    private void aksTambahGuide() {
        view.subHeader("Daftarkan Tour Guide Baru");
        try {
            String id   = input.bacaString("ID Guide (contoh: TG003)").toUpperCase();
            String nama = input.bacaString("Nama Lengkap");
            String telp = input.bacaString("No. Telepon");
            String email= input.bacaString("Email");
            int exp     = input.bacaInt("Pengalaman (tahun)", 0, 50);

            TourGuide guide = new TourGuide(id, nama, telp, email, exp);

            System.out.println("\n  -- Tambah Bahasa yang Dikuasai (ketik 'selesai' untuk berhenti) --");
            while (true) {
                String bhs = input.bacaString("Bahasa");
                if (bhs.equalsIgnoreCase("selesai")) {
                    if (guide.getBahasaDikuasai().isEmpty()) { view.error("Minimal 1 bahasa."); continue; }
                    break;
                }
                guide.tambahBahasa(bhs);
            }

            service.daftarGuide(guide);
            view.sukses("Tour Guide '" + nama + "' berhasil didaftarkan!");

        } catch (IllegalArgumentException e) {
            view.error(e.getMessage());
        }
        input.tekanEnterUntukLanjut();
    }

    private void aksTambahSertifikasi() {
        view.subHeader("Tambah Sertifikasi Tour Guide");
        view.tampilkanTabelGuide(service.semuaGuide());
        if (service.semuaGuide().isEmpty()) { input.tekanEnterUntukLanjut(); return; }

        String idGuide = input.bacaString("ID Guide").toUpperCase();
        Optional<TourGuide> opt = service.cariGuide(idGuide);
        if (opt.isEmpty()) { view.error("Guide tidak ditemukan."); input.tekanEnterUntukLanjut(); return; }

        TourGuide guide = opt.get();
        try {
            String kodeSert = input.bacaString("Kode Sertifikasi (contoh: BNSP-2024)");
            String namaSert = input.bacaString("Nama Sertifikasi");
            String tahun    = input.bacaString("Tahun Diperoleh");
            String lembaga  = input.bacaString("Lembaga Penerbit");

            // [KONSEP: Inner Class] SertifikasiGuide dibuat melalui instance outer class
            TourGuide.SertifikasiGuide sert = guide.tambahSertifikasi(kodeSert, namaSert, tahun, lembaga);
            view.sukses("Sertifikasi berhasil ditambahkan ke " + guide.getNama() + ".");
            System.out.println(sert);

        } catch (Exception e) {
            view.error(e.getMessage());
        }
        input.tekanEnterUntukLanjut();
    }

    private void aksUpdateRatingGuide() {
        view.subHeader("Update Rating Tour Guide");
        view.tampilkanTabelGuide(service.semuaGuide());
        if (service.semuaGuide().isEmpty()) { input.tekanEnterUntukLanjut(); return; }

        String idGuide = input.bacaString("ID Guide").toUpperCase();
        Optional<TourGuide> opt = service.cariGuide(idGuide);
        if (opt.isEmpty()) { view.error("Guide tidak ditemukan."); input.tekanEnterUntukLanjut(); return; }

        TourGuide guide = opt.get();
        view.info("Rating saat ini: " + guide.getRatingRataRata() + " / 5.0");
        try {
            int ratingBulat = input.bacaInt("Rating Baru (1-5)", 1, 5);
            guide.updateRating(ratingBulat);
            view.sukses("Rating " + guide.getNama() + " diperbarui menjadi " + ratingBulat + ".0");
        } catch (IllegalArgumentException e) {
            view.error(e.getMessage());
        }
        input.tekanEnterUntukLanjut();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  SUB-MENU 3: PELANGGAN
    // ═══════════════════════════════════════════════════════════════════════

    private void menuPelanggan() {
        boolean kembali = false;
        while (!kembali) {
            view.tampilkanMenuPelanggan();
            int pilihan = input.bacaPilihan(3);

            switch (pilihan) {
                case 1 -> aksLihatSemuaPelanggan();
                case 2 -> aksDetailPelanggan();
                case 3 -> aksTambahPelanggan();
                case 0 -> kembali = true;
            }
        }
    }

    private void aksLihatSemuaPelanggan() {
        view.subHeader("Daftar Semua Pelanggan");
        view.tampilkanTabelPelanggan(service.semuaPelanggan());
        input.tekanEnterUntukLanjut();
    }

    private void aksDetailPelanggan() {
        view.subHeader("Detail & Riwayat Pelanggan");
        view.tampilkanTabelPelanggan(service.semuaPelanggan());
        if (service.semuaPelanggan().isEmpty()) { input.tekanEnterUntukLanjut(); return; }

        String id = input.bacaString("Masukkan ID Pelanggan").toUpperCase();
        service.cariPelanggan(id).ifPresentOrElse(
            p -> { System.out.println(); p.tampilkanInfo(); },
            () -> view.error("Pelanggan '" + id + "' tidak ditemukan.")
        );
        input.tekanEnterUntukLanjut();
    }

    private void aksTambahPelanggan() {
        view.subHeader("Daftarkan Pelanggan Baru");
        try {
            String id     = input.bacaString("ID Pelanggan (contoh: PLN004)").toUpperCase();
            String nama   = input.bacaString("Nama Lengkap");
            String telp   = input.bacaString("No. Telepon");
            String email  = input.bacaString("Email");
            String ktp    = input.bacaString("Nomor KTP (16 digit)");
            String alamat = input.bacaString("Alamat Lengkap");

            Pelanggan p = service.daftarPelanggan(id, nama, telp, email, ktp, alamat);
            view.sukses("Pelanggan '" + nama + "' berhasil didaftarkan dengan ID: " + p.getId());

        } catch (IllegalArgumentException e) {
            view.error(e.getMessage());
        }
        input.tekanEnterUntukLanjut();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  SUB-MENU 4: BOOKING
    // ═══════════════════════════════════════════════════════════════════════

    private void menuBooking() {
        boolean kembali = false;
        while (!kembali) {
            view.tampilkanMenuBooking();
            int pilihan = input.bacaPilihan(6);

            switch (pilihan) {
                case 1 -> aksLihatSemuaBooking();
                case 2 -> aksDetailBooking();
                case 3 -> aksBuatBooking();
                case 4 -> aksUbahStatusBooking();
                case 5 -> aksFilterBookingByStatus();
                case 6 -> aksCariBookingNilaiTinggi();
                case 0 -> kembali = true;
            }
        }
    }

    private void aksLihatSemuaBooking() {
        view.subHeader("Daftar Semua Booking");
        view.tampilkanTabelBooking(service.generateRingkasanBooking());
        input.tekanEnterUntukLanjut();
    }

    private void aksDetailBooking() {
        view.subHeader("Detail Booking");
        view.tampilkanTabelBooking(service.generateRingkasanBooking());
        if (service.semuaBooking().isEmpty()) { input.tekanEnterUntukLanjut(); return; }

        String id = input.bacaString("Masukkan ID Booking").toUpperCase();
        service.cariBooking(id).ifPresentOrElse(
            b -> { System.out.println(); b.tampilkanInfo(); },
            () -> view.error("Booking '" + id + "' tidak ditemukan.")
        );
        input.tekanEnterUntukLanjut();
    }

    private void aksBuatBooking() {
        view.subHeader("Buat Booking Wisata Baru");

        // Tampilkan referensi data
        view.info("=== Data Pelanggan Tersedia ===");
        view.tampilkanTabelPelanggan(service.semuaPelanggan());

        if (service.semuaPelanggan().isEmpty()) {
            view.error("Belum ada pelanggan. Daftarkan pelanggan terlebih dahulu.");
            input.tekanEnterUntukLanjut(); return;
        }

        view.info("=== Paket Wisata Tersedia ===");
        view.tampilkanTabelPaket(service.semuaPaket());

        if (service.semuaPaket().isEmpty()) {
            view.error("Belum ada paket tersedia.");
            input.tekanEnterUntukLanjut(); return;
        }

        view.info("=== Tour Guide Tersedia ===");
        view.tampilkanTabelGuide(service.semuaGuide());

        try {
            System.out.println();
            String idPelanggan = input.bacaString("ID Pelanggan").toUpperCase();
            String idPaket     = input.bacaString("ID Paket Wisata").toUpperCase();

            // Ambil info kapasitas paket untuk validasi user-friendly
            Optional<PaketWisata> optPaket = service.cariPaket(idPaket);
            if (optPaket.isEmpty()) { view.error("Paket tidak ditemukan."); input.tekanEnterUntukLanjut(); return; }
            int kapasitas = optPaket.get().getKapasitasMaksimal();

            int peserta = input.bacaInt("Jumlah Peserta", 1, kapasitas);

            String idGuide = input.bacaStringOpsional("ID Tour Guide (kosongkan jika self-tour)").toUpperCase();
            if (idGuide.isEmpty()) idGuide = null;

            LocalDate tanggal = input.bacaTanggal("Tanggal Berangkat");

            // Preview simulasi harga sebelum konfirmasi
            System.out.println();
            view.info("Estimasi harga: Rp " + String.format("%,.0f",
                optPaket.get().hitungTotalHarga(peserta)) + " (sebelum diskon)");
            System.out.println();

            boolean konfirmasi = input.bacaKonfirmasi("Konfirmasi buat booking ini?");
            if (!konfirmasi) { view.info("Booking dibatalkan oleh user."); input.tekanEnterUntukLanjut(); return; }

            Booking booking = service.buatBooking(idPelanggan, idPaket, idGuide, peserta, tanggal);
            view.sukses("Booking berhasil dibuat!");
            System.out.println();
            booking.tampilkanInfo();

        } catch (Exception e) {
            view.error(e.getMessage());
        }
        input.tekanEnterUntukLanjut();
    }

    private void aksUbahStatusBooking() {
        view.subHeader("Ubah Status Booking");
        view.tampilkanTabelBooking(service.generateRingkasanBooking());
        if (service.semuaBooking().isEmpty()) { input.tekanEnterUntukLanjut(); return; }

        String idBooking = input.bacaString("ID Booking").toUpperCase();
        Optional<Booking> opt = service.cariBooking(idBooking);
        if (opt.isEmpty()) { view.error("Booking tidak ditemukan."); input.tekanEnterUntukLanjut(); return; }

        Booking booking = opt.get();
        view.info("Status saat ini: " + booking.getStatus());
        view.tampilkanPilihanStatus();

        StatusBooking[] statuses = StatusBooking.values();
        int pilStatus = input.bacaInt("Pilih Status Baru", 1, statuses.length);
        StatusBooking statusBaru = statuses[pilStatus - 1];

        try {
            service.ubahStatusBooking(idBooking, statusBaru);
            view.sukses("Status booking " + idBooking + " diubah ke: " + statusBaru);
        } catch (IllegalStateException e) {
            view.error(e.getMessage());
        }
        input.tekanEnterUntukLanjut();
    }

    private void aksFilterBookingByStatus() {
        view.subHeader("Filter Booking berdasarkan Status");
        view.tampilkanPilihanStatus();
        StatusBooking[] statuses = StatusBooking.values();
        int pilStatus = input.bacaInt("Pilih Status", 1, statuses.length);
        StatusBooking status = statuses[pilStatus - 1];

        List<Booking> hasil = service.bookingByStatus(status);
        view.info("Ditemukan " + hasil.size() + " booking dengan status: " + status);

        // Konversi ke DTO untuk ditampilkan di tabel
        List<BookingRingkasanDTO> dto = hasil.stream()
            .map(b -> new BookingRingkasanDTO(
                b.getIdBooking(), b.getPelanggan().getNama(),
                b.getPaket().getNamaPaket(), b.getPaket().getTipePaket(),
                b.getTourGuide() != null ? b.getTourGuide().getNama() : "Self Tour",
                b.getJumlahPeserta(), b.getTanggalBerangkatStr(),
                b.getStatus().getLabelTampilan(), b.getTotalPembayaran()
            )).toList();

        view.tampilkanTabelBooking(dto);
        input.tekanEnterUntukLanjut();
    }

    private void aksCariBookingNilaiTinggi() {
        view.subHeader("Cari Booking Nilai Tinggi");
        double threshold = input.bacaDouble("Masukkan Nilai Minimal (Rp)", 0);

        List<Booking> hasil = service.bookingNilaiTinggi(threshold);
        view.info("Ditemukan " + hasil.size() + " booking di atas Rp " +
                  String.format("%,.0f", threshold));

        List<BookingRingkasanDTO> dto = hasil.stream()
            .map(b -> new BookingRingkasanDTO(
                b.getIdBooking(), b.getPelanggan().getNama(),
                b.getPaket().getNamaPaket(), b.getPaket().getTipePaket(),
                b.getTourGuide() != null ? b.getTourGuide().getNama() : "Self Tour",
                b.getJumlahPeserta(), b.getTanggalBerangkatStr(),
                b.getStatus().getLabelTampilan(), b.getTotalPembayaran()
            )).toList();

        view.tampilkanTabelBooking(dto);
        input.tekanEnterUntukLanjut();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  SUB-MENU 5: LAPORAN
    // ═══════════════════════════════════════════════════════════════════════

    private void menuLaporan() {
        boolean kembali = false;
        while (!kembali) {
            view.tampilkanMenuLaporan();
            int pilihan = input.bacaPilihan(3);

            switch (pilihan) {
                case 1 -> aksRingkasanBooking();
                case 2 -> aksLaporanKeuangan();
                case 3 -> aksBookingAktif();
                case 0 -> kembali = true;
            }
        }
    }

    private void aksRingkasanBooking() {
        view.subHeader("Ringkasan Semua Booking");
        view.tampilkanTabelBooking(service.generateRingkasanBooking());
        input.tekanEnterUntukLanjut();
    }

    private void aksLaporanKeuangan() {
        view.subHeader("Laporan Keuangan");
        LaporanKeuanganDTO lap = service.generateLaporanKeuangan();
        System.out.println();
        System.out.println(lap.cetakLaporan());
        input.tekanEnterUntukLanjut();
    }

    private void aksBookingAktif() {
        view.subHeader("Booking Aktif (Pending & Dikonfirmasi)");

        List<Booking> pending = service.bookingByStatus(StatusBooking.PENDING);
        List<Booking> konfirmasi = service.bookingByStatus(StatusBooking.DIKONFIRMASI);
        List<Booking> berjalan = service.bookingByStatus(StatusBooking.BERJALAN);

        view.info("PENDING      : " + pending.size() + " booking");
        view.info("DIKONFIRMASI : " + konfirmasi.size() + " booking");
        view.info("BERJALAN     : " + berjalan.size() + " booking");

        // Gabungkan semua aktif
        List<Booking> semuaAktif = new java.util.ArrayList<>();
        semuaAktif.addAll(berjalan);
        semuaAktif.addAll(konfirmasi);
        semuaAktif.addAll(pending);

        List<BookingRingkasanDTO> dto = semuaAktif.stream()
            .map(b -> new BookingRingkasanDTO(
                b.getIdBooking(), b.getPelanggan().getNama(),
                b.getPaket().getNamaPaket(), b.getPaket().getTipePaket(),
                b.getTourGuide() != null ? b.getTourGuide().getNama() : "Self Tour",
                b.getJumlahPeserta(), b.getTanggalBerangkatStr(),
                b.getStatus().getLabelTampilan(), b.getTotalPembayaran()
            )).toList();

        view.tampilkanTabelBooking(dto);
        input.tekanEnterUntukLanjut();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  HELPER PRIVAT
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Menampilkan daftar kategori dan membaca pilihan user.
     * @return KategoriPaket yang dipilih
     */
    private KategoriPaket pilihKategori() {
        view.tampilkanPilihanKategori();
        KategoriPaket[] kategori = KategoriPaket.values();
        int pilihan = input.bacaInt("Pilih Kategori", 1, kategori.length);
        return kategori[pilihan - 1];
    }
}
